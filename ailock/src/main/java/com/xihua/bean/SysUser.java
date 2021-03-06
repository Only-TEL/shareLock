package com.xihua.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xihua.base.BaseEntity;
import com.xihua.annotation.AOPField;

import java.util.Date;

/**
 * 用户表 sys_user
 *
 * @author admin
 * @date 2019-12-04
 */
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;
    /**
     *  验证码
     */
    private String captcha;
    /**
     * 盐
     */
    private String salt;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 性别
     */
    private String sex;
    /**
     * 生日
     */
    private Date birthday;


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("password", getPassword())
                .append("salt", getSalt())
                .append("email", getEmail())
                .append("phone", getPhone())
                .append("sex", getSex())
                .append("birthday", getBirthday())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .toString();
    }
}