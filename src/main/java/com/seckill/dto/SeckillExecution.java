package com.seckill.dto;

import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStateEnum;

public class SeckillExecution {
    public SeckillExecution() {
        //Auto-generated constructor stub
    }

    // 商品Id
    private long seckillId;

    // 秒杀结果的状态
    private int state;

    /* 状态的明文标示 */
    private String stateInfo;

    /* 当秒杀成功时，需要传递秒杀结果的对象回去 */
    private SuccessKilled successKilled;

    /* 秒杀成功返回的实体 */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        super();
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    /* 秒杀失败时返回的实体，没有秒杀结果的对象 */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        super();
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    @Override
    public String toString() {
        return "SeckillException [seckillId=" + seckillId + ", state=" + state + ", stateIofo=" + stateInfo
                + ", successKilled=" + successKilled + "]";
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
