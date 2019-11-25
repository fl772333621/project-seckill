package com.mfanw.seckill.beans;

public class SecKillBean {

    /**
     * 抢购商品ID，请自行保证不重复
     */
    private Long id;

    /**
     * 抢购开始时间（毫秒），默认为0表示无抢购
     */
    private long startTime = 0;

    /**
     * 抢购商品总数目，默认为0表示无抢购
     */
    private int goodsCount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    @Override
    public String toString() {
        return "SecKillBean{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", goodsCount=" + goodsCount +
                '}';
    }
}
