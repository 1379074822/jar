package com.mall.service.Impl;

import com.mall.mapper.ShopCarMapper;
import com.mall.pojo.ShopCar;
import com.mall.service.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShopCarServiceImpl implements ShopCarService {

    /**
     * 购物车mapper
     */
    @Autowired
    ShopCarMapper shopCarMapper;
    @Override
    public boolean insert(ShopCar shopCar)throws Exception {
        boolean flag = false;
        if(shopCarMapper.insert(shopCar)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public ShopCar getByGoodsId(Integer goodsId) throws Exception {
        return shopCarMapper.getByGoodsId(goodsId);
    }

    @Override
    public ShopCar getById(Integer id) throws Exception {
        return shopCarMapper.getById(id);
    }

    @Override
    public List<ShopCar> getShopList(Integer userId) throws Exception {
        return shopCarMapper.getShopList(userId);
    }

    @Override
    public List<ShopCar> getListByIds(String ids) throws Exception {
        return shopCarMapper.getListByIds(ids);
    }

    @Override
    public BigDecimal getTotalPrice(Integer userId) throws Exception {
        return shopCarMapper.getTotalPrice(userId);
    }

    @Override
    public int getTotalCount(Integer userId) throws Exception {
        if(shopCarMapper.getTotalCount(userId) != null){
            return shopCarMapper.getTotalCount(userId);
        }else {
            return 0;
        }
    }

    @Override
    public boolean updateByGoodsId(Integer num, BigDecimal price, Integer goodsId) throws Exception {
        boolean flag = false;
        if(shopCarMapper.updateByGoodsId(num,price,goodsId) > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        boolean flag = false;
        if(shopCarMapper.deleteById(id)> 0){
            flag = true;
        }
        return flag;
    }
}
