package com.seckill.exception;

/**
 * 重复秒杀异常，不需要我们手动去try catch
 * @author hyh47
 *
 */
public class RepeaKillException extends SeckillException{

    public RepeaKillException() {

    }
    public RepeaKillException(String message){
        super(message);
    }

    public RepeaKillException(String message,Throwable cause){
        super(message, cause);
    }

}
