package com.mall.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.mall.pojo.DataDictionary;
import com.mall.pojo.User;
import com.mall.pojo.UserLog;
import com.mall.service.DataDictionaryService;
import com.mall.service.UserService;
import com.mall.tools.Constants;
import com.mall.tools.PageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 后台管理会员控制器
 */
@Controller
@RequestMapping(value = "/system/user")
public class AccountController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);
   /**
    *  会员接口
    */
   @Autowired
   private UserService userService;
    /**
     * 数据字典接口
     */
   @Autowired
   private DataDictionaryService dataDictionaryService;

    /**
     * 查看用户信息
     * @param model model
     * @param queryAccountName 账户名
     * @param queryNickName  昵称
     * @param queryState  状态id
     * @param pageIndex  当前页面
     * @return
     */
    @RequestMapping(value = "/list")
    public String getUserList(Model model, @RequestParam(value = "queryAccountName",required = false) String queryAccountName,
                              @RequestParam(value = "queryNickName",required = false) String queryNickName,
                              @RequestParam(value = "queryState",required = false) String queryState,
                              @RequestParam(value = "pageIndex",required = false) String pageIndex)throws Exception{
        logger.info("用户信息.."+queryAccountName+queryNickName+queryState+pageIndex);
        List<User> userList = null;
        List<DataDictionary> stateList = null;
        int pageSize = Constants.pageSizeAddress;  //页面大小
        Integer currentPageNo = 1;  //当前页码
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        Integer state = null;
        if (queryState != null && !queryState.equals("")) {
            state = Integer.valueOf(queryState);
        }
        //总数量(表)
        int totalCount = userService.getUserCount(queryAccountName,queryNickName,state);
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();  //总页数
        //控制首页和尾页
        if(currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        userList=userService.getUserList(queryAccountName,queryNickName,state,currentPageNo,pageSize);
        stateList = dataDictionaryService.getDataDictionaryList("USER_STATE");
        model.addAttribute("pages",pages);
        model.addAttribute("queryAccountName",queryAccountName);
        model.addAttribute("queryNickName",queryNickName);
        model.addAttribute("queryState",state);
        model.addAttribute("stateList",stateList);
        logger.info("分页信息是:"+pages.getCurrentPageNo()+pages.getTotalPageCount());
        model.addAttribute("userList",userList);
        return "system/user/list";
    }

    /**
     * 查看用户信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/userView/{id}",method = RequestMethod.GET)
    public String viewUser(@PathVariable String id,Model model){
        logger.info("查看会员id:"+id);
        Integer userId=Integer.parseInt(id);
        User user = null;
        user = userService.getUserById(userId);
        model.addAttribute(user);
        return "system/user/view";
    }

    /**
     * 操作会员(禁用或解封会员)
     * @param id  会员id
     * @param state 会员状态id
     * @return
     */
    @RequestMapping("/operationUser")
    @ResponseBody
    public String operationUser(@RequestParam String id,@RequestParam String state){
        logger.info("会员id和状态:"+id);
        HashMap<String, String> map = new HashMap<>();
        try{
            if(userService.updateState(Integer.parseInt(state),Integer.parseInt(id))){
                map.put("result",Constants.RESULT_TRUE);
            }else {
                map.put("result",Constants.RESULT_FALSE);
            }
        }catch (Exception e){
            map.put("result",Constants.RESULT_ERROR);
            logger.error("操作会员异常:",e);
        }
        return JSONArray.toJSONString(map);
    }


    /**
     * 查看会员日志信息列表
     * @param model model
     * @param account 账户
     * @param queryCreateTime 创建时间
     * @param pageIndex 当前页面
     * @return
     * @throws Exception
     */
    @RequestMapping("/logList")
    public String getLogList(Model model,@RequestParam(value = "account",required = false) String account,
                             @RequestParam(value = "createTime",required = false) String queryCreateTime,
                             @RequestParam(value = "pageIndex", required = false) String pageIndex)throws Exception{
        List<UserLog> userLogList = new ArrayList<>();
        int pageSize = Constants.pageSizeAddress;  //页面大小
        //当前页码
        Integer currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        int totalCount = userService.getLogCount(account,queryCreateTime); //总数量表
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();   //总页数
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        try{
            userLogList = userService.getLogList(account,queryCreateTime,currentPageNo,pageSize);
        }catch (Exception e){
            logger.error("查询订单日志列表出错:",e);
        }
        model.addAttribute("userLogList", userLogList);
        model.addAttribute("account",account);
        model.addAttribute("createTime",queryCreateTime);
        model.addAttribute("pages", pages);
        return "system/user/logList";
    }

    /**
     * 管理员删除操作日志表
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteLog")
    @ResponseBody
    public String deleteUserLog(@RequestParam String id)throws Exception{
        HashMap<String, String> map = new HashMap<>();
        if(userService.deleteLog(Integer.parseInt(id))){
            map.put("result",Constants.RESULT_TRUE);
        }else {
            map.put("result",Constants.RESULT_ERROR);
            throw new Exception("删除日志异常!");
        }
        return JSONArray.toJSONString(map);
    }
}
