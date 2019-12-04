package com.xihua.base;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T, ID extends Serializable> {
    void setBaseMapper();

    T selectById(ID id);

    List<T> selectList(T t);

    int insert(T t);

    int update(T t);

    int deleteById(ID id);

    int deleteByIds(ID[] ids);

    int insertBatch(List<T> list);
}
