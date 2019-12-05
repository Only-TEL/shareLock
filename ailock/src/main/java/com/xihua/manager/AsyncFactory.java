package com.xihua.manager;


import com.xihua.server.LockSocketServer;
import com.xihua.service.IBackRecodService;
import com.xihua.utils.SpringUtils;

import java.util.TimerTask;


public class AsyncFactory {

    // 将需要开锁的单车编号放入阻塞队列
    public static TimerTask syncSendOpen(final String backId) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LockSocketServer.backQueue.put(backId);
                } catch (InterruptedException e) {
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
