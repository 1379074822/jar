package com.mall.service;

import com.mall.pojo.DataDictionary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: 数据字典服务接口
 */
@Service
public interface DataDictionaryService {

    /**
     * 获取管理员角色名
     * @param id
     * @return
     */
    String getAdminRoleName(Integer id);

    /**
     * 获取数据字典
     * @param typeCode
     * @return
     */
    List<DataDictionary> getDataDictionaryList(String typeCode)throws Exception;
}
