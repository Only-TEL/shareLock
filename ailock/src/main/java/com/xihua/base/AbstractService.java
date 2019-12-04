package com.xihua.base;

import com.xihua.annotation.SaveOrUpdate;
import com.xihua.utils.CollectionUtil;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractService<T, ID extends Serializable> implements BaseService<T, ID> {

    private BaseMapper<T, ID> baseMapper;

    private int maxJdbcParaNums = 2100;

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    public T selectById(ID id) {
        return this.baseMapper.selectById(id);
    }

    public List<T> selectList(T entity) {
        return this.baseMapper.selectList(entity);
    }

    public int deleteById(ID id) {
        return this.baseMapper.deleteById(id);
    }

    public int deleteByIds(ID[] ids) {
        return this.baseMapper.deleteByIds(ids);
    }


    @SaveOrUpdate
    @Transactional
    public int insert(T entity) {
        return this.baseMapper.insert(entity);
    }

    @SaveOrUpdate
    @Transactional
    public int update(T entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity be = (BaseEntity)entity;
            if (!be.isNeedUpdate()) {
                return -1;
            }
        }
        return this.baseMapper.update(entity);
    }

    @Transactional
    @SaveOrUpdate
    public int insertBatch(List<T> entitys) {
        if (entitys.size() < 1) {
            return 0;
        } else {
            T t = entitys.get(0);
            Field[] declaredFields = t.getClass().getDeclaredFields();
            int length = declaredFields.length + 6;
            int toindex = this.maxJdbcParaNums / length - 1;
            int listsize = entitys.size();
            if (listsize < toindex) {
                return this.baseMapper.insertBatch(entitys);
            } else {
                List<List> lists = CollectionUtil.groupList(entitys, toindex);

                for(int i = 0; i < lists.size(); ++i) {
                    List list = (List)lists.get(i);
                    this.baseMapper.insertBatch(list);
                }

                return entitys.size();
            }
        }
    }

    @Transactional
    public int deleteByIds(List<ID> ids) {
        if (ids != null && ids.size() > 0) {
            Serializable id = (Serializable)ids.get(0);
            ID[] arr;
            if (id instanceof Integer) {
                arr = (ID[])((Serializable[])ids.toArray(new Integer[ids.size()]));
                return this.baseMapper.deleteByIds(arr);
            } else if (id instanceof Long) {
                arr = (ID[])((Serializable[])ids.toArray(new Long[ids.size()]));
                return this.baseMapper.deleteByIds(arr);
            } else if (id instanceof String) {
                arr = (ID[])((Serializable[])ids.toArray(new String[ids.size()]));
                return this.baseMapper.deleteByIds(arr);
            } else {
                arr = (ID[])((Serializable[])ids.toArray(new String[ids.size()]));
                return this.baseMapper.deleteByIds(arr);
            }
        } else {
            return 0;
        }
    }
}
