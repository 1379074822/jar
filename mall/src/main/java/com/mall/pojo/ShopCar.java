package com.mall.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 购物车实体类
 */
public class ShopCar{

    /**
     * 主键ID，自增
     */
    private Integer id;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 会员ID
     */
    private Integer userId;

    /**
     *  该商品数量
     */
    private Integer goodsNum;

    /**
     * 总价格
     */
    private BigDecimal price;

    /**
     * 加入购物车时间
     */
    private Date createTime;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品单价
     */
    private String soloPrice;

    /**
     * 商品图片url地址
     */
    private String logoPicPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSoloPrice() {
        return soloPrice;
    }

    public void setSoloPrice(String soloPrice) {
        this.soloPrice = soloPrice;
    }

    public String getLogoPicPath() {
        return logoPicPath;
    }

    public void setLogoPicPath(String logoPicPath) {
        this.logoPicPath = logoPicPath;
    }
}