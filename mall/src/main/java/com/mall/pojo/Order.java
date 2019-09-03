package com.mall.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单实体类
 */
public class Order implements Serializable {
    /**
     * 主键id，自增
     */
    private Integer id;

    /**
     * 订单编号
     */
    private Long orderCode;

    /**
     * 会员id
     */
    private Integer userId;

    /**
     * 商品项数量
     */
    private Integer goodsCount;

    /**
     * 订单金额
     */
    private BigDecimal orderMoney;

    /**
     * 折扣金额(积分抵扣)
     */
    private BigDecimal discountMoney;

    /**
     * 实付金额
     */
    private BigDecimal paymentMoney;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 下单所获积分
     */
    private Integer orderPoint;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 订单修改时间
     */
    private Date modifyTime;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String phone;

    /**
     * 订单状态名称
     */
    private String stateName;

    /**
     * 用户账号
     */
    private String account;


    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(BigDecimal discountMoney) {
        this.discountMoney = discountMoney;
    }

    public BigDecimal getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(BigDecimal paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderPoint() {
        return orderPoint;
    }

    public void setOrderPoint(Integer orderPoint) {
        this.orderPoint = orderPoint;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}