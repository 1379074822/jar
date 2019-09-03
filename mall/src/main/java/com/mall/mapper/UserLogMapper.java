package com.mall.mapper;

import com.mall.pojo.UserLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 用户日志接口
 */
@Repository
public interface UserLogMapper {
    /**
     * 根据id删除登录日志信息
     * @param id id
     * @return
     */
    int delete(Integer id);

    /**
     * 会员登录时添加登录日志
     * @param userLog 会员日志实体类
     * @return
     */
    int insert(UserLog userLog);

    /**
     * 获取会员登录日志列表
     * @param account 会员账户
     * @param createTime 创建时间
     * @param currentPage  当前页面
     * @param pageSize 页面大小
     * @return
     * @throws Exception
     */
    List<UserLog> getLogList(@Param("account") String account,@Param("createTime") String createTime,
                             @Param("from") Integer currentPage,@Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 会员登录日志列表数量
     * @param account 会员账户
     * @param createTime 创建时间
     * @return
     * @throws Exception
     */
    int getLogCounts(@Param("account") String account,@Param("createTime") String createTime)throws Exception;
}