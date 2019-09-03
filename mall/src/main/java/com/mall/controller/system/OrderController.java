package com.mall.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.mall.pojo.*;
import com.mall.service.DataDictionaryService;
import com.mall.service.OrderService;
import com.mall.tools.Constants;
import com.mall.tools.PageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 后台订单管理控制器
 */
@Controller
@RequestMapping("/system/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(com.mall.controller.system.OrderController.class);
    /**
     * 订单接口
     */
    @Autowired
    private OrderService orderService;
    /**
     * 数据字典接口
     */
    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 管理员筛选搜索订单
     * @param model model
     * @param queryOrderCode 订单编号
     * @param queryReceiverName 收货人姓名
     * @param queryCreateTime 创建时间
     * @param queryState 订单状态
     * @param pageIndex 当前页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String getOrderList(Model model,@RequestParam(value = "orderCode",required = false) String queryOrderCode,
                               @RequestParam(value = "receiverName",required = false) String queryReceiverName,
                               @RequestParam(value = "createTime",required = false) String queryCreateTime,
                               @RequestParam(value = "state",required = false) String queryState,
                               @RequestParam(value = "pageIndex", required = false) String pageIndex)throws Exception{
        logger.info("订单参数信息及页面:"+queryOrderCode+queryReceiverName+queryState+queryCreateTime+pageIndex);
        List<Order> orderList = new ArrayList<>();
        List<DataDictionary> stateList = new ArrayList<>(); //订单状态列表
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        Integer state = null;
        if (queryState != null && !queryState.equals("")) {
            state = Integer.valueOf(queryState);
        }
        Long orderCode = null;
        if (queryOrderCode != null && !queryOrderCode.equals("")) {
            orderCode = Long.valueOf(queryOrderCode);
        }
        //总数量表
        int totalCount = orderService.getOrderCountByParam(orderCode,queryReceiverName,state,queryCreateTime,currentPageNo,pageSize);
        logger.info("总数量:" + totalCount);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        logger.info("当前页码：" + currentPageNo + "每页条数:" + pageSize);
        try{
            orderList = orderService.getOrderListByParam(orderCode,queryReceiverName,state,queryCreateTime,currentPageNo,pageSize);
            stateList = dataDictionaryService.getDataDictionaryList("ORDER_STATE");
            logger.info("订单列表和状态列表大小:"+orderList.size()+stateList.size());
        }catch (Exception e){
            logger.error("查询订单列表出错:",e);
        }
        model.addAttribute("orderList", orderList);
        model.addAttribute("orderCode",orderCode);
        model.addAttribute("state",state);
        model.addAttribute("createTime",queryCreateTime);
        model.addAttribute("receiverName",queryReceiverName);
        model.addAttribute("stateList", stateList);
        model.addAttribute("pages", pages);
        return "system/order/list";
    }

    /**
     * 管理员删除已取消的订单
     * @param id  订单id
     * @return
     */
    @RequestMapping("/deleteOrder")
    @ResponseBody
    public String delOrder(@RequestParam String id,HttpSession session)throws Exception{
        logger.info("要删除的订单id是:"+id);
        HashMap<String, String> map = new HashMap<>();
        Order o = orderService.getById(Integer.parseInt(id));
        Admin admin = (Admin)session.getAttribute(Constants.ADMIN_SESSION);
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderCode(o.getOrderCode());
        orderLog.setContent("管理员"+admin.getAccount()+"删除了已取消的订单。");
        orderService.addLog(orderLog);
        try{
            orderService.deleteOrder(Integer.parseInt(id));
            map.put("result",Constants.RESULT_TRUE);
        }catch (Exception e){
            logger.error("删除订单失败：",e);
            map.put("result",Constants.RESULT_FALSE);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 进入订单发货页面
     * @param model model
     * @param id 订单id
     * @return
     */
    @RequestMapping("/deliver")
    public String deliver(Model model,@RequestParam String id){
        logger.info("进入收货地址页面id是"+id);
        Order o = orderService.getById(Integer.parseInt(id));
        try {
            model.addAttribute("order",o);
        } catch (Exception e) {
            logger.error("查看收货地址页面失败:",e);
        }
        return "system/order/deliver";
    }

    /**
     * 管理员发货
     * @param id 订单id
     * @return
     */
    @RequestMapping("/deliverSave")
    @ResponseBody
    public String deliverSave(@RequestParam String id, HttpSession session)throws Exception{
        Order o = orderService.getById(Integer.parseInt(id));
        HashMap<String, String> map = new HashMap<>();
        Admin admin = (Admin)session.getAttribute(Constants.ADMIN_SESSION);
        //记录管理员操作日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderCode(o.getOrderCode());
        orderLog.setContent("管理员"+admin.getAdminName()+"已发货,订单状态变更为:待收货!");
        orderService.addLog(orderLog);
        o.setState(3);
        if(orderService.updateState(o)){
            map.put("result",Constants.RESULT_TRUE);
        }else {
            logger.error("发货失败：");
            map.put("result",Constants.RESULT_FALSE);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 管理员查看订单详情
     * @param model model
     * @param id 订单id
     * @return
     */
    @RequestMapping("/view")
    public String viewOrder(Model model,@RequestParam String id){
        Order o = orderService.getById(Integer.parseInt(id));
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = orderService.getDetailsList(o.getOrderCode());
        } catch (Exception e) {
            logger.error("查看订单信息失败:",e);
        }
        model.addAttribute("orderDetailList",orderDetailList);
        model.addAttribute("order",o);
        return "system/order/view";
    }

    /**
     * 管理员查看订单操作日志记录
     * @param model  model
     * @param queryOrderCode  订单编号
     * @param queryCreateTime  订单创建时间
     * @param pageIndex  当前页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/logList")
    public String getOrderLogList(Model model,@RequestParam(value = "orderCode",required = false) String queryOrderCode,
                               @RequestParam(value = "createTime",required = false) String queryCreateTime,
                               @RequestParam(value = "pageIndex", required = false) String pageIndex)throws Exception{
        logger.info("订单参数信息及页面:"+queryOrderCode+queryCreateTime+pageIndex);
        List<OrderLog> orderLogList = new ArrayList<>();
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        Long orderCode = null;
        if (queryOrderCode != null && !queryOrderCode.equals("")) {
            orderCode = Long.valueOf(queryOrderCode);
        }
        //总数量表
        int totalCount = orderService.getLogCounts(orderCode,queryCreateTime);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        logger.info("当前页码：" + currentPageNo + "每页条数:" + pageSize);
        try{
            orderLogList = orderService.getLogList(orderCode,queryCreateTime,currentPageNo,pageSize);
        }catch (Exception e){
            logger.error("查询订单日志列表出错:",e);
        }
        model.addAttribute("orderLogList", orderLogList);
        model.addAttribute("orderCode",orderCode);
        model.addAttribute("createTime",queryCreateTime);
        model.addAttribute("pages", pages);
        return "system/order/logList";
    }


    /**
     * 管理员删除订单日志操作信息
     * @param id 订单id
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteLog")
    @ResponseBody
    public String deleteLog(@RequestParam String id)throws Exception{
        HashMap<String, String> map = new HashMap<>();
        if(orderService.deleteLog(Integer.parseInt(id))){
            map.put("result",Constants.RESULT_TRUE);
        }else {
            map.put("result",Constants.RESULT_ERROR);
        }
        return JSONArray.toJSONString(map);
    }
}
