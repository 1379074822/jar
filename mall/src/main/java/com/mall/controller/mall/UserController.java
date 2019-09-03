package com.mall.controller.mall;

import com.alibaba.fastjson.JSONArray;
import com.mall.pojo.AddressInfo;
import com.mall.pojo.User;
import com.mall.pojo.UserLog;
import com.mall.pojo.UserPoint;
import com.mall.service.AddressInfoService;
import com.mall.service.UserService;
import com.mall.tools.Constants;
import com.mall.tools.MD5Utils;
import com.mall.tools.PageSupport;
import com.mall.tools.RandomValidateCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 前台会员管理控制器
 */
@Controller
@RequestMapping("/user")
public class UserController {
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
     * 注册时验证账户是否存在
     * @param account 会员账户名
     * @return
     */
    @RequestMapping(value = "/isExitUser",method = RequestMethod.GET)
    @ResponseBody
    public String checkUser(@RequestParam String account){
        logger.info("检查的会员账号为:"+account);
        HashMap<String, String> map = new HashMap<>();
        User user=userService.getUserByAccount(account);
        if(user != null && !StringUtils.isBlank(user.getAccount()) && account.equals(user.getAccount())){
            map.put("result",Constants.RESULT_TRUE);
        }else{
            map.put("result",Constants.RESULT_FALSE);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 用户注册
     * @param session session
     * @param user  会员
     * @param verify  验证码
     * @return
     */
    @RequestMapping(value = "/register")
    @ResponseBody
    public String userRegister(HttpSession session, User user, String verify)throws Exception{
        logger.info("用户注册中.....");
        HashMap<String, String> map = new HashMap<>();
        String random = (String) session.getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
        if(!random.equalsIgnoreCase(verify)){
            map.put("result",Constants.RESULT_FALSE);
            return JSONArray.toJSONString(map);
        }
        String pwdAfterMd5 = MD5Utils.encrypt(user.getAccount(),user.getPassword());
        user.setPassword(pwdAfterMd5);
        user.setPoint(20); //注册获得20积分
        try{
            if (userService.insertUser(user)) {
                User u=userService.getUserByAccount(user.getAccount());
                UserPoint userPoint = new UserPoint();
                userPoint.setUserId(u.getId());
                userPoint.setSource(1);
                userPoint.setCurrentPoint(50);
                userPoint.setChangePoint("注册会员,+50积分");
                if(userService.insertPoint(userPoint)){  //添加会员积分变更记录
                    logger.info("用户注册成功!");
                    map.put("result",Constants.RESULT_TRUE);
                }
                UserLog userLog = new UserLog();
                userLog.setAccount(user.getAccount());
                userLog.setContent("注册成为会员!");
                userService.insertLog(userLog);
            }
        }catch (Exception e){
            logger.error("用户注册失败!",e);
            map.put("result",Constants.RESULT_ERROR);
        }

        return JSONArray.toJSONString(map);
    }

    /**
     * 用户登录
     * @param session session
     * @param account 会员账户名
     * @param password  会员密码
     * @param verify  验证码
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public String userLogin(HttpSession session,@RequestParam String account,
                            @RequestParam String password,@RequestParam String verify)throws Exception{
        logger.info("用户登录中....");
        String random = (String) session.getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
        HashMap<Object, Object> map = new HashMap<>();
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            map.put("result",Constants.USER_MESSAGE);
            return JSONArray.toJSONString(map);
        }
        if(!random.equalsIgnoreCase(verify)){
            map.put("result",Constants.VERIFY_ERROR);
            return JSONArray.toJSONString(map);
        }
        User user = userService.getUserByAccount(account);
        if(user == null){
            map.put("result",Constants.RESULT_ERROR);
            return JSONArray.toJSONString(map);
        }
        if(user.getState() == 1){
            map.put("result",Constants.USER_STATE);
            return JSONArray.toJSONString(map);
        }
        String pwdAfterMD5 = MD5Utils.encrypt(account,password);
        logger.info("加密后密码:"+pwdAfterMD5);
        if(!pwdAfterMD5.equals(user.getPassword())){
            map.put("result",Constants.PWD_ERROR);
        }else {
            session.setAttribute(Constants.USER_SESSION,user);
            map.put("result",Constants.RESULT_TRUE);
        }
        UserLog userLog = new UserLog();
        userLog.setAccount(user.getAccount());
        userLog.setContent("登录成功!");
        userService.insertLog(userLog);
        return JSONArray.toJSONString(map);
    }

    /**
     * 用户注销退出系统
     * @param session
     * @return
     */
    @RequestMapping({"/logout"})
    public String logOut(HttpSession session)throws Exception{
        UserLog userLog = new UserLog();
        //添加会员注销日志
        User u = (User)session.getAttribute(Constants.USER_SESSION);
        userLog.setAccount(u.getAccount());
        userLog.setContent("会员注销退出系统!");
        userService.insertLog(userLog);
        session.removeAttribute(Constants.USER_SESSION);
        session.removeAttribute("totalCount");
        return "redirect:/mall/index";
    }

    /**
     * 修改用户信息
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public String userModify(User user,HttpSession session){
        user.setId(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        User u=(User)session.getAttribute(Constants.USER_SESSION);
        logger.info("用户信息修改中,信息:"+user.getAccount()+user.getNickName()+user.getEmail()
                            +user.getPhone()+user.getId()+user.getPassword());
        try{
           userService.updateUser(user);
        }catch (Exception e){
            logger.error("修改错误!",e);
        }
        return "redirect:/mall/userCenter";
    }

    /**
     * 检查原密码是否存在
     * @param password  原密码
     * @param session session
     * @return
     */
    @RequestMapping("/checkPwd")
    @ResponseBody
    public String checkPwd(@RequestParam String password,HttpSession session){
        logger.info("要修改的原密码是:"+password);
        HashMap<Object, Object> map = new HashMap<>();
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        String pwdAfterMD5 = MD5Utils.encrypt(user.getAccount(),password);
        if(!pwdAfterMD5.equalsIgnoreCase(user.getPassword())){
            map.put("result",Constants.RESULT_FALSE);
        }else {
            map.put("result",Constants.RESULT_TRUE);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 修改密码
     * @param surePwd 新的密码
     * @param session session
     * @return
     */
    @RequestMapping("/modifyPwd")
    @ResponseBody
    public String modifyPwd(@RequestParam String surePwd,HttpSession session)throws Exception{
        logger.info("要修改的新密码是:"+surePwd);
        HashMap<Object, Object> map = new HashMap<>();
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        String pwdAfterMD5 = MD5Utils.encrypt(user.getAccount(),surePwd);
        user.setPassword(pwdAfterMD5);
        userService.updateUser(user);
        UserLog userLog = new UserLog();
        userLog.setAccount(user.getAccount());
        userLog.setContent("成功修改密码!");
        userService.insertLog(userLog);
        session.removeAttribute(Constants.USER_SESSION);
        map.put("result",Constants.RESULT_TRUE);
        logger.info("修改密码成功!");
        return JSONArray.toJSONString(map);
    }

    /**
     * 用户添加收货地址
     * @param addressInfo  用户地址表
     * @param session session
     * @return
     */
    @RequestMapping("/addNewAddress")
    public String addNewAddress(AddressInfo addressInfo,HttpSession session){
        logger.info("地址信息是:"+addressInfo.getReceiverName()+addressInfo.getProvinceName()+
                        addressInfo.getCityName()+addressInfo.getDistrictName());
        if(addressInfo.getReceiverName() == null || addressInfo.getPhone() == null
                || addressInfo.getProvinceName().equalsIgnoreCase("省/直辖市")
                || addressInfo.getCityName().equalsIgnoreCase("城市")
                || addressInfo.getDistrictName().equalsIgnoreCase("区/县")){
            return "redirect:/mall/address";
        }
        addressInfo.setStatus(0);
        addressInfo.setUserId(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        try{
            if(addressInfoService.addAddress(addressInfo)){
                logger.info("添加成功!");
                return "redirect:/mall/address";
            }
        }catch (Exception e){
            logger.error("添加地址失败!",e);
            return "redirect:/mall/address";
        }
        return "redirect:/mall/address?pageIndex=1";
    }


    /**
     * 删除用户地址
     * @param id 地址id
     * @return
     * @throws Exception
     */
    @RequestMapping({"/delAddress"})
    public String deleteAddress(@RequestParam String id) throws Exception {
        logger.info("删除的id是:"+id);
        if(addressInfoService.deleteAddress(Integer.parseInt(id))){
            return "redirect:/user/addNewAddress";
        }
        return "redirect:/user/addNewAddress";
    }

    /**
     * 修改地址是否为默认
     * @param id  地址id
     * @return
     */
    @RequestMapping("/updateDefaultAddress")
    @ResponseBody
    public String updateDefaultAddress(@RequestParam String id){
        logger.info("修改的默认地址的id:   "+id);
        HashMap<String, String> map = new HashMap<>();
        try {
            if(addressInfoService.updateState(Integer.parseInt(id))){
                if(addressInfoService.updateNotId(Integer.parseInt(id))){
                    map.put("updateResult",Constants.RESULT_TRUE);
                }else {
                    map.put("updateResult",Constants.RESULT_FALSE);
                }
            }else {
                map.put("updateResult",Constants.RESULT_ERROR);
            }
        }catch (Exception e){
            logger.error("修改默认地址失败:",e);
        }
        return JSONArray.toJSONString(map);
    }


    /**
     * 查看积分变更明细
     * @param model model
     * @param session session
     * @param pageIndex 当前页面
     * @return
     */
    @RequestMapping("/viewPoint")
    public String pointList(Model model,HttpSession session,
                            @RequestParam(value = "pageIndex",required = false) String pageIndex)throws Exception{
        List<UserPoint> userPointList = null;
        Integer userId = ((User)session.getAttribute(Constants.USER_SESSION)).getId();
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        //总数量(表)
        int totalCount = userService.getPointCount(userId);
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
        try{
            userPointList=userService.getPointList(userId,currentPageNo,pageSize);
        }catch (Exception e){
            logger.info("查看会员信息失败!",e);
        }
        model.addAttribute("pages",pages);
        model.addAttribute("userPointList",userPointList);
        return "shop/user_point";
    }


}
