package com.xihua.base;

/**
 * 自定义协议头信息
 */
public class LockHeader {

    /** 协议类型 */
    private String type;
    /** 区域代码+单车编号 */
    private String sessionId;
    /** 消息内容长度 */
    private int contentLength;

    public LockHeader(String type, int contentLength, String sessionId) {
        this.type = type;
        this.contentLength = contentLength;
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return "LockHeader{" +
                "type='" + type + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", contentLength=" + contentLength +
                '}';
    }
}
