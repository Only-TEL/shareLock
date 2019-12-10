package com.xihua.server.netty;

import com.xihua.base.LockDecoder;
import com.xihua.base.LockEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class LockServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOG = LoggerFactory.getLogger(LockServerInitializer.class);
    @Autowired
    private LockServerHandler nettyServerHandler;

    // 绑定handler
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 配置解码和编码器
        // 以 \n 为结尾分割的解码器
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // 8kb
        // ByteBuf delimiter = Unpooled.copiedBuffer("_$_".getBytes());
        // pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, delimiter));
        // 协议的解码和编码器
        pipeline.addLast("decoder", new LockDecoder());
        pipeline.addLast("encoder", new LockEncoder());
        // 自定义的Handler
        pipeline.addLast("handler", nettyServerHandler);
        LOG.info(" {} initChannel remote ip: {}", Thread.currentThread().getName(), socketChannel.remoteAddress());
    }


}

