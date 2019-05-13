package com.example.mydemo.common.service.impl;


import com.example.mydemo.common.mapper.BaseMysqlMapper;
import com.example.mydemo.common.pojo.DataEntity;
import com.example.mydemo.common.service.BaseService;
import com.example.mydemo.common.util.CopyUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.example.mydemo.common.util.ServiceMapUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public abstract class BaseServiceImpl<T extends DataEntity>  implements BaseService<T> {

    @Autowired
    private BaseMysqlMapper<T> mapper;

    private SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return this.sqlSession;
    }

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void deleteById(Integer id) throws Exception{
        Map<String, Object> params = ServiceMapUtil.getMap();
        params.put("sid", id);
        mapper.deleteByMap(params);
    }

    @Override
    public Page<T> selectPage(int pageNum, int pageSize, Map<String, Object> params) throws Exception{
        params = ServiceMapUtil.getMap(params);
        Page<T> page = PageHelper.startPage(pageNum,pageSize,true);
        mapper.selectByMap(params);
        return page;
    }

    @Override
    public List<T> selectAll() throws Exception{
        Map<String, Object> params =ServiceMapUtil.getMap();
        return mapper.selectByMap(params);
    }

    @Override
    public List<T> selectList(T record) throws Exception{

        return mapper.selectByMap(CopyUtil.copyBeanNotNullToMap(record));
    }

    @Override
    public List<T> selectByMap(Map<String, Object> params) throws Exception{
        params = ServiceMapUtil.getMap(params);
        return mapper.selectByMap(params);
    }

    @Override
    public T selectById(Integer id) throws Exception{
        Map<String, Object> params = ServiceMapUtil.getMap();
        params.put("sid", id);
        List<T> maps=  mapper.selectByMap(params);
        if(maps  != null && maps.size() ==1){
            return maps.get(0);
        }else{
            return null;
        }

    }

    @Override
    public T selectOne(T record) {
        List<T> maps=  mapper.selectByMap(CopyUtil.copyBeanNotNullToMap(record));
        if(maps  != null && maps.size() == 1){
            return maps.get(0);
        }else{
            return null;
        }
    }

    @Override
    public T selectUnineByMap(Map<String, Object> params) throws Exception {
        params = ServiceMapUtil.getMap(params);
        List<T> results = mapper.selectByMap(params);
        if(results.size() ==  0) {
            return null;
        }else if(results.size() == 1){
            return results.get(0);
        }else{
            throw new Exception("查询异常,结果集应为1条,现找到"+results.size()+"条");
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void save(T entity) throws Exception{
        if (entity.getSid() != null) {
            mapper.update(entity);
        } else {
            mapper.insert(entity);
        }
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public void saveSelective(T entity) throws Exception{
        if (entity.getSid() != null) {
            mapper.update(entity);
        } else {
            mapper.insert(entity);
        }
    }



    @Override
    @Transactional(rollbackFor=Exception.class)
    public int insertList(List<T> records) throws Exception{
        Map<String, Object> params = ServiceMapUtil.getMap();
        params.put("list", records);
        return mapper.insertList(params);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int deleteByParams(Map<String, Object> params) throws Exception{
        params = ServiceMapUtil.getMap(params);
        return mapper.deleteByMap(params);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int insert(T entity) throws Exception {
        return mapper.insert(entity);
    }



    @Override
    @Transactional(rollbackFor=Exception.class)
    public int update(T entity) throws Exception {
        return mapper.update(entity);
    }



    @Override
    @Transactional(rollbackFor=Exception.class)
    public int updateByMap(Map<String, Object> params) throws Exception {
        params = ServiceMapUtil.getMap(params);
        return mapper.updateByMap(params);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int updateAll(String statement,Collection collection) {
        try {
            Configuration conn = getSqlSession().getConfiguration();
            ManagedTransactionFactory managedTransactionFactory = new ManagedTransactionFactory();
            BatchExecutor batchExecutor = new BatchExecutor(conn,
                    managedTransactionFactory.newTransaction(getSqlSession().getConnection()));
            int i = 0;
            List lrs = new ArrayList();
            for (Iterator localIterator1 = collection.iterator() ; localIterator1.hasNext();) {
                Object entity = localIterator1.next();
                batchExecutor.doUpdate(conn.getMappedStatement(statement), entity);
                if ((i++ > 0) && (i % this.batchSize.intValue() == 0)) {
                    List b1 = batchExecutor.doFlushStatements(false);
                    lrs.addAll(b1);
                }
            }
            List b2 = batchExecutor.doFlushStatements(false);
            lrs.addAll(b2);

            int counts = 0;
            if (CollectionUtils.isNotEmpty(lrs)) {
                for (Iterator localIterator2 = lrs.iterator(); localIterator2.hasNext();) {
                    BatchResult br = (BatchResult) localIterator2.next();
                    counts += br.getUpdateCounts().length;
                }

            }
            return counts;
        } catch (Exception de) {
            throw new RuntimeException(de);
        }
    }





}
