package com.mall.service;

import com.mall.pojo.AddressInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员地址服务接口
 */
public interface AddressInfoService {

    /**
     * 添加会员地址
     * @param addressInfo
     * @return
     */
    boolean addAddress(AddressInfo addressInfo);

    /**
     * 获取用户地址列表
     * @param userId
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<AddressInfo> getAddressList(Integer userId,Integer currentPageNo,Integer pageSize)throws Exception;

    /**
     * 获得当前会员收获地址数量
     * @param userId
     * @return
     * @throws Exception
     */
    public int getAddressCount(Integer userId)throws Exception;

    /**
     * 根据id删除地址
     * @param id
     * @return
     * @throws Exception
     */
    public boolean deleteAddress(Integer id)throws Exception;

    /**
     * 修改当前默认地址状态
     * @param id
     * @return
     * @throws Exception
     */
    public boolean updateState(Integer id)throws Exception;

    /**
     * 修改其他地址状态
     * @param id
     * @return
     * @throws Exception
     */
    public boolean updateNotId(Integer id)throws Exception;

    /**
     * 获得默认地址
     * @return
     * @throws Exception
     */
    AddressInfo getDefaultAddress()throws Exception;

    /**
     * 根据id获得地址
     * @param id
     * @return
     * @throws Exception
     */
    AddressInfo getAddress(Integer id)throws Exception;
}
