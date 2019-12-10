package com.xihua.constants;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class OpcType {

    public final static Map<Integer, String> cmd2String = new HashMap<>();

    public final static int openCmd = ByteBuffer.wrap("open".getBytes()).getInt();
    public final static int stopCmd = ByteBuffer.wrap("stop".getBytes()).getInt();
    public final static int bindCmd = ByteBuffer.wrap("bind".getBytes()).getInt();
    public final static int pingCmd = ByteBuffer.wrap("ping".getBytes()).getInt();
    public final static int okCmd = ByteBuffer.wrap("okxx".getBytes()).getInt();
    public final static int failCmd = ByteBuffer.wrap("fail".getBytes()).getInt();

    static {
        cmd2String.put(openCmd, "open");
        cmd2String.put(stopCmd, "stop");
        cmd2String.put(bindCmd, "bind");
        cmd2String.put(pingCmd, "ping");
        cmd2String.put(okCmd, "okxx");
        cmd2String.put(failCmd, "fail");
    }

    public static boolean isKownType(int code){
        return cmd2String.containsKey(code);
    }


}
