package com.xihua.manager;


import com.xihua.base.LockMessage;
import com.xihua.exception.BusinessException;
import com.xihua.server.netty.LockServer;
import com.xihua.service.IBackRecodService;
import com.xihua.utils.SpringUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;


public class AsyncFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncFactory.class);

    // 将需要开锁的单车编号放入阻塞队列
    public static TimerTask syncSendOpen(final String backId) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if (!LockServer.backMap.containsKey(backId)) {
                        LOG.error("查询不到单车，检查单车编号{}", backId);
                        throw new BusinessException("车好像坏了");
                    }
                    LOG.info("发送开车信号，单车编号{}", backId);
                    // 获取channel
                    Channel channel = LockServer.channelMap.get(LockServer.backMap.get(backId));
                    LockMessage result = LockMessage.openMessage(backId);
                    // 开车信息号
                    channel.writeAndFlush(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // 执行关锁操作
    public static TimerTask syncSendStop(final String backId) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IBackRecodService.class).reBack(backId);
            }
        };
    }

}
