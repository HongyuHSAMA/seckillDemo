package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {

        long seckillId = 1009L;
        long userPhone = 18126745521L;

        int result = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        logger.info(Integer.toString(result));
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {

        long seckillId = 1001L;
        long userPhone = 18126745520L;

        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
        logger.info(successKilled.toString());

    }

}