package com.mall.service;

import com.mall.pojo.Admin;
import org.springframework.stereotype.Service;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 管理员服务接口
 */
@Service
public interface AdminService {

    /**
     * 根据id获得账户
     * @param account
     * @return
     */
    Admin getAdminByAccount(String account);

}
