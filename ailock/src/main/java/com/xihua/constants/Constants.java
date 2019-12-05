package com.xihua.constants;

import java.util.regex.Pattern;

public class Constants {

    public static final String UTF8 = "UTF-8";

    public static String AUTO_REOMVE_PRE = "true";

    /* 密码加密设置默认的循环数 **/
    public static final int DEFAULT_ITERATIONS = 1024;
    /** 密码加密的salt长度 */
    public static final int SALT_SIZE = 8;

    /**自定义定义cookie名称*/
    public static final String COOKIE_NAME = "sessionId";

    /** springboot 生成的sessionId*/
    public static final String DEFAULT_COOKIE_NAME = "JSESSIONID";

    /** 自定义单价 单位角  半小时5毛 */
    public static final int PRICE_OF_HOUR = 5;

    /**定义cookie的有效时间*/
    public static final int COOKIE_MAX_AGE = 1800;

    /** 定义验证码的长度 */
    public static final int CAPTCHA_LENGTH = 4;

    /** 验证码的有效期 10 min 10*60*1000 */
    public static final long CAPTCHA_MAX_AGE = 600000;

    /** TCP开车信号标识 */
    public static final String STOP_PREFIX = "STOP";

    /** TCP停车信号标识 */
    public static final String OPEN_PREFIX = "OPEN";

    /** TCP绑定标识 */
    public static final String BIND_PREFIX = "BIND";

    /*匹配手机号*/
    public static final Pattern PHONE_REGX = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|166|17[3678]|18[0-9]|14[57])[0-9]{8}$");

    /*匹配邮箱*/
    public static final Pattern EMAIL_REGX = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /** 数字+字母 */
    public static final Pattern USERNAME_REGX2 = Pattern.compile("^[A-Za-z0-9]+$");

    /** 数字+字母+下划线 */
    public static final Pattern USERNAME_REGX1 = Pattern.compile("^[a-zA-Z0-9_]*$");

    /** 生成验证码序列 */
    public static final char[] CAPTCHA_SEQUENCE = new char[]{'0','1','2','3','4','5','6','7','8','9'};
}
