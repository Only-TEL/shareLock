package com.xihua.base;

import com.xihua.constants.Constants;

public class LockMessage {

    private LockHeader header;
    private String body;

    public LockMessage(LockHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    public LockHeader getHead() {
        return header;
    }

    public static LockMessage success(String sessionId, String body) {
        LockHeader header = new LockHeader(Constants.OK_PREFIX, body.length(), sessionId);
        return new LockMessage(header, body);
    }

    public static LockMessage success(String sessionId) {
        LockHeader header = new LockHeader(Constants.OK_PREFIX, 7, sessionId);
        return new LockMessage(header, "success");
    }

    public static LockMessage fail(String sessionId, String body) {
        LockHeader header = new LockHeader(Constants.FAIL_PREFIX, body.length(), sessionId);
        return new LockMessage(header, body);
    }
    public static LockMessage openMessage(String sessionId, String body) {
        LockHeader header = new LockHeader(Constants.OPEN_PREFIX, body.length(), sessionId);
        return new LockMessage(header, body);
    }

    public static LockMessage openMessage(String sessionId) {
        LockHeader header = new LockHeader(Constants.OPEN_PREFIX, 4, sessionId);
        return new LockMessage(header, Constants.OPEN_PREFIX);
    }

    public void setHead(LockHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "LockMessage{" +
                "header=" + header +
                ", body='" + body + '\'' +
                '}';
    }
}
