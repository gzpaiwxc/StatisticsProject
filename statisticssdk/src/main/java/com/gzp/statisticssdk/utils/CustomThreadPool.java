package com.gzp.statisticssdk.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 自定义线程池
 */

public class CustomThreadPool {

    private static final int MAX_POOL_SIZE = 3;

    /**
     * 定长线程池
     */
    public ExecutorService workerThread;

    Callable<String> callBack = new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "true";
        }
    };

    private static CustomThreadPool mInstance;

    public static CustomThreadPool getInstance() {
        if (null == mInstance) {
            synchronized (CustomThreadPool.class) {
                if (null == mInstance) {
                    mInstance = new CustomThreadPool();
                }
            }
        }
        return mInstance;
    }

    /**
     * 1.初始化一个线程池
     */
    private CustomThreadPool() {
        workerThread = Executors.newFixedThreadPool(MAX_POOL_SIZE);
    }

    /**
     * 2.开始任务
     *
     * @param runnable
     */
    public void startTask(Runnable runnable) {
        if (null != workerThread && null != mInstance) {

            workerThread.execute(runnable);
        }
    }

    /**
     * 3.中断线程，关闭任务
     */
    public void closeTask() {
        if (null != workerThread && null != mInstance) {

            Future<String> f = workerThread.submit(callBack);
            f.cancel(true);
        }
    }
}
