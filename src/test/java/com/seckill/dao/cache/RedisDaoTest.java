package com.seckill.dao.cache;

import com.seckill.dao.SeckillDao;
import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    @Resource
    private RedisDao redisDao;

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void setRedisDaoTest() throws Exception {

        long seckillId = 1000L;

        Seckill seckill = seckillDao.queryById(seckillId);

        redisDao.putSeckill(seckill);

        seckill = redisDao.getSeckill(seckill.getSeckillId());

        System.out.println(seckill.toString());


    }



}