package com.mall.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品分类表
 */
public class GoodsCategory implements Serializable {
    /**
     * 主键ID，自增
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父节点id
     */
    private Short parentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最新修改时间
     */
    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Short getParentId() {
        return parentId;
    }

    public void setParentId(Short parentId) {
        this.parentId = parentId;
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
}
