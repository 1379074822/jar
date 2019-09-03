package com.mall.service;

import com.mall.pojo.GoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品分类服务接口
 */
public interface GoodsCategoryService {
    /**
     * 根据父节点parentId获取相应的分类列表
     * @param parentId
     * @return
     * @throws Exception
     */
    List<GoodsCategory> getCategoryListByParentId(Integer parentId)throws Exception;

    /**
     * 后台分页显示分类列表
     * @param parentId
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<GoodsCategory> getCategoryList(Integer parentId,Integer currentPageNo, Integer pageSize)throws Exception;

    /**
     * 获取各级分类数量
     * @param parentId  商品分类父类id
     * @return
     * @throws Exception
     */
    int getCategoryCount(Integer parentId)throws Exception;

    /**
     * 添加商品分类
     * @param goodsCategory  商品分类实体类
     * @return
     * @throws Exception
     */
    boolean insert(GoodsCategory goodsCategory)throws Exception;

    /**
     * 查看商品分类信息
     * @param id
     * @return
     * @throws Exception
     */
    GoodsCategory getCategory(Integer id)throws Exception;

    /**
     * 根据父节点查询二级id
     * @param parentId
     * @return
     * @throws Exception
     */
    GoodsCategory getByParentId(Integer parentId)throws Exception;
    /**
     * 更新商品分类实体类
     * @param goodsCategory  商品分类实体类
     * @return
     * @throws Exception
     */
    boolean updateCategory(GoodsCategory goodsCategory)throws Exception;

    /**
     * 删除商品分类失败
     * @param id 商品分类id
     * @return
     * @throws Exception
     */
    boolean deleteCategory(Integer id)throws Exception;
}
