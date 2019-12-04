package com.xihua.aspect;

import com.xihua.base.BaseEntity;
import com.xihua.bean.SysUser;
import com.xihua.utils.ServletUtil;
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
        // 获取操作对象
        SysUser currentUser = null;
        try {
            currentUser = ServletUtil.getOnlineUser();
            if (currentUser == null) {
                currentUser = new SysUser();
                currentUser.setUserName("anonymous");
            }
        } catch (Exception ex) {
            currentUser = new SysUser();
            currentUser.setUserName("anonymous");
        }
        // 获取参数
        Object[] args = point.getArgs();
        Date now = new Date();
        for (int i = 0, len = args.length; i < len; i++) {
            Object arg = args[i];
            // 判断参数类型
            if (arg instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) arg;
                // 设置属性
                this.setSaveOrUpdateInfo(baseEntity, currentUser.getUserName(), now);
            }
            if (arg instanceof Collection) {
                Collection c = (Collection) arg;
                if (c != null) {
                    Iterator it = c.iterator();
                    while (it.hasNext()) {
                        Object next = it.next();
                        if (next instanceof BaseEntity) {
                            BaseEntity baseEntity = (BaseEntity) next;
                            this.setSaveOrUpdateInfo(baseEntity, currentUser.getUserName(), now);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }


    /**
     * 为BaseEntity对象设置切面值
     * @param baseEntity
     */
    private void setSaveOrUpdateInfo(BaseEntity baseEntity, String userName,Date now) {

        if (StringUtils.isEmpty(baseEntity.getCreateBy())) {
            baseEntity.setCreateTime(now);
            baseEntity.setCreateBy(userName);
        }
        baseEntity.setUpdateBy(userName);
        baseEntity.setUpdateTime(now);
    }


}
