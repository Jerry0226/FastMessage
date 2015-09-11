package com.jerry.socket.nio.read;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MessageReadThreadPool {

    /** 线程池 */
    private static ThreadPoolExecutor pool;

    static {
        pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2 + 1);
    }

    /** 线程池实例化对象 */
    private static MessageReadThreadPool socketPool = new MessageReadThreadPool();

    /**
     * 获取线程池对象实例
     * 
     * @return SocketThreadPool
     */
    public static MessageReadThreadPool getInstance() {
        if (socketPool == null) {
            socketPool = new MessageReadThreadPool();
        }
        return socketPool;
    }

    /**
     * 发起任务
     * @param task 任务
     */
    public void startReadTask(Runnable readTask) {
        pool.execute(readTask);
    }
    
}
