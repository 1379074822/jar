package com.mall.mapper;

import com.mall.pojo.UserPoint;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 会员积分操作接口
 */
@Repository
public interface UserPointMapper {

    /**
     * 添加会员积分变更记录
     * @param record
     * @return
     */
    int insert(UserPoint record);

    /**
     * 查看会员积分情况
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<UserPoint> getPointList(@Param("userId") Integer userId,@Param("from") Integer currentPageNo,
                                 @Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 获取积分变更记录条数
     * @param userId
     * @return
     * @throws Exception
     */
    int getPointCount(@Param("userId") Integer userId)throws Exception;
}