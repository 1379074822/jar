package com.mall.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.mall.pojo.Admin;
import com.mall.service.AdminService;
import com.mall.service.OrderService;
import com.mall.tools.Constants;
import com.mall.tools.MD5Utils;
import com.mall.tools.RandomValidateCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 后台登录控制器
 */
@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    /**
     * 管理员接口
     */
    @Autowired
    private AdminService adminService;
    /**
     * 订单接口
     */
    @Autowired
    private OrderService orderService;

    @RequestMapping("/403")
    public String fail(){
        return "403";
    }

    /**
     * 进入后台管理系统
     * @param model model
     * @param session session
     * @return
     */
    @RequestMapping({"/","/index"})
    public String welcome(Model model,HttpSession session){
        Object o = SecurityUtils.getSubject().getPrincipal();
        Admin admin =(Admin)o;
        if(admin == null){
            return "redirect:/login";
        }
        session.setAttribute(Constants.ADMIN_SESSION,admin);
        model.addAttribute("admin",admin);
        return "index";
    }

    /**
     * 主页获取订单报表
     * @return map
     */
    @RequestMapping("/report")
    @ResponseBody
    public String indexReport(){
        Map<String, Object> map = orderService.getReportList();
        logger.error("报表大小:"+map.size());
        return JSONArray.toJSONString(map);
    }

    /**
     * 进入后台登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        logger.debug("进入登录页面");
        return "login";
    }

    /**
     * 后台注销
     * @return
     */
    @RequestMapping("/logout")
    public String logout(){
        logger.debug("退出系统中，稍后进入登录页面");
        SecurityUtils.getSubject().logout(); //清楚shiro里面的数据
        return "login";
    }

    /**
     * 后台管理员登录
     * @param session
     * @param admin
     * @param verify
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public String ajaxLogin(HttpSession session, Admin admin, String verify){
        logger.info("尝试登录中....");
        HashMap<String, String> map = new HashMap<>();
        String random = (String) session.getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
        if(StringUtils.isBlank(verify)){
            map.put("msg",Constants.VERIFY_NULL);
            return JSONArray.toJSONString(map);
        }
        if(!random.equalsIgnoreCase(verify)){
            map.put("msg",Constants.VERIFY_ERROR);
            return JSONArray.toJSONString(map);
        }
        logger.info("管理员账户:"+admin.getAccount()+"; 密码:"+admin.getPassword());
        String pwdAfterMd5 = MD5Utils.encrypt(admin.getAccount(), admin.getPassword());
        //把用户名和密码封装成UsernamePasswordToken对象
        //shrio的身份认证和MD5加密
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getAccount(), pwdAfterMd5);
        Subject currentUser = SecurityUtils.getSubject();
        try{
            currentUser.login(token);
            session.setAttribute(Constants.ADMIN_SESSION,admin);
            map.put("msg",Constants.RESULT_SUCCESS);
        }catch (IncorrectCredentialsException e){
            map.put("msg",Constants.PWD_ERROR);
            logger.error("登录密码错误:",e);
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 验证管理员是否存在
     * @param uname 管理员账户
     * @return
     */
    @PostMapping("/isExitAccount")
    @ResponseBody
    public boolean checkAccount(String uname){
        logger.debug("验证管理员是否存在;"+uname);
        Admin admin= adminService.getAdminByAccount(uname);
        if(admin != null && !StringUtils.isBlank(admin.getAccount()) && admin.getAccount().equals(uname)){
            return  true;
        }
        return false;
    }

    /**
     * 获取验证码
     * @param request request
     * @param response response
     */
    @RequestMapping("/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        }catch (Exception e){
            logger.debug("获取验证码失败===>",e);
        }
    }
}
