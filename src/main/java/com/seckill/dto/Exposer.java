package com.seckill.dto;

public class Exposer {

    @Override
    public String toString() {
        return "Exposer [exposed=" + exposed + ", md5=" + md5 + ", seckillId=" + seckillId + ", now=" + now + ", start="
                + start + ", end=" + end + "]";
    }
    //无参构造函数
    public Exposer() {
        // Auto-generated constructor stub
    }
    //是否开启秒杀
    private boolean exposed;
    // 一种加密措施
    private String md5;
    //秒杀商品id
    private long seckillId;
    // 系统当前时间
    private long now;
    //开启时间
    private long start;
    //结束时间
    private long end;

    /**
     * 当秒杀时间不符合时，返回的数据
     * @param exposed   false状态
     * @param seckillId 商品ID
     * @param now   当前系统时间（服务器时间）
     * @param start 秒杀开启时间
     * @param end   秒杀结束时间
     */
    public Exposer(boolean exposed, Long seckillId,long now, long start, long end) {
        super();
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    /**
     * 该商品符合秒杀时返回的数据
     * @param exposed   状态
     * @param md5   一种盐值加密数据
     * @param seckillId 秒杀商品id
     */
    public Exposer(boolean exposed, String md5, long seckillId) {
        super();
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    /**
     * 秒杀商品不存在时返回的数据
     * @param exposed
     * @param seckillId
     */
    public Exposer(boolean exposed, long seckillId) {
        super();
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
