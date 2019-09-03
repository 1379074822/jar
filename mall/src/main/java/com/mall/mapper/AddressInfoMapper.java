package com.mall.mapper;

import com.mall.pojo.AddressInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23 
 *@Description: 地址访问接口
 */
@Repository
public interface AddressInfoMapper {

    /**
     * 添加用户地址
     * @param record
     * @return
     */
    int insert(AddressInfo record);

    /**
     * 根据id获得用户地址
     * @param id
     * @return
     */
    AddressInfo getAddress(@Param(value = "id") Integer id);

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    int deleteById(@Param(value = "id") Integer id)throws Exception;

    /**
     * 用户地址列表
     * @param userId
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<AddressInfo> getAddressList(@Param(value = "userId") Integer userId,
                                   @Param(value = "from") Integer currentPageNo,
                                   @Param(value = "pageSize") Integer pageSize)throws Exception;

    /**
     * 得到地址总数
     * @param userId
     * @return
     * @throws Exception
     */
    int getAddressCount(@Param(value = "userId") Integer userId)throws Exception;

    /**
     * 修改当前默认地址状态
     * @param id
     * @return
     * @throws Exception
     */
    int updateState(@Param(value = "id") Integer id)throws Exception;

    /**
     * 修改其他地址状态
     * @param id
     * @return
     * @throws Exception
     */
    int updateNotId(@Param(value = "id") Integer id)throws Exception;

    /**
     * 获得默认地址
     * @return
     * @throws Exception
     */
    AddressInfo getDefaultAddress()throws Exception;
}