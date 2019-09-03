package com.mall.pojo;

import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员日志类
 */
public class UserLog {

    /**
     * 主键ID，自增
     */
    private Integer id;

    /**
     * 会员账户
     */
    private String account;

    /**
     * 日志内容
     */
    private String content;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}