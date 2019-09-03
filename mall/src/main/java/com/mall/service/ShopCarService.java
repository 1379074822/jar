package com.mall.service;

import com.mall.pojo.ShopCar;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 购物车服务接口
 */
public interface ShopCarService {

    /**
     * 是否添加购物车成功
     * @param shopCar
     * @return
     */
    boolean insert(ShopCar shopCar)throws Exception;

    /**
     * 根据商品ID获得购物车
     * @param goodsId
     * @return
     * @throws Exception
     */
    ShopCar getByGoodsId(Integer goodsId)throws Exception;

    /**
     * 根据ID获得购物车
     * @param id
     * @return
     * @throws Exception
     */
    ShopCar getById(Integer id)throws Exception;

    /**
     * 根据用户ID获得购物车列表
     * @param userId
     * @return
     * @throws Exception
     */
    List<ShopCar> getShopList(Integer userId)throws Exception;

    /**
     * 获得加入结算的商品列表
     * @param ids
     * @return
     * @throws Exception
     */
    List<ShopCar> getListByIds(String ids)throws Exception;

    /**
     * 根据用户ID获取该用户购物车总价格
     * @param userId
     * @return
     * @throws Exception
     */
    BigDecimal getTotalPrice( Integer userId)throws Exception;
    /**
     * 根据用户ID获取购物车总商品数量
     * @param userId
     * @return
     * @throws Exception
     */
    int getTotalCount(Integer userId)throws Exception;

    /**
     * 修改同一商品数量和总价
     * @param num
     * @param price
     * @param goodsId
     * @return
     * @throws Exception
     */
    boolean updateByGoodsId(Integer num,BigDecimal price,Integer goodsId)throws Exception;

    /**
     * 根据id删除购物车
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteById(String id)throws Exception;
}
