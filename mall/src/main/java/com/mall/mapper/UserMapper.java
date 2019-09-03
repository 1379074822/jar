package com.mall.mapper;

import com.mall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员接口
 */
@Repository
public interface UserMapper {

    /**
     * 添加会员基本信息
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 修改会员信息
     * @param record
     * @return
     */
    int update(User record);


    /**
     * 根据ID获得会员信息
     * @param id
     * @return
     */
    User selectById(Integer id);

    /**
     * 根据Id删除会员
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * 按账户名查找
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 后台系统查找会员列表
     * @param queryAccountName
     * @param queryNickName
     * @return
     */
    List<User> getUserList(@Param(value = "queryAccountName") String queryAccountName,@Param(value = "queryNickName") String queryNickName,
                           @Param(value = "state") Integer state,@Param(value = "from") Integer currentPageNo,
                           @Param(value = "pageSize") Integer pageSize)throws Exception;

    /**
     * 查询会员数量
     * @return
     */
    int getUserCount(@Param(value = "queryAccountName") String queryAccountName,@Param(value = "queryNickName") String queryNickName,
                     @Param(value = "state") Integer state)throws Exception;

    /**
     * 修改会员状态
     * @param state
     * @param id
     * @return
     * @throws Exception
     */
    int updateState(@Param("state") Integer state,@Param("id") Integer id)throws Exception;
}