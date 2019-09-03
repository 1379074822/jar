package com.mall.service;

import com.mall.pojo.*;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品服务接口
 */
public interface GoodsService {

    /**
     * 条件查询商品列表
     * @param queryGoodsName 商品名称
     * @param queryGoodsCore 商品货号
     * @param queryState 商品状态id
     * @param queryOneCategoryId 一级分类id
     * @param queryTwoCategoryId 二级分类id
     * @param currentPageNo 当前页面
     * @param pageSize 页面大小
     * @return
     * @throws Exception
     */
    List<Goods> getGoodsListByParam(String queryGoodsName,String queryGoodsCore,Integer queryState,
                                    Integer queryOneCategoryId,Integer queryTwoCategoryId,
                                    Integer currentPageNo, Integer pageSize)throws Exception;

    /**
     * 条件查询商品数量
     * @param queryGoodsName 商品名称
     * @param queryGoodsCore 商品货号
     * @param queryState 商品状态id
     * @param queryOneCategoryId 一级分类id
     * @param queryTwoCategoryId 二级分类id
     * @return
     * @throws Exception
     */
    int getGoodsCountByParam(String queryGoodsName,String queryGoodsCore,Integer queryState,
                             Integer queryOneCategoryId,Integer queryTwoCategoryId)throws Exception;

    /**
     * 检查商品名称是否存在
     * @param goodsName  商品名称
     * @return
     * @throws Exception
     */
    Goods checkGoodsName(String goodsName)throws Exception;

    /**
     * 添加商品
     * @param goods
     * @return
     */
    boolean insertGoods(Goods goods)throws Exception;

    /**
     * 增加商品库存记录
     * @param goodsStock 商品库存
     * @return
     * @throws Exception
     */
    boolean insertGoodsStock(GoodsStock goodsStock)throws Exception;

    /**
     * 根据Id获得商品
     * @param id 商品id
     * @return
     * @throws Exception
     */
    Goods getGoodsById(Integer id)throws Exception;

    /**
     * 删除商品基本信息、库存记录及存储在本地的Logo图片
     * @param id 商品id
     * @return
     * @throws Exception
     */
    boolean deleteGoodsById(Integer id)throws Exception;

    /**
     * 修改商品信息(包括上、下架状态)
     * @param goods 商品实体类
     * @return
     * @throws Exception
     */
    Goods update(Goods goods)throws Exception;

    /**
     * 修改商品时，更新库存
     * @param stock  库存
     * @param goodsId 商品id
     * @return
     * @throws Exception
     */
    boolean updateStockByAdmin(Integer stock,Integer goodsId)throws Exception;

    /**
     * 修改商品过程中，若删除图片，则更新商品表
     * @param id 商品id
     * @return
     * @throws Exception
     */
    boolean deleteGoodsLogo(Integer id)throws Exception;

    /**
     * 前台主页商品显示
     * @return
     * @throws Exception
     */
    List<Goods> getAllGoodsByLimit()throws Exception;

    /**
     * 获取商品销量
     * @param id 商品id
     * @return
     * @throws Exception
     */
    int getGoodsSale(Integer id)throws Exception;

    /**
     * 获得商品库存
     * @param id
     * @return
     * @throws Exception
     */
    int getGoodsStock(Integer id) throws Exception;

    /**
     * 通过分类id查询商品
     * @param id  商品分类id
     * @return
     * @throws Exception
     */
    Goods checkByCategory(Integer id)throws Exception;

    /**
     * 获取数据库的所有商品资源
     * @return
     */
    List<Goods> getGoodsList();

    /**
     * 前台显示热门商品
     * @return
     */
    List<Goods> getHotGoods();

    /**
     * 前台显示最新商品
     * @return
     */
    List<Goods> getNewGoods();

    /**
     * 将数据库的数据导入到elasticsearch
     * @param goodsList  商品list
     */
    void saveGoods(List<Goods> goodsList);

    /**
     * 根据输入的值全文搜索商品内容
     * @param keyword  输入的值
     * @return
     */
    List<Goods> searchGoods(String keyword);

    /**
     * 批量插入用户商品评论
     * @param goodsCommentList
     * @param userId
     * @throws Exception
     */
    boolean insertComment(List<GoodsComment> goodsCommentList,Integer userId)throws Exception;

    /**
     * 获取商品评论列表
     * @param id  商品id
     * @param currentPageNo  当前页面
     * @param pageSize  页面大小
     * @return
     * @throws Exception
     */
    List<GoodsComment> getListById(Integer id,Integer currentPageNo,Integer pageSize)throws Exception;

    /**
     * 获得商品评论数量
     * @param id 商品id
     * @return
     * @throws Exception
     */
    int getCountById(Integer id)throws Exception;

    /**
     * 添加商品操作日志
     * @param goodsLog 日志实体类
     * @return
     */
    boolean insertLog(GoodsLog goodsLog);

    /**
     * 管理员删除日志信息
     * @param id
     * @return
     */
    boolean deleteLog(Integer id);

    /**
     * 获取商品日志数量
     * @param goodsName 商品名称
     * @param createTime 创建时间
     * @return
     */
    int getLogCount(String goodsName, String createTime);

    /**
     * 获得商品操作日志列表
     * @param goodsName 商品名称
     * @param createTime 创建时间
     * @param currentPage 当前页码
     * @param pageSize 页面大小
     * @return
     */
    List<GoodsLog> getLogList(String goodsName,String createTime,Integer currentPage,Integer pageSize);
}
