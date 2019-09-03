package com.mall.mapper;

import com.mall.pojo.DataDictionary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 数据字典接口
 */
@Repository
public interface DataDictionaryMapper {

    /**
     * 获取数据字典
     * @param typeCode
     * @return
     */
    List<DataDictionary> getDataDictionaryList(@Param("typeCode") String typeCode)throws Exception;

    /**
     * 获取角色名
     * @param id
     * @return
     */
    String getAdminRoleName(Integer id);

}