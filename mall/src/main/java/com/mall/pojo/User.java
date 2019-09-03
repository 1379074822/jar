package com.mall.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员实体类
 */
public class User implements Serializable {
    /**
     * 主键ID，自增
     */
    private Integer id;

    /**
     * 账户名
     */
    private String account;

    /**
     * 会员昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 会员电话
     */
    private String phone;

    /**
     * 会员邮箱
     */
    private String email;

    /**
     * 会员积分
     */
    private Integer point;

    /**
     * 会员状态
     */
    private Integer state;

    /**
     * 会员创建时间
     */
    private Date createTime;

    /**
     * 会员修改时间
     */
    private Date modifyTime;

    /**
     * 可用、黑名单
     */
    private String stateName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "user [id=" + id + ", account=" + account + ", email=" + email + ", phone=" + phone + "]";
    }
}