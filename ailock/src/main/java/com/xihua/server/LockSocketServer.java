package com.xihua.server;

import com.xihua.constants.Constants;
import com.xihua.manager.AsyncFactory;
import com.xihua.manager.AsyncManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nio socket 服务器
 */
public class LockSocketServer {

    // 解码buffer
    private Charset cs = Charset.forName("ASCII");
    // 接受数据缓冲区
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    // 发送数据缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    // 监听器
    private static Selector selector;
    // 还车阻塞队列
    public static ArrayBlockingQueue<String> backQueue = new ArrayBlockingQueue<>(40);
    // 打日志
    private static Logger log = LoggerFactory.getLogger(LockSocketServer.class);
    // 缓存channel
    public static ConcurrentHashMap<String, SocketChannel> channelCache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        new LockSocketServer().startSocketServer(8088);
    }

    // 开启socket服务
    public void startSocketServer(int port) {
        try {
            // 打开通信信道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 获取套接字
            ServerSocket serverSocket = serverSocketChannel.socket();
            // 绑定端口号
            serverSocket.bind(new InetSocketAddress(port));
            // 打开监听器
            selector = Selector.open();
            // 将通信信道注册到监听器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 监听器会一直监听，如果客户端有请求就会进入相应的事件处理
            System.out.println("Socket server 启动完成");
            while (true) {
                // select方法会一直阻塞直到有相关事件发生或超时
                selector.select();
                // 监听到的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    handle(key);
                }
                // 清除处理过的事件
                selectionKeys.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理事件
    private void handle(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        String requestMsg = "";
        int count = 0;
        if (selectionKey.isAcceptable()) {
            // 每有客户端连接，即注册通信信道为可读
            serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            rBuffer.clear();
            count = socketChannel.read(rBuffer);
            // 读取数据
            if (count > 0) {
                rBuffer.flip();
                requestMsg = String.valueOf(cs.decode(rBuffer).array());
            }
            log.info("Received message from client: " + requestMsg);
            // 判断是不是关锁信号 -> 接受关锁信号  -> 异步关锁backid
            if (requestMsg.startsWith(Constants.STOP_PREFIX)) {
                String backId = requestMsg.substring(Constants.STOP_PREFIX.length());
                AsyncManager.asyncManager().execute(AsyncFactory.syncSendStop(backId));
                // 返回数据
                String responseMsg = "success";
                sBuffer = ByteBuffer.allocate(responseMsg.getBytes("ASCII").length);
                sBuffer.put(responseMsg.getBytes("ASCII"));
                sBuffer.flip();
                socketChannel.write(sBuffer);
            } else if (requestMsg.startsWith(Constants.BIND_PREFIX)) {
                String backId = requestMsg.substring(Constants.BIND_PREFIX.length());
                SocketChannel value = channelCache.putIfAbsent(backId, socketChannel);
                if (value == null) {
                    log.info("将编号：{}单车加入缓存", backId);
                }
            }
            socketChannel.close();
        } else if (selectionKey.isWritable()) {
            // 找到backId对应的socketChannel 写数据
            socketChannel = (SocketChannel) selectionKey.channel();
            // 向每一个机器写入开锁信号 内容就是需要关锁的 backId
            String backId = backQueue.poll();
            if (backId != null && channelCache.containsKey(backId)) {
                SocketChannel backChannel = channelCache.get(backId);
                backId += Constants.OPEN_PREFIX + backId;
                log.info("send messge to client: {}", backId);
                sBuffer = ByteBuffer.allocate(backId.getBytes("ASCII").length);
                sBuffer.flip();
                backChannel.write(sBuffer);
            } else if (backId != null) {
                log.error("放弃向编号为{}发送开锁信号", backId);
            }
            socketChannel.close();
        }
    }

}
