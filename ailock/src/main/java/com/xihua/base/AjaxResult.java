package com.xihua.base;

import java.util.HashMap;

public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public AjaxResult() {
    }

    public static AjaxResult error() {
        return error(1, "操作失败");
    }

    public static AjaxResult error(String msg) {
        return error(500, msg);
    }

    public static AjaxResult error(int code, String msg) {
        AjaxResult json = new AjaxResult();
        json.put((String) "code", code);
        json.put((String) "msg", msg);
        return json;
    }

    public static AjaxResult success(String msg) {
        AjaxResult json = new AjaxResult();
        json.put((String) "msg", msg);
        json.put((String) "code", 0);
        return json;
    }

    public static AjaxResult success(Object data) {
        AjaxResult json = new AjaxResult();
        json.put((String) "msg", "success");
        json.put((String) "code", 0);
        json.put((String) "data", data);
        return json;
    }

    public static AjaxResult success(int code, String msg) {
        AjaxResult json = new AjaxResult();
        json.put((String) "msg", msg);
        json.put((String) "code", code);
        return json;
    }

    public static AjaxResult success() {
        return success("success");
    }

    public AjaxResult mPut(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public boolean isSuccess() {
        Object code = this.get("code");
        if (this.get("code") != null) {
            return 0 == (Integer) code;
        } else {
            return false;
        }
    }

    public AjaxResult setData(Object data) {
        AjaxResult result = success();
        result.mPut("data", data);
        return result;
    }

    public boolean isError() {
        return !this.isSuccess();
    }
}
