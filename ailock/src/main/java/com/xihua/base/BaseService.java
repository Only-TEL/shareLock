package com.xihua.base;


import java.io.Serializable;
import java.util.List;

public interface BaseService<T, ID extends Serializable> {

    T selectById(ID id);

    List<T> selectList(T t);

    int insert(T t);

    int update(T t);

    int deleteById(ID id);

    int deleteByIds(ID[] ids);

    int deleteByIds(List<ID> list);

    int insertBatch(List<T> list);

}
