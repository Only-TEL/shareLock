package com.xihua.utils;

import com.xihua.constants.Constants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Set;

/**
 * @description: 转换工具类
 * @author: admin
 */
public class Convert {
    public Convert() {
    }

    // 判断是不是电话格式
    public static boolean isPhone(String str) {
        return Constants.PHONE_REGX.matcher(str).matches();
    }

    // 判断是不是邮箱格式
    public static boolean isEmail(String str) {
        return Constants.EMAIL_REGX.matcher(str).matches();
    }

    // 判断是不是纯数字和字母
    public static boolean isUserName(String str) {
        return Constants.USERNAME_REGX1.matcher(str).matches();
    }

    // 判断是不是数字字母和下划线格式
    public static boolean isLoginName(String str) {
        return Constants.USERNAME_REGX2.matcher(str).matches();
    }


}
