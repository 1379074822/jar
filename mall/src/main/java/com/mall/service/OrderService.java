package com.mall.service;

import com.mall.pojo.Order;
import com.mall.pojo.OrderDetail;
import com.mall.pojo.OrderLog;
import com.mall.pojo.OrderReport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 订单服务接口
 */
public interface OrderService {


    /**
     * 生成订单,同时生成订单明细,然后更新库存表和删除购物车信息等
     * @param order   订单对象
     * @param orderDetailList  订单明细对象列表
     * @param money  总金额(同时也是所获积分)
     * @param discount  折扣
     * @param userId  会员id
     * @param ids  购物车id集合
     * @return
     * @throws Exception
     */
    Integer insertOrder(Order order, List<OrderDetail> orderDetailList,
                        String money,String discount,Integer userId,String ids)throws Exception;

    /**
     * 根据订单id获得订单
     * @param id  订单id
     * @return
     */
    Order getById(Integer id);

    /**
     * 根据订单编号获得订单
     * @param orderCode  订单编号
     * @return
     */
    Order getByCode(Long orderCode);

    /**
     * 获得会员订单列表
     * @param state   订单状态
     * @param userId  会员id
     * @return
     * @throws Exception
     */
    List<Order> getOrderList(Integer state,Integer userId)throws Exception;

    /**
     * 获得某订单的明细
     * @param orderCode  订单编号
     * @return
     * @throws Exception
     */
    List<OrderDetail> getDetailsList(Long orderCode)throws Exception;

    /**
     * 获取当前订单状态名
     * @param state  状态id
     * @return
     * @throws Exception
     */
    String getStateName(Integer state)throws Exception;


    /**
     * 更新订单状态
     * @param order
     * @return
     * @throws Exception
     */
    boolean updateState(Order order)throws Exception;

    /**
     * 更新订单
     * @param order 订单实体
     * @return
     * @throws Exception
     */
    boolean update(Order order)throws Exception;
    /**
     * 后台管理员获得订单列表
     * @param orderCode  订单查询参数
     * @param receiverName  订单查询参数
     * @param state   订单查询参数
     * @param createTime  订单查询参数
     * @param currentPageNo   当前页面
     * @param pageSize   页面大小
     * @return  订单数量
     * @throws Exception
     */
    List<Order> getOrderListByParam(Long orderCode, String receiverName, Integer state,
                                    String createTime, Integer currentPageNo, Integer pageSize)throws Exception;
    /**
     * 后台管理员获得订单数量
     * @param orderCode  订单查询参数
     * @param receiverName  订单查询参数
     * @param state   订单查询参数
     * @param createTime  订单查询参数
     * @param currentPageNo   当前页面
     * @param pageSize   页面大小
     * @return  订单数量
     * @throws Exception
     */
    int getOrderCountByParam(Long orderCode, String receiverName, Integer state,
                             String createTime, Integer currentPageNo, Integer pageSize)throws Exception;

    /**
     * 后台管理员删除已取消订单
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteOrder(Integer id)throws Exception;

    /**
     * 添加订单状态修改记录日志
     * @param orderLog  订单日志实体类
     * @return
     * @throws Exception
     */
    boolean addLog(OrderLog orderLog)throws Exception;

    /**
     * 获取订单日志列表
     * @param orderCode  订单编号
     * @param createTime  创建时间
     * @param currentPage  当前页码
     * @param pageSize  当前大小
     * @return
     * @throws Exception
     */
    List<OrderLog> getLogList(Long orderCode, String createTime,
                              Integer currentPage,Integer pageSize)throws Exception;

    /**
     * 获得订单日志数量
     * @param orderCode  订单编号
     * @param createTime  创建时间
     * @return
     * @throws Exception
     */
    int getLogCounts(Long orderCode, String createTime)throws Exception;

    /**
     * 删除订单日志操作记录
     * @param id  订单日志id
     * @return
     * @throws Exception
     */
    boolean deleteLog(Integer id)throws Exception;

    /** 添加订单报表类
     * @param orderReport  报表实体类
     * @return
     * @throws Exception
     */
    boolean insertReport(OrderReport orderReport);

    /**
     * 获取每日订单数
     * @param createTime 每日时间
     * @return
     * @throws Exception
     */
    int getDayCount(String createTime);

    /**
     * 获取每日营业额
     * @param createTime  每日时间
     * @return
     * @throws Exception
     */
    BigDecimal getDayMoney(String createTime);

    /**
     * 获取订单报表数据
     * @return
     */
    Map<String,Object> getReportList();

}
