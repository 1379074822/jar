package com.mall.service.Impl;

import com.mall.mapper.AdminMapper;
import com.mall.pojo.Admin;
import com.mall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    /**
     * 管理员mapper
     */
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin getAdminByAccount(String account) {
        return adminMapper.selectByAccount(account);
    }
}
