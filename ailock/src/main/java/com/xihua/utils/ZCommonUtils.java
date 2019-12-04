package com.xihua.utils;

import com.xihua.constants.Constants;

import java.util.Random;

public class ZCommonUtils {

    // 生成4为随机验证码
    public static String buildCaptcha() {
        String captcha = new String();
        for (int i = 0; i < 4; i++) {
            char ch = Constants.CAPTCHA_SEQUENCE[new Random().nextInt(Constants.CAPTCHA_SEQUENCE.length)];
            captcha += ch;
        }
        return captcha;
    }
    // 生成随机盐
    public static String randomSalt() {
        byte[] salt = EncryptUtil.generateSalt(Constants.SALT_SIZE);
        return EncryptUtil.encodeHex(salt);
    }
}
