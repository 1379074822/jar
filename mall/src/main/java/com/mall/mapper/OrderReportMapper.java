package com.mall.mapper;

import com.mall.pojo.OrderReport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单报表接口
 */
@Repository
public interface OrderReportMapper {

    /**
     * 添加订单每日报表
     * @param record
     * @return
     */
    int insert(OrderReport record);

    /**
     * 获取所有报表记录
     * @return
     */
    List<OrderReport> getReportList();
}