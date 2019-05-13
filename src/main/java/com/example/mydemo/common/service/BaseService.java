package com.example.mydemo.common.service;

import com.example.mydemo.common.pojo.DataEntity;
import com.github.pagehelper.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseService<T extends DataEntity> {
    final static Integer batchSize = 50;
    Page<T> selectPage(int pageNum, int pageSize, Map<String, Object> params) throws Exception;
    List<T> selectAll() throws Exception;
    List<T> selectList(T record) throws Exception;
    List<T> selectByMap(Map<String, Object> params) throws Exception;
    T selectById(Integer id) throws Exception;
    T selectOne(T record);
    T selectUnineByMap(Map<String, Object> params) throws Exception;
    int insertList(List<T> records) throws Exception;
    int insert(T entity) throws Exception;
    int update(T entity) throws Exception;
    int updateAll(String statement, Collection<T> collection);
    int updateByMap(Map<String, Object> params) throws Exception;
    void save(T entity) throws Exception;
    void saveSelective(T entity) throws Exception;
    void deleteById(Integer id) throws Exception;
    int deleteByParams(Map<String, Object> params) throws Exception;
}
