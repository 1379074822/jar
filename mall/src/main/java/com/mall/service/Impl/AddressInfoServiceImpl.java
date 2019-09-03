package com.mall.service.Impl;

import com.mall.mapper.AddressInfoMapper;
import com.mall.pojo.AddressInfo;
import com.mall.service.AddressInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressInfoServiceImpl implements AddressInfoService {
    /**
     * 会员收获地址mapper
     */
    @Autowired
    AddressInfoMapper addressInfoMapper;

    @Override
    public boolean addAddress(AddressInfo addressInfo) {
        boolean flag = false;
        if(addressInfoMapper.insert(addressInfo)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<AddressInfo> getAddressList(Integer userId, Integer currentPageNo, Integer pageSize) throws Exception {
        return addressInfoMapper.getAddressList(userId,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public int getAddressCount(Integer userId) throws Exception {
        return addressInfoMapper.getAddressCount(userId);
    }

    @Override
    public boolean deleteAddress(Integer id) throws Exception {
        boolean flag = false;
        if(addressInfoMapper.deleteById(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateState(Integer id) throws Exception {
        boolean flag = false;
        if(addressInfoMapper.updateState(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateNotId(Integer id) throws Exception {
        boolean flag = false;
        if(addressInfoMapper.updateNotId(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public AddressInfo getDefaultAddress() throws Exception {
        return addressInfoMapper.getDefaultAddress();
    }

    @Override
    public AddressInfo getAddress(Integer id) throws Exception {
        return addressInfoMapper.getAddress(id);
    }


}
