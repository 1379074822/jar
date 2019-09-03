package com.mall.handler;

import com.mall.mapper.GoodsStockMapper;
import com.mall.mapper.OrderMapper;
import com.mall.pojo.Order;
import com.mall.pojo.OrderDetail;
import com.mall.pojo.OrderLog;
import com.mall.service.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 死信接收队列(消费队列)的监听器
 */
@Component
public class DelayOrderReceiver {
    private Logger logger = LoggerFactory.getLogger(DelayOrderReceiver.class);
    /**
     * 订单接口
     */
    @Autowired
    private OrderService orderService;
    /**
     * 商品库存mapper
     */
    @Autowired
    private GoodsStockMapper goodsStockMapper;
    /**
     * 订单mapper
     */
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 监听消息队列
     * @param order
     * @param channel
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = "order.receive_queue",containerFactory = "rabbitListenerContainerFactory")
    public void consumerMessage(Order order, Channel channel, Message message)throws Exception {
        logger.info("接收队列！！！状态为:");
        Order o = orderMapper.getByCode(order.getOrderCode());
        try{
            //若状态为未付款，则更改状态且库存回滚，否则不处理
            if(o.getState() == 1){
                o.setState(6);
                logger.info("mq更改状态啦！！！");
                orderService.updateState(o);
                //订单日志记录信息
                OrderLog orderLog = new OrderLog();
                orderLog.setOrderCode(order.getOrderCode());
                orderLog.setContent("订单超时未支付，已自动取消!订单状态为:已取消！");
                orderService.addLog(orderLog);
                List<OrderDetail> orderDetailList = orderService.getDetailsList(order.getOrderCode());
                for(OrderDetail orderDetail:orderDetailList){
                    goodsStockMapper.stockRollback(orderDetail.getGoodsNum(),orderDetail.getGoodsId());
                }
            }
        }catch (Exception e){
            //这次消息，我已经接受并消费掉了，不会再重复发送消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            throw new Exception("mq抛出异常啦！！！",e);
        }
    }
}
