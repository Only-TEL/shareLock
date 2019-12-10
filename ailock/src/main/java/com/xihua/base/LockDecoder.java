package com.xihua.base;


import com.xihua.constants.Constants;
import com.xihua.constants.OpcType;
import com.xihua.utils.ZCommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class LockDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        // 前4位是type
        int typeInt = byteBuf.readInt();
        if (!OpcType.isKownType(typeInt)) {
            throw new IllegalArgumentException("请求类型错误");
        }
        String type = OpcType.cmd2String.get(typeInt);
        // 4位是contentLen
        int contentLen = ZCommonUtils.decode2Int(byteBuf, Constants.READ_UNIT, CharsetUtil.US_ASCII);
        // 继续读backId
        byte[] backId = new byte[6];
        String sessionId = ZCommonUtils.decode2String(byteBuf, backId, CharsetUtil.US_ASCII);
        // 组装协议头
        LockHeader header = new LockHeader(type, contentLen, sessionId);
        // 最后是body
        byte[] body = new byte[byteBuf.readableBytes()];
        String msg = ZCommonUtils.decode2String(byteBuf, body, CharsetUtil.US_ASCII);
        LockMessage lockMessage = new LockMessage(header, msg);
        list.add(lockMessage);
    }
}
