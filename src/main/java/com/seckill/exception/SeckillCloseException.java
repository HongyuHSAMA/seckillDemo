package com.seckill.exception;

/**
 * 秒杀已经关闭异常，当秒杀结束就会出现这个异常
 * @author hyh47
 *
 */
public class SeckillCloseException extends SeckillException{

    public SeckillCloseException() {

    }
    public SeckillCloseException(String message){
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause){
        super(message, cause);
    }
}
