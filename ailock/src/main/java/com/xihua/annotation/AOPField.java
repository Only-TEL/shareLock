package com.xihua.annotation;

import java.lang.annotation.*;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AOPField {
    /**
     * 对应的数据库字段
     *
     * @return
     */
    String dbField() default "";

    String description() default "";

    boolean isId() default false;

    boolean isParentId() default false;

    boolean isNeedRecord() default true;

}
