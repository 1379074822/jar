package com.mall.service.Impl;

import com.mall.mapper.GoodsCategoryMapper;
import com.mall.pojo.GoodsCategory;
import com.mall.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService{
    /**
     * 商品分类mapper
     */
    @Autowired
    GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public List<GoodsCategory> getCategoryListByParentId(Integer parentId) throws Exception {
        return goodsCategoryMapper.getCategoryListByParentId(parentId);
    }

    @Override
    public List<GoodsCategory> getCategoryList(Integer parentId, Integer currentPageNo, Integer pageSize) throws Exception {
        return goodsCategoryMapper.getCategoryList(parentId,(currentPageNo-1)*pageSize,pageSize);
    }

    @Override
    public int getCategoryCount(Integer parentId) throws Exception {
        return goodsCategoryMapper.getCategoryCount(parentId);
    }

    @Override
    public boolean insert(GoodsCategory goodsCategory) throws Exception {
        boolean flag = false;
        if(goodsCategoryMapper.insert(goodsCategory)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public GoodsCategory getCategory(Integer id) throws Exception {
        return goodsCategoryMapper.getCategory(id);
    }

    @Override
    public GoodsCategory getByParentId(Integer parentId) throws Exception {
        return goodsCategoryMapper.getByTwoCategory(parentId);
    }

    @Override
    public boolean updateCategory(GoodsCategory goodsCategory) throws Exception {
        boolean flag = false;
        if(goodsCategoryMapper.updateCategory(goodsCategory)>0){
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean deleteCategory(Integer id) throws Exception {
        boolean flag = false;
        if(goodsCategoryMapper.deleteCategory(id)>0){
            flag = true;
        }
        return flag;
    }
}
