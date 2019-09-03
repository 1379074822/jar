package com.mall.mapper;

import com.mall.pojo.GoodsCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 商品分类接口
 */
@Repository
public interface GoodsCategoryMapper {
    /**
     * 根据父Id获得获取相应的分类列表
     * @param parentId  商品分类父类id
     * @return
     * @throws Exception
     */
    List<GoodsCategory> getCategoryListByParentId(@Param("parentId")Integer parentId)throws Exception;

    /**
     * 后台管理分类列表显示
     * @param parentId  商品分类父类id
     * @param currentPageNo  当前页面
     * @param pageSize   页面大小
     * @return
     * @throws Exception
     */
    List<GoodsCategory> getCategoryList(@Param("parentId")Integer parentId,
                                        @Param("from") Integer currentPageNo,
                                        @Param("pageSize") Integer pageSize)throws Exception;

    /**
     * 查询分类数量
     * @param parentId  商品分类父类id
     * @return
     * @throws Exception
     */
    int getCategoryCount(@Param("parentId")Integer parentId)throws Exception;

    /**
     * 添加分类
     * @param goodsCategory  商品分类实体类
     * @return
     * @throws Exception
     */
    int insert(GoodsCategory goodsCategory)throws Exception;

    /**
     * 管理员获取商品分类
     * @param id  商品分类id
     * @return
     * @throws Exception
     */
    GoodsCategory getCategory(@Param("id") Integer id)throws Exception;

    /**
     * 根据父类id查询二级分类
     * @param parentId  父节点id
     * @return
     * @throws Exception
     */
    GoodsCategory getByTwoCategory(@Param("parentId") Integer parentId)throws Exception;

    /**
     * 更新商品分类
     * @param goodsCategory  商品分类实体类
     * @return
     * @throws Exception
     */
    int updateCategory(GoodsCategory goodsCategory)throws Exception;

    /**
     * 删除商品分类
     * @param id
     * @return
     * @throws Exception
     */
    int deleteCategory(@Param("id") Integer id)throws Exception;
}
