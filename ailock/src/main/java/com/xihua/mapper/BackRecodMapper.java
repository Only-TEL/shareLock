package com.xihua.mapper;

import com.xihua.bean.BackRecod;
import java.util.List;
import com.xihua.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 行车记录 数据层
 *
 * @author admin
 * @date 2019-12-04
 */
public interface BackRecodMapper extends BaseMapper<BackRecod, Integer> {


    BackRecod selectByRunningRecord(@Param("userId") Integer userId,@Param("backId") String backId);

    List<BackRecod> selectAllStopRecord(@Param("userId") Integer userId);

    BackRecod selectSingleRunningRecord(@Param("userId") Integer userId);

    BackRecod selectSingleRunningRecordByBack(@Param("backId") String backId);
}