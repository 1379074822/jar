package com.mall.service.Impl;

import com.mall.mapper.DataDictionaryMapper;
import com.mall.pojo.DataDictionary;
import com.mall.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

    /**
     * 数据字典mapper
     */
    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @java.lang.Override
    public String getAdminRoleName(Integer id) {
        return dataDictionaryMapper.getAdminRoleName(id);
    }

    @Override
    public List<DataDictionary> getDataDictionaryList(String typeCode) throws Exception {
        return dataDictionaryMapper.getDataDictionaryList(typeCode);
    }
}
