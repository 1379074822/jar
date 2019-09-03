package com.mall.mapper;

import com.mall.pojo.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单明细接口
 */
@Repository
public interface OrderDetailMapper {
    /**
     * 根据订单编号删除订单明细
     * @param id
     * @return
     */
    int deleteByOrder(Long id);

    /**
     * 生产订单后，同时添加订单明细
     * @param record
     * @return
     */
    int insert(OrderDetail record);

    /**
     * 根据id获得订单明细
     * @param id
     * @return
     */
    OrderDetail getById(Integer id);


    /**
     * 根据订单编号获得订单明细
     * @param orderCode  订单编号
     * @return
     * @throws Exception
     */
    List<OrderDetail> getDetailsList(@Param("orderCode") Long orderCode)throws Exception;
}