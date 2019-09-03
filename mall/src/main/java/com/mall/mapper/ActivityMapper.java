package com.mall.mapper;

import com.mall.pojo.Activity;
import org.springframework.stereotype.Repository;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 积分兑换比例接口
 */
@Repository
public interface ActivityMapper {

    /**
     * 添加积分兑换比例
     * @param record
     * @return
     */
    int insert(Activity record);

    /**
     * 获得积分兑换比例
     * @param id
     * @return
     */
    Activity getById(Integer id);

    /**
     * 更新积分兑换比例
     * @param record
     * @return
     */
    int update(Activity record);

    /**
     * 获取列表
     * @return
     */
    Activity getActivity();
}