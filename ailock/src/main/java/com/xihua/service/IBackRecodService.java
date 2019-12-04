package com.xihua.service;

import com.xihua.bean.BackRecod;
import com.xihua.base.BaseService;
import java.util.List;

/**
 * 行车记录 服务层
 *
 * @author admin
 * @date 2019-12-04
 */
public interface IBackRecodService extends BaseService<BackRecod, Integer> {

    /**
     * 保存或更新
     * @return 结果
     */
    public int saveOrUpdateBackRecod(BackRecod backRecod);

}