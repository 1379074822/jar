package com.mall.mapper;

import com.mall.pojo.GoodsComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品评论接口
 */
@Repository
public interface GoodsCommentMapper {

    /**
     * 批量插入用户商品评论
     * @param list 用户添加商品评论list
     * @return
     */
    int insert(@Param("list") List<GoodsComment> list,@Param("userId") Integer userId);

    /**
     * 根据商品id获得商品评论列表
     * @param id  商品id
     * @param currentPageNo  当前页码
     * @param pageSize  页面大小
     * @return
     * @throws Exception
     */
    List<GoodsComment> getById(@Param("id") Integer id,@Param("from") Integer currentPageNo,
                               @Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 获得商品评论数量
     * @param id
     * @return
     * @throws Exception
     */
    int getCountById(@Param("id") Integer id)throws Exception;

    /**
     * 删除评论
     * @param goodsComment  商品评论实体类
     * @return
     */
    int delete(GoodsComment goodsComment);

}