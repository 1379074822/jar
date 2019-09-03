package com.mall.controller.mall;

import com.alibaba.fastjson.JSONArray;
import com.mall.pojo.*;
import com.mall.service.*;
import com.mall.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 前台用户、商品、订单等控制器
 */
@Controller
@RequestMapping("/mall")
public class MallController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * 会员接口
     */
    @Autowired
    private UserService userService;
    /**
     * 收货地址接口
     */
    @Autowired
    private AddressInfoService addressInfoService;
    /**
     * 商品接口
     */
    @Autowired
    private GoodsService goodsService;
    /**
     * 商品分类接口
     */
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    /**
     * 购物车接口
     */
    @Autowired
    private ShopCarService shopCarService;
    /**
     * 订单接口
     */
    @Autowired
    private OrderService orderService;

    /**
     * 进入前台主页 model
     * @param model model
     * @param session session
     * @return
     * @throws Exception
     */
    @RequestMapping("/index")
    public String index(Model model,HttpSession session)throws Exception{
        List<GoodsCategory> oneCategoryList = null; //列出一级分类列表
        int totalCount = 0;
        List<Goods> goodsList = goodsService.getAllGoodsByLimit(); //获得主页部分显示商品
        List<Goods> hotGoodsList = goodsService.getHotGoods();  //热门商品列表
        List<Goods> newGoodsList = goodsService.getNewGoods();  //最新商品列表
        oneCategoryList = goodsCategoryService.getCategoryListByParentId(null);
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        if(user != null){
            totalCount = shopCarService.getTotalCount(user.getId());
            logger.info("商品总数量:"+totalCount);
            session.setAttribute("totalCount",totalCount);
        }
        model.addAttribute("hotGoodsList",hotGoodsList);
        model.addAttribute("newGoodsList",newGoodsList);
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("oneCategoryList",oneCategoryList);
        return "shop/index";
    }

    /**
     * 进入前台用户注册页面
     * @return
     */
    @RequestMapping("/reg")
    public String mallRegister(){ return "shop/reg";}

    /**
     * 进入前台会员登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){return "shop/login";}

    /**
     * 进入用户中心查看
     * @param session session
     * @param model model
     * @return
     */
    @RequestMapping("/userCenter")
    public String userCenter(HttpSession session, Model model)throws Exception{
        logger.info("用户信息查看中!");
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        if(u == null){
            return "redirect:/mall/login";
        }
        User user = userService.getUserById(u.getId());
        model.addAttribute("user",user);
        return "shop/user";
    }

    /**
     *进入修改密码页面
     * @return
     */
    @RequestMapping("/modifyPwd")
    public String modifyPwd(){return "shop/modifypwd";}

    /**
     * 显示当前用户所有地址
     * @param model model
     * @param session session
     * @param pageIndex 当前页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/address")
    public String userAddress(Model model, HttpSession session,@RequestParam(value = "pageIndex",required = false) String pageIndex)throws Exception{
        System.out.println("当前页面:"+pageIndex);
        Integer userId = ((User)session.getAttribute(Constants.USER_SESSION)).getId();
        List<AddressInfo> addressInfoList = null;
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        //总数量(表)
        int totalCount = addressInfoService.getAddressCount(userId);
        logger.info("总数量:"+totalCount);
        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        logger.info("currentPageNo：" + currentPageNo + "pageSize:"+pageSize);
        addressInfoList = addressInfoService.getAddressList(userId,currentPageNo,pageSize);
        model.addAttribute("pages",pages);
        model.addAttribute("addressInfoList",addressInfoList);
        return "shop/address";
    }

    /**
     * 实现数据库数据导入到elasticsearch中
     * @return
     */
    @RequestMapping("/ImportEs")
    public void importToEs(){
        List<Goods> goodsList = goodsService.getGoodsList();
        goodsService.saveGoods(goodsList);
    }
    /**
     * 全文搜索商品
     * @param keyword  关键字
     * @param model model
     * @return
     */
    @RequestMapping("/goodsQuery")
    public String goodsQuery(@RequestParam(name = "keyword", required = false) String keyword,
                             Model model){
        List<Goods> goodsList = goodsService.searchGoods(keyword);
        model.addAttribute("goodsList",goodsList);
        return "shop/search_list";
    }


    /**
     * 进入商品详细页面
     * @param id 商品id
     * @param model model
     * @return
     */
    @RequestMapping("/goodsDetail")
    public String goodsDetail(@RequestParam String id,
                              @RequestParam(value = "pageIndex",required = false) String pageIndex,
                              Model model,HttpSession session)throws Exception{
        int pageSize = Constants.pageSizeGoods;  //页面大小
        Integer currentPageNo = 1;  //当前页码
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        int totalCounts = 0;  //总数量表
        totalCounts = goodsService.getCountById(Integer.parseInt(id));
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCounts);
        int totalPageCount = pages.getTotalPageCount();  //总页数
        //控制首页和尾页
        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        List<GoodsComment> goodsCommentList = goodsService.getListById(Integer.parseInt(id),currentPageNo,pageSize);
        Goods goods = goodsService.getGoodsById(Integer.parseInt(id));
        Integer sale = goodsService.getGoodsSale(Integer.parseInt(id));
        Integer stock = goodsService.getGoodsStock(Integer.parseInt(id));
        User u =(User)session.getAttribute(Constants.USER_SESSION);
        if(session.getAttribute(Constants.USER_SESSION) != null){
            Integer totalCount = shopCarService.getTotalCount(u.getId());
            session.setAttribute("totalCount",totalCount);
        }
        List<GoodsCategory> oneCategoryList = this.getGoodsCategoryList(null);
        model.addAttribute("id",id);
        model.addAttribute("goods",goods);
        model.addAttribute("sale",sale);
        model.addAttribute("stock",stock);
        model.addAttribute("pages",pages);
        model.addAttribute("totalCounts",totalCounts);
        model.addAttribute("oneCategoryList",oneCategoryList);
        model.addAttribute("goodsCommentList",goodsCommentList);
        return "shop/goods_detail";
    }

    /**
     * 前台显示商品列表
     * @param model model
     * @param _oneCategoryId 一级分类id
     * @param _twoCategoryId 二级分类id
     * @param pageIndex 当前页面
     * @return
     */
    @RequestMapping("/goodsList")
    public String goodsList(Model model,@RequestParam(value = "goodsParam",required = false) String goodsParam,
                            @RequestParam(value = "oneCategoryId",required = false) String _oneCategoryId,
                            @RequestParam(value = "twoCategoryId",required = false) String _twoCategoryId,
                            @RequestParam(value = "pageIndex",required = false) String pageIndex)throws Exception{
        int pageSize = Constants.pageSizeIndex;  //页面大小
        Integer currentPageNo = 1;   //当前页码
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        Integer oneCategoryId = null;
        if(_oneCategoryId != null && !_oneCategoryId.equals("")){
            oneCategoryId = Integer.valueOf(_oneCategoryId);
        }
        Integer twoCategoryId = null;
        if(_twoCategoryId != null && !_twoCategoryId.equals("")){
            twoCategoryId = Integer.valueOf(_twoCategoryId);
        }
        int totalCount = goodsService.getGoodsCountByParam(null,null,1,oneCategoryId,twoCategoryId);
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);   //总页数
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        List<Goods> goodsList = goodsService.getGoodsListByParam(null,null,1,oneCategoryId,twoCategoryId, currentPageNo,pageSize);
        List<GoodsCategory> oneCategoryList = goodsCategoryService.getCategoryListByParentId(null); //列出一级分类列表,二级分类列表通过异步ajax获取
        if(oneCategoryId != null && !oneCategoryId.equals("")){
            List<GoodsCategory> twoCategoryList = this.getGoodsCategoryList(oneCategoryId.toString());
            model.addAttribute("twoCategoryList",twoCategoryList);
        }
        model.addAttribute("oneCategoryList",oneCategoryList);
        model.addAttribute("oneCategoryId",oneCategoryId);
        model.addAttribute("twoCategoryId",twoCategoryId);
        model.addAttribute("pages",pages);
        if(goodsParam != null && goodsParam != "" && goodsParam.equalsIgnoreCase("new")){
            List<Goods> newGoodsList  = goodsService.getNewGoods();
            model.addAttribute("goodsList",newGoodsList);
            model.addAttribute("goodsParam",goodsParam);
        }else if(goodsParam != null && goodsParam != "" && goodsParam.equalsIgnoreCase("hot")){
            List<Goods> hotGoodsList  = goodsService.getHotGoods();
            model.addAttribute("goodsList",hotGoodsList);
            model.addAttribute("goodsParam",goodsParam);
        }else {
            model.addAttribute("goodsList",goodsList);
            model.addAttribute("goodsParam",goodsParam);
        }
        return "shop/goods_list";
    }

    /**
     * 获取分类列表
     * @param pid 商品分类id
     * @return
     */
    public List<GoodsCategory> getGoodsCategoryList(String pid)throws Exception{
        List<GoodsCategory> goodsCategoryList = null;
        goodsCategoryList = goodsCategoryService.getCategoryListByParentId(pid == null ? null : Integer.parseInt(pid));
        return goodsCategoryList;
    }


    /**
     * 查看购物车表
     * @param session session
     * @param model model
     * @return
     */
    @RequestMapping("/viewShopCar")
    public String viewShopCar(@RequestParam(value = "goodsName",required = false) String goodsName,
                              @RequestParam(value = "stock",required = false) Integer stock,
                              HttpSession session,Model model)throws Exception{
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        List<ShopCar> shopCarList = null;
        int totalCount = 0;
        if(user == null){
            return "redirect:/mall/login";
        }
        shopCarList = shopCarService.getShopList(user.getId());
        totalCount = shopCarService.getTotalCount(user.getId());
        logger.info("总商品是:" + totalCount);
        session.setAttribute("totalCount",totalCount);
        model.addAttribute("shopCarList", shopCarList);
        model.addAttribute("stock", stock);
        model.addAttribute("goodsName",goodsName);
        return "shop/shop_car";
    }

    /**
     * 删除购物车信息
     * @param id 购物车id
     * @return
     */
    @RequestMapping("/delShopCar")
    public String deleteById(@RequestParam String id)throws Exception{
        if(shopCarService.deleteById(id)) {
            return "redirect:/mall/viewShopCar";
        }
        return "redirect:/mall/viewShopCar";
    }

    /**
     * 批量删除购物车信息
     * @param ids 购物车ids
     * @return
     */
    @RequestMapping("/delGoodsByIds")
    @ResponseBody
    public String delGoodsByIds(@RequestParam String ids)throws Exception{
        if(ids.contains(",")){
            ids = ids.substring(0,ids.length()-1);
        }
        HashMap<Object, Object> map = new HashMap<>();
        if(shopCarService.deleteById(ids)) {
            map.put("result",Constants.RESULT_TRUE);
        }else {
            map.put("result",Constants.RESULT_FALSE);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 更新购物车数量
     * @param num 数量
     * @param id 商品id
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateNum")
    @ResponseBody
    public String updateNum(@RequestParam String num,@RequestParam String id) throws Exception {
        logger.info("更新的商品数量和id是:"+num+"  "+id);
        HashMap<Object, Object> map = new HashMap<>();
        Integer goodsId = shopCarService.getById(Integer.parseInt(id)).getGoodsId();
        Goods goods = goodsService.getGoodsById(goodsId);
        if(num.equalsIgnoreCase("0")){
            map.put("result",Constants.RESULT_FALSE);
        }else if (Integer.parseInt(num) > goods.getStock()){
            map.put("result",Constants.RESULT_ERROR);
            map.put("number",goods.getStock());
        }else {
            BigDecimal number = BigDecimal.valueOf(Integer.parseInt(num));
            BigDecimal price = goods.getPrice().multiply(number);
            if(shopCarService.updateByGoodsId(Integer.parseInt(num),price,goods.getId())){
                map.put("result",Constants.RESULT_TRUE);
            }
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 商品加入购物车
     * @param session session
     * @param goodsId 商品id
     * @param num 商品数量
     * @return
     */
    @RequestMapping("/shopCar")
    public String myShopCar(HttpSession session,
                         @RequestParam(value = "goodsId",required = false) String goodsId,
                         @RequestParam(value = "num",required = false) String num) throws Exception {
        logger.info("商品id是: "+goodsId+" 数量是: "+num);
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        if(user == null){
            return "redirect:/mall/login";
        }
        //判断是查看购物车还是加入购物车
        //获取购物车列表
        ShopCar shopCar = shopCarService.getByGoodsId(Integer.parseInt(goodsId));
        Goods goods = goodsService.getGoodsById(Integer.parseInt(goodsId));  //获取商品单价
        //判断该商品是否已存在购物车,若存在，则更新数量和总价;若不存在则添加至购物车
        if (shopCar != null) {
            //获取新的商品数量和总价
            Integer num1 = shopCar.getGoodsNum() + Integer.parseInt(num);
            BigDecimal soloTotalPrice = goods.getPrice().multiply(BigDecimal.valueOf(num1));
            shopCarService.updateByGoodsId(num1, soloTotalPrice, goods.getId());
        } else {
            ShopCar car = new ShopCar();
            car.setUserId(user.getId());
            car.setGoodsId(Integer.parseInt(goodsId));
            car.setGoodsNum(Integer.parseInt(num));
            BigDecimal number = BigDecimal.valueOf(Integer.parseInt(num));
            car.setPrice(goods.getPrice().multiply(number));
            shopCarService.insert(car);
        }
        return "redirect:/mall/goodsDetail?id="+goodsId;
    }

    /**
     * 进入订单确认页面
     * @param model model
     * @param session session
     * @param count 数量
     * @param totalPrice 总价
     * @param ids 商品ids
     * @return
    */
    @RequestMapping(value = "/confirmOrder")
    public String confirmOrder(Model model,HttpSession session,@RequestParam(value = "count",required = false) String count,
                               @RequestParam(value = "total",required = false) String totalPrice,
                               @RequestParam(value = "ids",required = false) String ids)throws Exception{
        if(ids.contains(",")){
            ids = ids.substring(0,ids.length()-1);
        }
        logger.info("确定订单信息:"+count+" "+totalPrice+" "+ids);
        List<ShopCar> shopCarList = shopCarService.getListByIds(ids);
        for(ShopCar shopCar:shopCarList){
            Goods goods = goodsService.getGoodsById(shopCar.getGoodsId());
            //判断商品库存是否足够，不够返回购物车页面
            Integer stock = goodsService.getGoodsStock(shopCar.getGoodsId());
            if(stock<shopCar.getGoodsNum()){
                return "redirect:/mall/viewShopCar?goodsName="+ URLEncoder.encode(goods.getGoodsName(),"UTF-8")+"&&stock="+stock;
            }
            //若商品下架，则无法下单，返回购物车页面
            if(goods.getState() == 2){
                return "redirect:/mall/viewShopCar?goodsName="+ URLEncoder.encode(goods.getGoodsName(),"UTF-8");
            }
        }
        User u = userService.getUserById(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        Integer point = u.getPoint();
        List<AddressInfo> addressInfoList = addressInfoService.getAddressList(((User)session.getAttribute(Constants.USER_SESSION)).getId(),1,5);
        Integer pointProportion = userService.getActivity().getActivityRule();
        model.addAttribute("pointProportion",pointProportion);
        model.addAttribute("ids",ids);
        model.addAttribute("addressInfoList",addressInfoList);
        model.addAttribute("point",point);
        model.addAttribute("shopCarList",shopCarList);
        model.addAttribute("count",count);
        model.addAttribute("totalPrice",totalPrice);
        return "shop/confirm_order";
    }

    /**
     * 提交订单(后台数据库更新一系列表操作)
     * @param session session
     * @param aid  收货地址id
     * @param money  商品总价
     * @param discount 商品折扣
     * @param ids  购物车已选商品ids
     * @return
     */
    @RequestMapping("/submitOrder")
    @ResponseBody
    public String submitOrder(HttpSession session, @RequestParam String aid,@RequestParam String money, @RequestParam String discount,
                              @RequestParam String ids) throws Exception {
        AddressInfo addressInfo = new AddressInfo();
        HashMap<String, String> map = new HashMap<>();
        if(aid.equalsIgnoreCase("0")){
           addressInfo = addressInfoService.getDefaultAddress();
        }else {
            addressInfo = addressInfoService.getAddress(Integer.parseInt(aid));
        }
        if(addressInfo == null){
            map.put("result",Constants.RESULT_FALSE);
            return JSONArray.toJSONString(map);
        }
        String address = addressInfo.getProvinceName()+addressInfo.getCityName()+addressInfo.getDistrictName()+addressInfo.getAddress();
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        String[] idArr = ids.split(",");
        for(String id:idArr){
            ShopCar shopCar = shopCarService.getById(Integer.parseInt(id));
            Integer stock = goodsService.getGoodsStock(shopCar.getGoodsId());
            //判断商品库存是否足够，不够返回确认订单页面
            if(stock < shopCar.getGoodsNum()){
                map.put("result",Constants.RESULT_ERROR);
                map.put("goods",shopCar.getGoodsName());
                map.put("stock",stock.toString());
                return JSONArray.toJSONString(map);
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setGoodsId(shopCar.getGoodsId());
            orderDetail.setPrice(BigDecimal.valueOf(Double.parseDouble(shopCar.getSoloPrice())));
            orderDetail.setGoodsNum(shopCar.getGoodsNum());
            orderDetailList.add(orderDetail);
        }
        Order order = new Order();
        order.setPhone(addressInfo.getPhone());
        order.setReceiverName(addressInfo.getReceiverName());
        order.setAddress(address);
        order.setDiscountMoney(BigDecimal.valueOf(Integer.parseInt(discount)));
        order.setGoodsCount(idArr.length);
        order.setOrderMoney(BigDecimal.valueOf(Double.parseDouble(money)));
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        order.setOrderCode(Long.parseLong(OrderCodeUtils.orderCode(u.getId())));
        order.setUserId(u.getId());
        order.setPaymentMoney(BigDecimal.valueOf(Integer.parseInt(money)-Integer.parseInt(discount)));
        order.setOrderPoint(Integer.parseInt(discount));
        Integer id = orderService.insertOrder(order,orderDetailList,money,discount,u.getId(),ids);
        OrderLog orderLog = new OrderLog();    //记录会员下单
        orderLog.setOrderCode(order.getOrderCode());
        orderLog.setContent("会员账户名:"+u.getAccount()+",下单成功");
        orderService.addLog(orderLog);
        map.put("result",Constants.RESULT_TRUE);
        map.put("id",id.toString());
        return JSONArray.toJSONString(map);
    }

    /**
     * 查看我的订单
     * @param status 订单状态
     * @param model model
     * @param session session
     * @return
     * @throws Exception
     */
    @RequestMapping("/myOrder")
    public String myOrder(@RequestParam(value = "status",required = false) Integer status ,
                          Model model,HttpSession session) throws Exception {
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        if(u == null){
            return "redirect:/mall/login";
        }else {
            logger.error("订单状态和用户id:"+status+u.getId());
            List<Order> orderList = orderService.getOrderList(status,u.getId());
            model.addAttribute("orderList",orderList);
            model.addAttribute("status",status);
        }
        return "shop/order";
    }

    /**
     * 进入订单详情页面
     * @param id  订单id
     * @param model model
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderDetails")
    public String myOrderDetails(@RequestParam String id,Model model) throws Exception {
        logger.info("订单的id是:"+id);
        Order order = orderService.getById(Integer.parseInt(id));
        Long orderCode = order.getOrderCode();
        List<OrderDetail> orderDetailList = orderService.getDetailsList(orderCode);
        String stateName = orderService.getStateName(order.getState());
        model.addAttribute("orderDetailList",orderDetailList);
        model.addAttribute("stateName",stateName);
        model.addAttribute("order",order);
        return "shop/order_details";
    }

    /**
     * 进入订单支付页面
     * @param id 订单id
     * @param model model
     * @param session session
     * @return
     * @throws Exception
     */
    @RequestMapping("/pay")
    public String pay(@RequestParam String id,Model model,HttpSession session)throws Exception{
        Order order = orderService.getById(Integer.parseInt(id));
        User u = (User)(session.getAttribute(Constants.USER_SESSION));
        Integer totalCount = shopCarService.getTotalCount(u.getId());
        model.addAttribute("order",order);
        session.setAttribute("totalCount",totalCount);
        return "shop/pay";
    }

    /**
     * 支付成功进入支付成功页面，同时更新订单状态与会员积分和积分变更情况
     * @return
     */
    @RequestMapping("/paySuccess")
    public String paySuccess(@RequestParam String id,HttpSession session,Model model)throws Exception{
        Order o = new Order();
        try {
            o.setState(2);
            o.setPayTime(new Date());
            o.setId(Integer.parseInt(id));
            orderService.update(o);
        } catch (Exception e) {
            logger.error("支付异常,订单状态改变失败!",e);
        }
        Order order = orderService.getById(Integer.parseInt(id));
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderCode(order.getOrderCode());
        orderLog.setContent("会员账户："+u.getAccount()+"已支付，订单状态变更为:待发货。");
        orderService.addLog(orderLog);
        model.addAttribute("order",order);
        return "shop/pay_success";
    }

    /**
     * 支付失败进入支付失败页面
     * @param id id
     * @param model model
     * @return
     */
    @RequestMapping("/payFailure")
    public String payFailure(@RequestParam String id,Model model){
        Order order = orderService.getById(Integer.parseInt(id));
        model.addAttribute("order",order);
        return "shop/pay_failure";
    }

    /**
     * 前台会员更改订单状态
     * @param id
     * @return
     */
    @RequestMapping("/switchOrder")
    @ResponseBody
    public String switchOrderState(@RequestParam String id,HttpSession session){
        logger.info("要调整的订单id和更改的状态是:"+id);
        Order order = orderService.getById(Integer.parseInt(id));
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        HashMap<String, String> map = new HashMap<>();
        try {
            Order o = new Order();
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderCode(order.getOrderCode());
            if(order.getState() == 1){
                o.setState(6);
                orderLog.setContent("会员账户："+u.getAccount()+"已取消订单，订单状态变更为:已取消。");
            }else {
                o.setState(4);
                orderLog.setContent("会员账户："+u.getAccount()+"已确定收货,订单状态变更为:待评价。");
            }
            o.setId(Integer.parseInt(id));
            orderService.updateState(o);
            orderService.addLog(orderLog);  //添加会员操作订单日志；
            map.put("result",Constants.RESULT_TRUE);
        } catch (Exception e) {
            map.put("result",Constants.RESULT_FALSE);
            logger.error("更改订单状态错误:",e);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 进入订单商品评论页面
     * @param orderCode  订单编号
     * @param model  model
     * @return   订单商品评论页面
     * @throws Exception
     */
    @RequestMapping("/comment")
    public String orderComment(@RequestParam String orderCode,Model model)throws Exception{
        logger.info("订单编号是:"+orderCode);
        List<OrderDetail> orderDetailList = orderService.getDetailsList(Long.parseLong(orderCode));
        model.addAttribute("orderCode",orderCode);
        model.addAttribute("orderDetailList",orderDetailList);
        return "shop/order_comment";
    }

    /**
     * 提交商品评论
     * @param ids  商品ids
     * @param session  session
     * @param comments  商品评论
     * @param orderCode  订单编号
     * @return
     * @throws Exception
     */
    @RequestMapping("/submitComments")
    @ResponseBody
    public String submitComments(@RequestParam String ids,HttpSession session,
                                 @RequestParam String[] comments,@RequestParam String orderCode)throws Exception{
        logger.info("提交评论的商品id和评论内容是:"+ids+"  "+comments.length);
        HashMap<String, String> map = new HashMap<>();
        if(ids == "" || ids ==null){
            map.put("result",Constants.RESULT_ERROR);
            return JSONArray.toJSONString(map);
        }
        String[] idArr = ids.split(",");
        List<GoodsComment> goodsCommentList = new ArrayList<>();
        for(int i=0;i<idArr.length ;i++){
            GoodsComment goodsComment = new GoodsComment();
            goodsComment.setGoodsId(Integer.parseInt(idArr[i]));
            goodsComment.setContent(comments[i]);
            goodsCommentList.add(goodsComment);
        }
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        boolean flag = goodsService.insertComment(goodsCommentList,u.getId());
        Order order = orderService.getByCode(Long.parseLong(orderCode));
        order.setState(5);
        orderService.updateState(order);
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderCode(order.getOrderCode());
        orderLog.setContent("会员账户"+u.getAccount()+"已评价订单,状态更新为:已完成。");
        orderService.addLog(orderLog);
        if(flag){
            map.put("result",Constants.RESULT_TRUE);
        }else {
            map.put("result",Constants.RESULT_FALSE);
        }
        return JSONArray.toJSONString(map);
    }
}
