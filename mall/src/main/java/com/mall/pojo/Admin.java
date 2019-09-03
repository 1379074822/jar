package com.mall.pojo;

import java.io.Serializable;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 管理员实体类
 */
public class Admin implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 管理员账户
     */
    private String account;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员姓名
     */
    private String adminName;

    /**
     * 管理员权限id
     */
    private Integer authorityId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }
}