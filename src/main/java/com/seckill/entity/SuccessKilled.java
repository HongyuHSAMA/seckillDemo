package com.seckill.entity;

import java.util.Date;

public class SuccessKilled {
    public SuccessKilled() {
        // Auto-generated constructor stub
    }

    private long seckillId;//秒杀商品ID
    private long userPhone;//参与秒杀的用户手机号码
    private short state;//秒杀的状态
    private Date createTime;//秒杀的时间

    //多对一
    private Seckill seckill;

    public Seckill getSeckill() {
        return seckill;
    }
    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }
    public long getSeckillId() {
        return seckillId;
    }
    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }
    public long getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }
    public short getState() {
        return state;
    }
    public void setState(short state) {
        this.state = state;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Override
    public String toString() {
        return "SuccessKilled [seckillId=" + seckillId + ", userPhone=" + userPhone + ", state=" + state
                + ", createTime=" + createTime + "]";
    }
}
