package com.mall.mapper;

import com.mall.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品信息接口
 */
@Repository
public interface GoodsMapper {
    /**
     * 添加商品
     * @param record
     * @return
     */
    int insert(Goods record)throws Exception;

    /**
     * 根据Id获取商品
     * @param id
     * @return
     */
    Goods getGoodsById(@Param("id") Integer id)throws Exception;

    /**
     * 修改商品信息(包括上、下架等)
     * @param goods
     * @return
     */
    int update(Goods goods)throws Exception;

    /**
     * 条件查询商品列表
     * @param goodsName
     * @param goodsCore
     * @param state
     * @param oneCategoryId
     * @param twoCategoryId
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<Goods> getGoodsListByParam(@Param("goodsName") String goodsName,@Param("goodsCore") String goodsCore,
                                    @Param("state") Integer state,@Param("oneCategoryId") Integer oneCategoryId,
                                    @Param("twoCategoryId") Integer twoCategoryId, @Param("from") Integer currentPageNo,
                                    @Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 条件查询商品数量
     * @param goodsName
     * @param goodsCore
     * @param state
     * @param oneCategoryId
     * @param twoCategoryId
     * @return
     * @throws Exception
     */
    int getGoodsCountByParam(@Param("goodsName") String goodsName,@Param("goodsCore") String goodsCore,
                             @Param("state") Integer state,@Param("oneCategoryId") Integer oneCategoryId,
                             @Param("twoCategoryId") Integer twoCategoryId)throws Exception;

    /**
     * 检查商品名称是否存在
     * @param goodsName
     * @return
     */
    Goods checkGoods(@Param("goodsName") String goodsName)throws Exception;

    /**
     * 通过分类id查询商品
     * @param id  商品分类id
     * @return
     * @throws Exception
     */
    Goods checkByCategory(@Param("id") Integer id)throws Exception;

    /**
     * 根据id删除商品
     * @param id   商品id
     * @return
     * @throws Exception
     */
    int deleteGoodsById(@Param("id") Integer id)throws Exception;

    /**
     * 修改商品过程中，若删除图片，则更新商品表
     * @param id
     * @return
     * @throws Exception
     */
    int deleteGoodsLogo(@Param("id") Integer id)throws Exception;

    /**
     * 前台首页商品显示
     */
    List<Goods> getAllGoodsByLimit()throws Exception;

    /**
     * 获取所有商品资源
     */
    List<Goods> getGoodsList();

    /**
     * 主页显示热门产品
     * @return
     */
    List<Goods> getHotGoods();

    /**
     * 最新产品
     * @return
     */
    List<Goods> getNewGoods();
}