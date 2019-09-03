package com.mall.mapper;

import com.mall.pojo.GoodsStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品库存接口
 */
@Repository
public interface GoodsStockMapper {

    /**
     * 删除商品后同时清除库存记录
     * @param goodsId
     * @return
     */
    int deleteStock(@Param("goodsId") Integer goodsId);

    /**
     * 添加商品后，同时添加商品库存信息
     * @param record
     * @return
     */
    int insert(GoodsStock record);


    /**
     * 管理员修改商品库存时，库存更新
     * @return
     */
    int updateStockByAdmin(@Param("stock") Integer stock,@Param("goodsId") Integer goodsId);

    /**
     * 获得商品销量
     * @param id
     * @return
     * @throws Exception
     */
    int getGoodsSale(@Param("id") Integer id)throws Exception;

    /**
     * 根据商品id获取商品库存
     * @param id
     * @return
     * @throws Exception
     */
    int getGoodsStock(@Param("id") Integer id)throws Exception;

    /**
     * 下单后,更新库存和销量
     * @param count  减库存(商品数量)
     * @param goodsId  商品
     * @return
     * @throws Exception
     */
    int updateByOrder(@Param("count") Integer count,@Param("goodsId") Integer goodsId)throws Exception;

    /**
     * 下单24小时未支付，自动取消订单库存回滚
     * @param count
     * @param goodsId
     * @return
     * @throws Exception
     */
    int stockRollback(@Param("count") Integer count,@Param("goodsId") Integer goodsId)throws Exception;
}