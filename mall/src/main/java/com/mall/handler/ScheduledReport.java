package com.mall.handler;

import com.mall.pojo.OrderReport;
import com.mall.service.OrderService;
import com.mall.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 定时任务添加每日订单报表(统计订单数 销售额)
 */
@Service
public class ScheduledReport {

    /**
     * 订单接口
     */
    @Autowired
    private OrderService orderService;

    /**
     * 每天23:59定时添加订单报表记录
     */
    @Scheduled(cron = "0 18 22 ? * 1-7")
    public void insertReport(){
        //获取今天日期
        String time = DateUtils.format(new Date());
        //今日订单数
        Integer count = orderService.getDayCount(time);
        //今日订单营业额
        BigDecimal totalMoney = orderService.getDayMoney(time);
        OrderReport orderReport = new OrderReport();
        orderReport.setOrderCount(count);
        orderReport.setOrderMoney(totalMoney);
        orderService.insertReport(orderReport);  //添加订单报表
    }
}
