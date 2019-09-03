package com.mall.pojo;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 积分兑换比例实体类
 */
public class Activity {
    /**
     * 主键，自增id
     */
    private Integer id;

    /**
     * 积分兑换比例
     */
    private Integer activityRule;

    /**
     * 活动规则解释
     */
    private String activityBrief;

    /**
     * 设定人
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(Integer activityRule) {
        this.activityRule = activityRule;
    }

    public String getActivityBrief() {
        return activityBrief;
    }

    public void setActivityBrief(String activityBrief) {
        this.activityBrief = activityBrief;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}