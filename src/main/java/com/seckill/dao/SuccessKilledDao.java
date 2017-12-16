package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {

    /**
     * 插入一条详细的购买信息
     * @param seckillId
     * @param userPhone
     * @return 成功插入返回1，否则返回0
     */
    int insertSuccessKilled(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);

    /**
     * 根据秒杀商品的id查询，明细信息
     * @param seckillId
     * @param uerPhone
     * @return 秒杀商品的明细信息
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userPhone")long uerPhone);
}
