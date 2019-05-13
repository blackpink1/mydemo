package com.example.mydemo.common.mapper;

import com.example.mydemo.common.pojo.DataEntity;

import java.util.List;
import java.util.Map;

public interface BaseMysqlMapper<T extends DataEntity>  {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错

    /**
     * 增加自定义接口,考虑到使用map传值的情况，原有单表查询限制配置表较麻烦，还是使用xml配置比较方便
     * 所以在每个mapper.xml方法增加一个接口实现selectMap
     * @param selectMap
     * @return
     */
    List<T> selectByMap(Map<String, Object> selectMap);




    int insert(T entity);

    /**
     *
     * @param map  里面连个参数，一个schema ，一个 List<T> records
     * @return
     */
    int insertList(Map<String, Object> map);

    int update(T entity);

    int updateList(List<T> records);


    int updateByMap(Map<String, Object> params);


    int delete(T entity);

    int deleteByMap(Map<String, Object> params);

}
