package com.mall.mapper;

import com.mall.pojo.OrderLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单日志接口
 */
@Repository
public interface OrderLogMapper {

    /**
     * 添加订单操作操作
     * @param orderLog 订单日志实体类
     * @return
     */
    int insert(OrderLog orderLog);

    /**
     * 分页获得订单日志列表
     * @param currentPage  当前页面
     * @param pageSize  页面大小
     * @return
     * @throws Exception
     */
    List<OrderLog> getLogList(@Param("orderCode") Long orderCode, @Param("createTime") String createTime,
                              @Param("from") Integer currentPage,@Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 获得订单日志数量
     * @return
     * @throws Exception
     */
    int getLogCounts(@Param("orderCode") Long orderCode, @Param("createTime") String createTime)throws Exception;

    /**
     * 删除日志
     * @param id
     * @return
     */
    int deleteLog(@Param("id") Integer id);

}