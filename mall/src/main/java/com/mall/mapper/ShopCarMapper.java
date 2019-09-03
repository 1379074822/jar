package com.mall.mapper;

import com.mall.pojo.ShopCar;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 购物车接口
 */
@Repository
public interface ShopCarMapper {
    /**
     * 根据id删除购物车信息
     * @param id
     * @return
     * @throws Exception
     */
    int deleteById(@Param("id") String id)throws Exception;

    /**
     * 增加商品至购物车
     * @param record
     * @return
     */
    int insert(ShopCar record)throws Exception;
    /**
     * 根据商品id获得购物车信息
     * @param id
     * @return
     * @throws Exception
     */
    ShopCar getByGoodsId(@Param("goodsId") Integer id)throws Exception;

    /**
     * 根据用户ID获得购物车列表
     * @param userId
     * @return
     * @throws Exception
     */
    List<ShopCar> getShopList(@Param("userId") Integer userId)throws Exception;


    /**
     * 获得加入结算的商品列表
     * @param ids
     * @return
     * @throws Exception
     */
    List<ShopCar> getListByIds(@Param("ids") String ids)throws Exception;

    /**
     * 根据用户ID获取该用户购物车总价格
     * @param userId
     * @return
     * @throws Exception
     */
    BigDecimal getTotalPrice(@Param("userId") Integer userId)throws Exception;

    /**
     * 根据id获得购物车信息
     * @param id
     * @return
     * @throws Exception
     */
    ShopCar getById(@Param("id") Integer id)throws Exception;
    /**
     * 根据用户ID获取购物车总商品数量
     * @param userId
     * @return
     * @throws Exception
     */
    Integer getTotalCount(@Param("userId") Integer userId)throws Exception;

    /**
     * 修改同一商品数量和总价
     * @param num
     * @param price
     * @param goodsId
     * @return
     * @throws Exception
     */
    int updateByGoodsId(@Param("num") Integer num,@Param("price") BigDecimal price,
                        @Param("goodsId") Integer goodsId)throws Exception;
}