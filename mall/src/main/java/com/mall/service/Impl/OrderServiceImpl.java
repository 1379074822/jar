package com.mall.service.Impl;

import com.mall.mapper.*;
import com.mall.pojo.*;
import com.mall.service.OrderService;
import com.mall.service.UserService;
import com.mall.tools.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    /**
     * 订单mapper
     */
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 订单明细mapper
     */
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    /**
     * 商品库存mapper
     */
    @Autowired
    private GoodsStockMapper goodsStockMapper;
    /**
     * 会员mapper
     */
    @Autowired
    private UserMapper userMapper;
   /**
    * 会员接口
    */
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private UserPointMapper userPointMapper;
    @Autowired
    private ShopCarMapper shopCarMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Autowired
    private OrderReportMapper orderReportMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 下单操作(多表操作)，启动事务
     * @param order 订单
     * @param orderDetailList 订单明细列表
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer insertOrder(Order order, List<OrderDetail> orderDetailList,
                               String money,String discount,Integer userId,String ids)throws Exception{
        if(orderMapper.insert(order) <= 0){
            throw new RuntimeException("插入订单错误");
        }
        //下单成功后，删除相应购物车信息
        shopCarMapper.deleteById(ids);
        Order order1 = orderMapper.getByCode(order.getOrderCode());
        for(OrderDetail od:orderDetailList){
            od.setOrderId(order.getOrderCode());
            if(orderDetailMapper.insert(od)<=0){
                throw new RuntimeException("插入订单明细错误");
            }
            //实时获得商品库存,与购买数量比较
            if(goodsStockMapper.updateByOrder(od.getGoodsNum(),od.getGoodsId())<=0){
                throw new RuntimeException("修改商品库存错误");
            }
        }
        //设置订单支付时限为1分钟，用mq处理已超时的订单，一旦过期，将订单设置为已取消
        rabbitTemplate.convertAndSend("order.delay_exchange","order.delay_key",order, message -> {
            message.getMessageProperties().setExpiration(Integer.toString(20*1000));
            return message;
        });
        return order1.getId();
    }

    @Override
    public Order getById(Integer id) {
        return orderMapper.getById(id);
    }

    @Override
    public Order getByCode(Long orderCode) {
        return orderMapper.getByCode(orderCode);
    }


    @Override
    public List<Order> getOrderList(Integer state,Integer userId) throws Exception {
        return orderMapper.getOrderList(state,userId);
    }

    @Override
    public List<OrderDetail> getDetailsList(Long orderCode) throws Exception {
        return orderDetailMapper.getDetailsList(orderCode);
    }

    @Override
    public String getStateName(Integer state) throws Exception {
        return orderMapper.getStateName(state);
    }

    @Override
    public boolean updateState(Order order) throws Exception {
        boolean flag = false;
        if(orderMapper.updateById(order)>0){
            flag = true;
        }
        return flag;
    }

    /**
     * 支付成功后,会员积分变更以及增加积分变更记录
     * @param order 订单实体
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean update(Order order) throws Exception {
        boolean flag = false;
        Order o = orderMapper.getById(order.getId());
        User user = userMapper.selectById(o.getUserId());
        Integer pointProportion = activityMapper.getActivity().getActivityRule(); //获得积分兑换比例
        Integer discount = (o.getDiscountMoney().intValue()/pointProportion)*1000;   //折扣的积分
        Integer point = user.getPoint()-discount+o.getPaymentMoney().intValue();
        //会员变更日志记录(若使用折扣则有两个记录)
        if(o.getDiscountMoney().intValue() != 0){
            UserPoint userPoint1 = new UserPoint();
            userPoint1.setUserId(o.getUserId());
            userPoint1.setSource(4);
            userPoint1.setChangePoint("使用积分抵扣金额,积分-"+discount);
            userPoint1.setCurrentPoint(user.getPoint()-discount);
            userPointMapper.insert(userPoint1);
        }
        Thread.sleep(1000);
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(o.getUserId());
        userPoint.setSource(2);
        userPoint.setChangePoint("下订单获得积分,积分+"+o.getPaymentMoney().intValue());
        userPoint.setCurrentPoint(point);
        userPointMapper.insert(userPoint);
        User u = new User();
        u.setPoint(point);
        u.setId(o.getUserId());
        userService.updateUser(u);
        if(orderMapper.updateById(order)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Order> getOrderListByParam(Long orderCode, String receiverName, Integer state,
                                           String createTime, Integer currentPageNo, Integer pageSize)throws Exception{
        return orderMapper.getOrderListByParam(orderCode,receiverName,state,createTime,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public int getOrderCountByParam(Long orderCode, String receiverName, Integer state,
                                    String createTime, Integer currentPageNo, Integer pageSize) throws Exception {
        return orderMapper.getOrderCountByParam(orderCode,receiverName,state,createTime,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteOrder(Integer id) throws Exception {
        boolean flag = false;
        Order order = orderMapper.getById(id);
        if(orderMapper.deleteById(id)>0){
            orderDetailMapper.deleteByOrder(order.getOrderCode());
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean addLog(OrderLog orderLog) throws Exception {
        boolean flag = false;
        if(orderLogMapper.insert(orderLog)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public int getLogCounts(Long orderCode, String createTime) throws Exception {
        return orderLogMapper.getLogCounts(orderCode,createTime);
    }

    @Override
    public boolean deleteLog(Integer id) throws Exception {
        boolean flag = false;
        if(orderLogMapper.deleteLog(id)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean insertReport(OrderReport orderReport){
        boolean flag = false;
        if(orderReportMapper.insert(orderReport)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public int getDayCount(String createTime){
        return orderMapper.getDayCount(createTime);
    }

    @Override
    public BigDecimal getDayMoney(String createTime){
        return orderMapper.getDayMoney(createTime);
    }

    @Override
    public Map<String,Object> getReportList() {
        //获取所有订单报表列表
        List<OrderReport> orderReportList = orderReportMapper.getReportList();
        Map<String, Object> map = new HashMap<>();
        int[] count = new int[orderReportList.size()];  //订单数量数组
        String[] money = new String[orderReportList.size()]; //订单营业额数组
        String[] time = new String[orderReportList.size()]; //时间数组
        int i = 0;
        for(OrderReport orderReport:orderReportList){
            count[i] = orderReport.getOrderCount();
            money[i] = orderReport.getOrderMoney().toString();
            time[i] = DateUtils.format(orderReport.getCreateTime(),"yyyy-MM-dd");
            i++;
        }
        map.put("count",count);
        map.put("money",money);
        map.put("time",time);
        return map;
    }

    @Override
    public List<OrderLog> getLogList(Long orderCode, String createTime,Integer currentPage, Integer pageSize) throws Exception {
        return orderLogMapper.getLogList(orderCode,createTime,(currentPage-1)*pageSize,pageSize);
    }
}
