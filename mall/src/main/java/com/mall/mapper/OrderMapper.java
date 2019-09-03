package com.mall.mapper;

import com.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单接口
 */
@Repository
public interface OrderMapper {
    /**
     * 后台管理员根据订单id删除已取消订单
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 生成订单
     * @param record
     * @return
     */
    int insert(Order record);

    /**
     * 根据id获得订单
     * @param id  订单id
     * @return
     */
    Order getById(@Param("id") Integer id);

    /**
     * 更新订单
     * @param record
     * @return
     */
    int updateById(Order record);

    /**
     * 根据订单号获得订单
     * @param id  订单号
     * @return
     */
    Order getByCode(@Param("orderCode") Long id);

    /**
     * 前台会员查看自己全部订单
     * @param userId  会员id
     * @param state  订单状态
     * @return
     * @throws Exception
     */
    List<Order> getOrderList(@Param("state") Integer state,@Param("userId") Integer userId)throws Exception;

    /**
     * 获取当前订单状态名字
     * @param state  订单状态id
     * @return
     * @throws Exception
     */
    String getStateName(@Param("state") Integer state)throws Exception;

    /**
     * 后台管理员获得订单列表
     * @param orderCode  订单查询参数
     * @param receiverName  订单查询参数
     * @param state  订单查询参数
     * @param createTime  订单查询参数
     * @param currentPageNo  当前页面
     * @param pageSize  页面大小
     * @return  订单数量
     * @throws Exception
     */
    List<Order> getOrderListByParam(@Param("orderCode") Long orderCode,
                                    @Param("receiverName") String receiverName,
                                    @Param("state") Integer state,
                                    @Param("createTime") String createTime,
                                    @Param("from") Integer currentPageNo,
                                    @Param("pageSize") Integer pageSize)throws Exception;


    /**
     * 获得订单数量
     * @param orderCode  订单查询参数
     * @param receiverName  订单查询参数
     * @param state  订单查询参数
     * @param createTime  订单查询参数
     * @param currentPageNo  当前页面
     * @param pageSize  页面大小
     * @return  订单数量
     * @throws Exception
     */
    int  getOrderCountByParam(@Param("orderCode") Long orderCode,
                              @Param("receiverName") String receiverName,
                              @Param("state") Integer state,
                              @Param("createTime") String createTime,
                              @Param("from") Integer currentPageNo,
                              @Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 获取每日订单数
     * @param createTime 日期
     * @return
     * @throws Exception
     */
    int getDayCount(@Param("createTime") String createTime);

    /**
     * 获取每日营业额
     * @param createTime  日期
     * @return
     * @throws Exception
     */
    BigDecimal getDayMoney(@Param("createTime") String createTime);

}