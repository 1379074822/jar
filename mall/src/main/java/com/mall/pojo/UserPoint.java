package com.mall.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 用户积分变更实体类
 */
public class UserPoint implements Serializable {
    /**
     * 主键ID，自增
     */
    private Integer id;  //用户积分变更表id

    /**
     * 会员id
     */
    private Integer userId;

    /**
     * 积分来源id
     */
    private Integer source;

    /**
     * 会员积分变更(获得、扣除)情况
     */
    private String changePoint;

    /**
     * 会员当前积分
     */
    private Integer currentPoint;

    /**
     * 用户积分变更创建时间
     */
    private Date createTime;

    /**
     * 会员账户名
     */
    private String userAccount;

    /**
     * 会员积分来源(1.注册2.下单3.活动下单4.积分抵扣)
     */
    private String pointState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    public String getChangePoint() {
        return changePoint;
    }

    public void setChangePoint(String changePoint) {
        this.changePoint = changePoint;
    }

    public String getPointState() {
        return pointState;
    }

    public void setPointState(String pointState) {
        this.pointState = pointState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}