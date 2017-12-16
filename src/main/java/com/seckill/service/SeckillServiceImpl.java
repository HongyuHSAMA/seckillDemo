package com.seckill.service;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;
import com.seckill.exception.RepeaKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    /*日志记录*/
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    /*自定义用于md5加密的盐值*/
    private final String salt = "ljflajvoia332131ADSFJJL(&(*(*#@";

    final int pageSize = 5;//单页显示的数量

    //注入service依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    public SeckillServiceImpl() {
        //  Auto-generated constructor stub
    }

    /**
     * 查询全部的秒杀记录
     *
     * @return 数据库中所有的秒杀记录
     */
    /*
        pageSize(每个页面所显示的记录数)、
        pageCount(页面的总数)、
        showPage（目前显示第几页）、
        recordCount(总的记录数)
     */
    @Override
    public List<Seckill> getSeckillList(int showPage) {
        // Auto-generated method stub
        return seckillDao.queryAll((showPage - 1) * pageSize, pageSize);
    }

    @Override
    public int getPageCount() {
        int recordCount = seckillDao.querySeckillNumber();//总数
        int pageCount = (recordCount % pageSize == 0) ? (recordCount / pageSize) : (recordCount / pageSize + 1);
        return pageCount;
    }

    /**
     * 查询秒杀记录，通过商品Id
     */
    @Override
    public Seckill getById(long seckillId) {
        //  Auto-generated method stub
        return seckillDao.queryById(seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1：开发团队打成一致约定，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能短，不穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
     * 3：不是所有的方法都需要事务，比如只有一条修改操作，或者只读操作不需要事务控制
     */
    /**
     * 暴露秒杀地址
     */
    public Exposer exportSeckillUrl(long seckillId) {
        //  Auto-generated method stub

        /*Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null){
            return new Exposer(false,seckillId);
        }*/
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //访问数据库读取数据
            System.out.println("需要从数据库中获取数据");
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //放入redis中
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前系统时间
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);//

        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;

    }

    /**
     * 执行秒杀业务逻辑
     * @param seckillId 秒杀的商品Id
     * @param userPhone 手机号码
     * @param md5 md5加密值
     * @return
     * @throws SeckillException
     * @throws SeckillCloseException
     * @throws RepeaKillException
     */
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeaKillException {

        //在这一系列操作中，对应事务，return是commit,抛异常是rollback
        try {
            if (md5 == null || !md5.equals(getMD5(seckillId))) {
                logger.error("秒杀数据被篡改");
                throw new SeckillException("seckill data rewrite");
            }

            //执行秒杀逻辑： 减库存+记录购买行为
            Date nowTime = new Date();
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一：seckillId,userPhone
            if (insertCount <= 0) {
                logger.warn("不允许重复秒杀");
                throw new RepeaKillException("repeaKill !!");
            } else {
                //减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新记录，秒杀结束,可能时间结束也可能库存为零，rollback
                    logger.warn("没有更新数据库记录，秒杀已经结束了");
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功,commit
                    SuccessKilled successkilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successkilled);
                }

            }

        } catch (SeckillCloseException | RepeaKillException e1) {

            throw e1;
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }

    }

    /**
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStateEnum.DATE_PEWRITER);
        }

        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        //执行存储过程，result被复制

        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }

}
