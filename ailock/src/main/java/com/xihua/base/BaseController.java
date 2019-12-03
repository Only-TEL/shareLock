package com.xihua.base;

import com.alibaba.fastjson.JSONObject;
import com.xihua.utils.ServletUtil;
import com.xihua.utils.StringUtils;


public class BaseController {

    public BaseController() {
    }

    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? this.success() : this.error();
    }

    protected AjaxResult toAjax(boolean result) {
        return result ? this.success() : this.error();
    }

    public AjaxResult success() {
        return AjaxResult.success();
    }

    public AjaxResult error() {
        return AjaxResult.error();
    }

    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    public AjaxResult error(int code, String message) {
        return AjaxResult.error(code, message);
    }




    /**
     * 设置时间查询条件
     *
     * @param baseEntity
     */
    protected void setTimeParam(BaseEntity baseEntity) {
        String startTime = ServletUtil.getParameter("startTime");
        String endTime = ServletUtil.getParameter("endTime");
        if (StringUtils.isNotEmpty(startTime)) {
            baseEntity.getParams().put("startTime" , startTime);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            baseEntity.getParams().put("endTime" , endTime);
        }
    }



    /**
     * 将json字符串转为对象
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    public Object tranObject(String jsonStr, Class clazz) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return JSONObject.toJavaObject(jsonObject, clazz);
    }
}
