package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeaKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void executeSeckillProcedure() throws Exception {

        long seckillId = 1001L;
        long userphone = 18126745520L;

        Exposer exposer = seckillService.exportSeckillUrl(seckillId);

        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userphone, md5);
            logger.info(execution.getStateInfo());
        }
    }

    @Test
    public void getPageCount() throws Exception {
        System.out.println(seckillService.getPageCount());
    }

    @Test
    public void getSeckillList() throws Exception {

        List<Seckill> list = seckillService.getSeckillList(1);
        logger.info(list.toString());
        System.out.println(list.toString());
    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getById(seckillId);
        System.out.println(seckill.toString());
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long seckillId = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        System.out.println(exposer.toString());
    }

    @Test
    public void executeSeckill() throws Exception {

        long seckillId = 1000L;
        long userPhone = 125906441181L;
        String md5 = "ab977232c7bfb60cf33df852e171edd9";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result={}", seckillExecution);
        } catch (SeckillCloseException e) {

            logger.error(e.getMessage());
        } catch (RepeaKillException e) {

            logger.error(e.getMessage());
        } catch (SeckillException e) {

            logger.error(e.getMessage());
        }
    }

}