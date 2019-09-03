package com.mall.mapper;

import com.mall.pojo.Admin;
import org.springframework.stereotype.Repository;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员实体类接口
 */
@Repository
public interface AdminMapper {

    /**
     * 通过管理员id获得管理员
     * @param id 管理员id
     * @return
     */
    Admin selectByPrimaryKey(Integer id);

    /**
     * 通过管理员账户获得管理员
     * @param account 账户
     * @return
     */
    Admin selectByAccount(String account);
}