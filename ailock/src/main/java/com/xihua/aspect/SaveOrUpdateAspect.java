package com.xihua.aspect;

import com.xihua.base.BaseEntity;
import com.xihua.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@Aspect
public class SaveOrUpdateAspect {

    private static final Logger log = LoggerFactory.getLogger(SaveOrUpdateAspect.class);

    public SaveOrUpdateAspect() {
    }

    @Pointcut("@annotation(com.xihua.annotation.SaveOrUpdate)")
    public void saveOrUpdatePointCut() {
    }

    /**
     * 前置通知，设置参数的默认值
     * @param point
     */
    @Before("saveOrUpdatePointCut()")
    public void doBefore(JoinPoint point) {
        System.out.println("do before");
    }



    /**
     * 为BaseEntity对象设置切面值
     * @param baseEntity
     */
    private void setSaveOrUpdateInfo(BaseEntity baseEntity) {
        Date now = new Date();
        if (StringUtils.isEmpty(baseEntity.getCreateBy())) {
            baseEntity.setCreateTime(now);
//            baseEntity.setCreateBy();
        }
        //baseEntity.setUpdateBy();
        baseEntity.setUpdateTime(now);
    }


}
