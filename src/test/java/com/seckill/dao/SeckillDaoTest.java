package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

    //注入需要测试的类的实例
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void querySeckillNumber() throws Exception {

        System.out.println(seckillDao.querySeckillNumber());
    }
    @Test
    public void reduceNumber() throws Exception {
        long seckillId = 1000;
        Date killTime = new Date();
        int i = seckillDao.reduceNumber(seckillId, killTime);
        System.out.println(i);
    }

    @Test
    public void queryById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillDao.queryById(seckillId);
        System.out.println(seckill.toString());
    }

    @Test
    public void queryAll() throws Exception {
        int offset = 2;
        int limit = 3;
        List<Seckill> list = seckillDao.queryAll(offset, limit);
        Iterator iterator  = list.iterator();
        while(iterator.hasNext()) System.out.println(iterator.next());
    }

}