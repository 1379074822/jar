package com.mall.controller.system;

import com.mall.pojo.Activity;
import com.mall.pojo.User;
import com.mall.pojo.UserPoint;
import com.mall.service.UserService;
import com.mall.tools.Constants;
import com.mall.tools.PageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 后台积分管理控制器
 */
@Controller
@RequestMapping("/system/point")
public class PointController {
    private Logger logger = LoggerFactory.getLogger(PointController.class);
   /**
    * 会员接口
    */
   @Autowired
   private UserService userService;

    /**
     * 查看会员积分信息
     * @param model model
     * @param pageIndex 当前页面
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model,@RequestParam(value = "pageIndex",required = false) String pageIndex){
        List<User> userList = null;
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        //总数量(表)
        int totalCount = 0;
        try{
            totalCount = userService.getUserCount(null,null,null);
            logger.info("总数量:"+totalCount);
        }catch (Exception e){
            logger.info("查看会员信息总数量!",e);
        }
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
            userList=userService.getUserList(null,null,null,currentPageNo,pageSize);
        }catch (Exception e){
            logger.info("查看会员信息失败!",e);
        }
        Activity activity = userService.getActivity();
        model.addAttribute("pages",pages);
        logger.info("分页信息是:"+pages.getCurrentPageNo()+pages.getTotalPageCount());
        model.addAttribute("userList",userList);
        model.addAttribute("activity",activity);
        return "system/point/list";
    }

    /**
     * 修改积分兑换比例
     * @param activity 比例
     * @return
     */
    @RequestMapping("/modifyPro")
    public String modifyProportion(Activity activity){
        Integer num = activity.getActivityRule();
        activity.setActivityBrief("积分兑换比例：满1000减"+ num);
        try{
            userService.updateActivity(activity);
            return "redirect:/system/point/list";
        }catch (Exception e){
            logger.error("修改积分兑换比例",e);
        }
        return "redirect:/system/point/list";
    }

    /**
     * 查看用户积分变更情况
     * @param model model
     * @param userId 会员id
     * @param pageIndex 当前页面
     * @return
     */
    @RequestMapping("/viewPoint")
    public String pointList(Model model,
                            @RequestParam (value = "userId",required = false)String userId,
                            @RequestParam(value = "pageIndex",required = false) String pageIndex){
        logger.info("查看积分变更情况的用户id："+userId);
        List<UserPoint> userPointList = null;
        //页面大小
        int pageSize = Constants.pageSizeAddress;
        //当前页码
        Integer currentPageNo = 1;
        if(pageIndex != null){
            currentPageNo = Integer.valueOf(pageIndex);
        }
        //总数量(表)
        int totalCount = 0;
        try{
            totalCount = userService.getPointCount(Integer.parseInt(userId));
            logger.info("总数量:"+totalCount);
        }catch (Exception e){
            logger.info("查看会员信息总数量!",e);
        }
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
            userPointList=userService.getPointList(Integer.parseInt(userId),currentPageNo,pageSize);
        }catch (Exception e){
            logger.info("查看会员信息失败!",e);
        }
        model.addAttribute("pages",pages);
        logger.info("分页信息是:"+pages.getCurrentPageNo()+pages.getTotalPageCount());
        model.addAttribute("userPointList",userPointList);
        model.addAttribute("userId",userId);
        return "system/point/changelist";
    }
}
