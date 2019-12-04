package com.xihua.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xihua.annotation.AOPField;

import java.io.Serializable;
import java.util.*;

public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean needUpdate = true;

    @AOPField(dbField = "create_by", description = "创建者")
    private String createBy;

    @AOPField(dbField = "create_time", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @AOPField(dbField = "update_by", description = "更新者")
    private String updateBy;

    @AOPField(dbField = "update_time", description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @AOPField(dbField = "remark", description = "备注")
    private String remark;

    @AOPField(dbField = "del_flag", description = "删除标志0-不删除 1-删除")
    private String delFlag = "0";

    /** 查询条件 */
    private Map<String, Object> params;


    public Map<String, Object> getParams() {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}
