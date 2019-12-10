package com.xihua.utils;

import com.xihua.constants.Constants;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

public class ZCommonUtils {

    // 生成4为随机验证码
    public static String buildCaptcha() {
        String captcha = new String();
        for (int i = 0; i < Constants.CAPTCHA_LENGTH; i++) {
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

    // buffer转String
    public static String buildString(ByteBuffer buffer, Charset charset) {
        int ps = buffer.position();
        byte[] bs = new byte[ps];
        for (int i = 0; i < ps; i++) {
            bs[i] = buffer.get(i);
        }
        return new String(bs, charset);
    }

    public static String decode2String(ByteBuf buf, byte[] bytes, Charset charset) {
        buf.readBytes(bytes);
        return new String(bytes, charset);
    }

    public static int decode2Int(ByteBuf buf, byte[] bytes, Charset charset) {
        buf.readBytes(bytes);
        return Integer.parseInt(new String(bytes, charset));
    }
}
