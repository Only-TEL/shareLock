package com.xihua.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LockServer {

    private static final Logger LOG = LoggerFactory.getLogger(LockServer.class);

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> backMap = new ConcurrentHashMap<>();

    @Autowired
    private LockServerInitializer serverInitializer;

    public ChannelFuture start(String hostname, int port) throws Exception {
        ChannelFuture future = null;
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(hostname, port))
                    .childHandler(serverInitializer);
            // start
            future = bootstrap.bind().sync();
            channel = future.channel();
            LOG.info("======LockServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (future != null && future.isSuccess()) {
                LOG.info("Netty server listening " + hostname + " on port " + port + " and ready for connections...");
            } else {
                LOG.error("Netty server start up Error!");
            }
        }
        return future;
    }

    /**
     * 停止服务
     */
    public void destroy() {
        LOG.info("Shutdown Netty Server...");
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        LOG.info("Shutdown Netty Server Success!");
    }
}
