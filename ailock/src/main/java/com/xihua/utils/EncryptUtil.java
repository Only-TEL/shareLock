package com.xihua.utils;

import com.alibaba.druid.sql.visitor.functions.Hex;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 */
public class EncryptUtil {

    public static final String DEFAULT_URL_ENCODING = "UTF-8";
    public static final String SHA1 = "SHA-1";
    public static final String MD5 = "MD5";
    public static final String AES = "AES";

    private static SecureRandom random = new SecureRandom();
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);


    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String urlDecode(String part) {
        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对输入字符串进行md5散列.
     */
    public static byte[] md5(byte[] input) {
        return digest(input, MD5, null, 1);
    }

    public static byte[] md5(byte[] input, int iterations) {
        return digest(input, MD5, null, iterations);
    }

    /**
     * 对输入字符串进行sha1散列.
     */
    public static byte[] sha1(byte[] input) {
        return digest(input, SHA1, null, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, SHA1, salt, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1, salt, iterations);
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(input);

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 对文件进行md5散列.
     */
    public static byte[] md5(InputStream input) throws IOException {
        return digest(input, MD5);
    }

    /**
     * 对文件进行sha1散列.
     */
    public static byte[] sha1(InputStream input) throws IOException {
        return digest(input, SHA1);
    }

    private static byte[] digest(InputStream input, String algorithm) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            int bufferLength = 8 * 1024;
            byte[] buffer = new byte[bufferLength];
            int read = input.read(buffer, 0, bufferLength);

            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, bufferLength);
            }

            return messageDigest.digest();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class DateUtils extends org.apache.commons.lang3.time.DateUtils {

        public static String YYYY = "yyyy";
        public static String YYYY_MM = "yyyy-MM";
        public static String YYYY_MM_DD = "yyyy-MM-dd";
        public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        private static String[] parsePatterns = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

        public DateUtils() {
        }

        public static Date getNowDate() {
            return new Date();
        }

        public static String getDate() {
            return dateTimeNow(YYYY_MM_DD);
        }

        public static final String getTime() {
            return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
        }

        public static final String dateTimeNow() {
            return dateTimeNow(YYYYMMDDHHMMSS);
        }

        public static final String dateTimeNow(String format) {
            return parseDateToStr(format, new Date());
        }

        public static final String dateTime(Date date) {
            return parseDateToStr(YYYY_MM_DD, date);
        }

        public static final String parseDateToStr(String format, Date date) {
            return (new SimpleDateFormat(format)).format(date);
        }

        public static final Date dateTime(String format, String ts) {
            try {
                return (new SimpleDateFormat(format)).parse(ts);
            } catch (ParseException var3) {
                throw new RuntimeException(var3);
            }
        }

        public static final String datePath() {
            Date now = new Date();
            return DateFormatUtils.format(now, "yyyy/MM/dd");
        }

        public static final String dateTime() {
            Date now = new Date();
            return DateFormatUtils.format(now, "yyyyMMdd");
        }

        public static Date parseDate(Object str) {
            if (str == null) {
                return null;
            } else {
                try {
                    return DateUtils.parseDate(str.toString(), parsePatterns);
                } catch (ParseException var2) {
                    return null;
                }
            }
        }

        public static Date getServerStartDate() {
            long time = ManagementFactory.getRuntimeMXBean().getStartTime();
            return new Date(time);
        }

        public static String getDatePoor(Date endDate, Date nowDate) {
            long nd = 86400000L;
            long nh = 3600000L;
            long nm = 60000L;
            long diff = endDate.getTime() - nowDate.getTime();
            long day = diff / nd;
            long hour = diff % nd / nh;
            long min = diff % nd % nh / nm;
            return day + "天" + hour + "小时" + min + "分钟";
        }
    }
}
