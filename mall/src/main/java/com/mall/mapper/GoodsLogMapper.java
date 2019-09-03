package com.mall.mapper;

import com.mall.pojo.GoodsLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品日志接口
 */
@Repository
public interface GoodsLogMapper {
    /**
     * 根据id删除商品操作日志记录
     * @param id  id
     * @return
     */
    int delete(@Param("id") Integer id);

    /**
     * 管理员操作商品时，添加操作日志记录
     * @param goodsLog  操作日志实体类
     * @return
     */
    int insert(GoodsLog goodsLog);

    /**
     * 后台管理员获得日志列表
     * @param goodsName 商品姓名
     * @param createTime 创建时间
     * @param currentPage 当前页面
     * @param pageSize  页面大小
     * @return
     */
    List<GoodsLog> getLogList(@Param("goodsName") String goodsName,@Param("createTime") String createTime,
                              @Param("from") Integer currentPage,@Param("pageSize") Integer pageSize);

    /**
     * 获取日志数量
     * @param goodsName 商品姓名
     * @param createTime 创建时间
     * @return
     */
    int getLogCounts(@Param("goodsName") String goodsName,@Param("createTime") String createTime);

}