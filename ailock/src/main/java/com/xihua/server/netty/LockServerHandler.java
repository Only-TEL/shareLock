package com.xihua.server.netty;

import com.xihua.base.LockMessage;
import com.xihua.constants.Constants;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ctx.channel().id().asLongText()
 * ctx.channel().id().asShortText()
 */
@Component
@Sharable
public class LockServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(LockServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String channelText = ctx.channel().id().asLongText();
        LOG.info("持久化channel  key : {}", channelText);
        LockServer.channelMap.put(channelText, ctx.channel());
        super.channelActive(ctx);
    }

    // 服务端获取请求的时候被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("接收到远程客户端" + ctx.channel().remoteAddress() + "的消息：" + msg);
        // 调用处理消息的逻辑
        LockMessage lockMessage = (LockMessage) msg;
        String opcType = lockMessage.getHead().getType();
        String backId = lockMessage.getHead().getSessionId();
        String channelId = ctx.channel().id().asLongText();
        if (opcType.equals(Constants.STOP_PREFIX)) {
            // 处理停车逻辑
            LOG.info("停车，单车编号 backId {}", backId);
            //AsyncManager.asyncManager().execute(AsyncFactory.syncSendStop(backId));
        } else if (opcType.equals(Constants.BIND_PREFIX)) {
            // 绑定单车id和channelId
            LOG.info("绑定单车编号和channel，backId {},channelId {}", backId, channelId);
            LockServer.backMap.put(backId, channelId);
        }
        LockMessage result = LockMessage.success(backId);
        // 会写返回消息
        ctx.write(result);
    }

    // channelRead方法完成之后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // flush之后才是真的发送了
        ctx.flush();
        LOG.info("netty服务端响应成功 客户端 {}", ctx.channel().remoteAddress());
    }

    // 发生异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error("服务端捕获 {} 客户端出现异常，关闭ChannelHandlerContext", ctx.channel().remoteAddress());
        cause.printStackTrace();
        ctx.close();
    }
}
