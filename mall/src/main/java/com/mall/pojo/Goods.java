package com.mall.pojo;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品表
 */
@Document(indexName = Goods.INDEX,type = Goods.TYPE, shards = 1, replicas = 0)
public class Goods implements Serializable {

    /**
     * 建立索引
     */
    public static final String INDEX = "goods_index";

    /**
     * 类型
     */
    public static final String TYPE =  "goods_type";

    /**
     * 主键ID，自增
     */
    private Integer id;

    /**
     * 商品名称，通过ik分词器进行分词
     */
    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    private String goodsName;

    /**
     * 商品货号
     */
    private String goodsCore;

    /**
     * 商品描述，通过ik分词器进行分词
     */
    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    private String goodsDescription;

    /**
     * 一级分类
     */
    private Short oneCategoryId;

    /**
     * 二级分类
     */
    private Short twoCategoryId;

    /**
     * 图片url地址
     */
    private String logoPicPath;

    /**
     * 图片的服务器存储路径
     */
    private String logoLocPath;

    /**
     * 商品描述，通过ik 分词器进行分词
     */
    private BigDecimal cost;   //商品成本

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 上、下架状态id
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 上架、下架
     */
    private String stateName;

    /**
     * 所属一级分类名称
     */
    private String oneCategoryName;

    /**
     * 所属二级分类名称
     */
    private String twoCategoryName;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 库存
     */
    private Integer stock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCore() {
        return goodsCore;
    }

    public void setGoodsCore(String goodsCore) {
        this.goodsCore = goodsCore;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public Short getOneCategoryId() {
        return oneCategoryId;
    }

    public void setOneCategoryId(Short oneCategoryId) {
        this.oneCategoryId = oneCategoryId;
    }

    public Short getTwoCategoryId() {
        return twoCategoryId;
    }

    public void setTwoCategoryId(Short twoCategoryId) {
        this.twoCategoryId = twoCategoryId;
    }

    public String getLogoPicPath() {
        return logoPicPath;
    }

    public void setLogoPicPath(String logoPicPath) {
        this.logoPicPath = logoPicPath;
    }

    public String getLogoLocPath() {
        return logoLocPath;
    }

    public void setLogoLocPath(String logoLocPath) {
        this.logoLocPath = logoLocPath;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getOneCategoryName() {
        return oneCategoryName;
    }

    public void setOneCategoryName(String oneCategoryName) {
        this.oneCategoryName = oneCategoryName;
    }

    public String getTwoCategoryName() {
        return twoCategoryName;
    }

    public void setTwoCategoryName(String twoCategoryName) {
        this.twoCategoryName = twoCategoryName;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}