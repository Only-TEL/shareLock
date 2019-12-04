package com.xihua.manager;


import java.util.TimerTask;


public class AsyncFactory {

    // 发送开锁指令
    public static TimerTask syncSendOpen(final String backId){

        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("发送socket信号");
            }
        };
    }

}
