package com.mall.service;

import com.mall.pojo.Activity;
import com.mall.pojo.User;
import com.mall.pojo.UserLog;
import com.mall.pojo.UserPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员服务接口
 */
public interface UserService {
    /**
     * 登录查找，按用户名查找
     * @param account  会员账户
     * @return
     */
    public User getUserByAccount(String account);

    /**
     * 后台系统查找用户列表
     * @param queryAccountName  会员账户
     * @param queryNickName 会员昵称
     * @param state 会员状态
     * @return
     */
    public List<User> getUserList(String queryAccountName, String queryNickName ,Integer state,
                                  Integer currentPageNo,Integer pageSize)throws Exception;


    /**
     * 查询会员数量
     * @param queryAccountName  会员账户
     * @param queryNickName 会员昵称
     * @param state 会员状态
     * @return
     * @throws Exception
     */
    public int getUserCount(String queryAccountName, String queryNickName,Integer state)throws Exception;

    /**
     * 添加会员
     * @param user  会员实体类
     * @return
     */
    public boolean insertUser(User user);

    /**
     * 根据ID查询会员(使用缓存)
     * @param id  会员id
     * @return
     */
    public User getUserById(Integer id);

    /**
     * 修改会员信息
     * @param user 会员实体类
     * @return
     */
    public User updateUser(User user);

    /**
     * 修改会员状态
     * @param state  会员状态id
     * @param id  会员id
     * @return
     */
    public boolean updateState(Integer state ,Integer id)throws Exception;

    /**
     * 获取活动(当前积分兑换比例)
     * @return
     */
    Activity getActivity();

    /**
     * 更新积分兑换比例
     */
    boolean updateActivity(Activity activity);

    /**
     * 积分变更记录条数
     * @param id  积分变更日志id
     * @return
     * @throws Exception
     */
    int getPointCount(Integer id)throws Exception;

    /**
     * 添加会员积分情况
     * @param userPoint  积分变更实体类
     * @return
     */
    boolean insertPoint(UserPoint userPoint);

    /**
     * 获得会员积分列表
     * @param userId 会员id
     * @param currentPageNo 当前页吗
     * @param pageSize  页面大小
     * @return
     * @throws Exception
     */
    List<UserPoint> getPointList(Integer userId,Integer currentPageNo, Integer pageSize)throws Exception;

    /**
     * 会员登录时添加登录日志
     * @param userLog 会员日志实体类
     * @return
     * @throws Exception
     */
    boolean insertLog(UserLog userLog)throws Exception;

    /**
     * 根据id删除登录日志信息
     * @param id id
     * @return
     */
    boolean deleteLog(Integer id)throws Exception;

    /**
     * 获取会员登录日志列表
     * @param account 会员账户
     * @param createTime 创建时间
     * @param currentPage  当前页面
     * @param pageSize 页面大小
     * @return
     * @throws Exception
     */
    List<UserLog> getLogList(String account,String createTime, Integer currentPage,Integer pageSize)throws Exception;

    /**
     * 会员登录日志列表数量
     * @param account 会员账户
     * @param createTime 创建时间
     * @return
     * @throws Exception
     */
    int getLogCount(String account,String createTime)throws Exception;
}
