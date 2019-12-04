package com.xihua.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xihua.mapper.BackRecodMapper;
import com.xihua.bean.BackRecod;
import com.xihua.service.IBackRecodService;
import com.xihua.base.AbstractService;
import com.xihua.utils.StringUtils;

/**
 * 行车记录 服务层实现
 *
 * @author admin
 * @date 2019-12-04
 */
@Service
public class BackRecodServiceImpl extends AbstractService<BackRecod,Integer> implements IBackRecodService {

    @Autowired
    private BackRecodMapper backRecodMapper;

    @Autowired
    private IBackRecodService backRecodService;

    @Autowired
    public void setBaseMapper(){
        super.setBaseMapper(backRecodMapper);
    }

    /**
     * 新增行车记录
     * @param backRecod 行车记录信息
     * @return 结果
     */
    @Override
    @Transactional
    public int saveOrUpdateBackRecod(BackRecod backRecod) {
        //新记录标记
        boolean isNewRecord = false;
        if (StringUtils.isEmpty(backRecod.getId())){
            isNewRecord = true;
                backRecodService.insert(backRecod);
        } else{
                backRecodService.update(backRecod);
        }
        return 1;
    }

}