package com.xihua.service.impl;

import com.xihua.constants.Constants;
import com.xihua.exception.BusinessException;
import com.xihua.utils.Convert;
import com.xihua.utils.EncryptUtil;
import com.xihua.utils.ZCommonUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xihua.mapper.SysUserMapper;
import com.xihua.bean.SysUser;
import com.xihua.service.ISysUserService;
import com.xihua.base.AbstractService;
import com.xihua.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Objects;

/**
 * 用户 服务层实现
 *
 * @author admin
 * @date 2019-12-04
 */
@Service
public class SysUserServiceImpl extends AbstractService<SysUser,Integer> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    public void setBaseMapper(){
        super.setBaseMapper(sysUserMapper);
    }

    /**
     * 新增用户
     * @param sysUser 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int saveOrUpdateSysUser(SysUser sysUser) {
        //新记录标记
        boolean isNewRecord = false;
        if (StringUtils.isEmpty(sysUser.getUserId())){
            isNewRecord = true;
            sysUserService.insert(sysUser);
        } else{
            sysUserService.update(sysUser);
        }
        return 1;
    }

    @Override
    public boolean register(SysUser sysUser) {
        if(!checkSomeInfo(sysUser)){
            return false;
        }
        // 加密注册
        encryptPwd(sysUser);
        String operationUserName = sysUser.getUserName();
        Date now = new Date();
        sysUser.setCreateBy(operationUserName);
        sysUser.setCreateTime(now);
        sysUser.setUpdateBy(operationUserName);
        sysUser.setUpdateTime(now);
        int row = sysUserService.saveOrUpdateSysUser(sysUser);
        return row > 0;
    }

    private boolean checkSomeInfo(SysUser sysUser){
        if (!Convert.isLoginName(sysUser.getUserName())) {
            throw new BusinessException("用户名不合法，请重试");
        }
        if(sysUserMapper.checkLoginNameUnique(sysUser.getUserName()) > 0){
            throw new BusinessException("用户名已被注册，请重试");
        }
        if(StringUtils.isNotEmpty(sysUser.getPhone())){
            if (!Convert.isPhone(sysUser.getPhone())) {
                throw new BusinessException("手机号不合法，请重试");
            }
            if(sysUserMapper.checkPhoneUnique(sysUser.getPhone()) > 0){
                throw new BusinessException("手机号已被注册，请重试");
            }
        }
        if(StringUtils.isNotEmpty(sysUser.getEmail())){
            if (!Convert.isEmail(sysUser.getEmail())) {
                throw new BusinessException("邮箱地址不合法，请重试");
            }
        }
        return true;
    }
    // 对密码进行加密  	hex(salt)+hex(sha1(password,salt))
    private void encryptPwd(SysUser sysUser) {
        // 生成盐
        String saltStr = ZCommonUtils.randomSalt();
        byte[] salt = EncryptUtil.decodeHex(saltStr);
        // 加密
        String newPassword = saltStr + EncryptUtil.encodeHex(EncryptUtil.sha1(sysUser.getPassword().getBytes(), salt, Constants.DEFAULT_ITERATIONS));
        // 设置盐和密码
        sysUser.setSalt(saltStr);
        sysUser.setPassword(newPassword);
    }

    @Override
    public SysUser login(String userName, String password) {
        SysUser sysUser = null;
        if (Convert.isLoginName(userName)) {
            sysUser = sysUserService.selectUserByLoginName(userName);
        } else if (Convert.isPhone(userName)) {
            sysUser = sysUserService.selectUserByPhoneNumber(userName);
        }
        if (Objects.isNull(sysUser)) {
            throw new BusinessException("登录用户不存在");
        } else if ("2".equals(sysUser.getDelFlag())) {
            throw new BusinessException("登录用户错误");
        }
        boolean checkRs = sysUserService.validate(sysUser, password);
        if(!checkRs) throw new BusinessException("账号或密码输入错误");
        return sysUser;
    }

    @Override
    public SysUser selectUserByLoginName(String userName) {
        return sysUserMapper.selectUserByLoginName(userName);
    }

    @Override
    public SysUser selectUserByPhoneNumber(String phoneNumber) {
        return sysUserMapper.selectUserByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean validate(SysUser realUser, String password) {
        // 1.获取用户信息的中盐
        String saltString = realUser.getSalt();
        // 2.逆转salt
        byte[] salt = EncryptUtil.decodeHex(saltString);
        // 3.与指定的盐加密后与数据库中的加密密码比较
        String newPwd = saltString + EncryptUtil.encodeHex(EncryptUtil.sha1(password.getBytes(), salt, Constants.DEFAULT_ITERATIONS));
        if(newPwd.equals(realUser.getPassword())){
            return true;
        };
        return false;
    }
}