package com.xihua.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xihua.base.BaseEntity;
import com.xihua.annotation.AOPField;

import java.util.Date;

/**
 * 行车记录表 back_recod
 *
 * @author admin
 * @date 2019-12-04
 */
public class BackRecod extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 单车id
     */
    private Integer backId;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 单价，单位是分
     */
    private Integer price;
    /**
     * 总计，单位是分
     */
    private Integer tPrice;
    /**
     * 单车状态，0-正在使用，1-已关锁
     */
    private String status;
    /**
     * 行驶距离，单位厘米
     */
    private Integer distance;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setBackId(Integer backId) {
        this.backId = backId;
    }

    public Integer getBackId() {
        return backId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setTPrice(Integer tPrice) {
        this.tPrice = tPrice;
    }

    public Integer getTPrice() {
        return tPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("backId", getBackId())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("price", getPrice())
                .append("tPrice", getTPrice())
                .append("status", getStatus())
                .append("distance", getDistance())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("delFlag", getDelFlag())
                .append("remark", getRemark())
                .toString();
    }
}