package com.mall.pojo;

import java.math.BigDecimal;
import java.util.Date;
/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单报表实体类
 */
public class OrderReport {

    /**
     * 主键，自增id
     */
    private Integer id;

    /**
     * 每日订单数
     */
    private Integer orderCount;

    /**
     * 每日营业额
     */
    private BigDecimal orderMoney;

    /**
     * 日期
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}