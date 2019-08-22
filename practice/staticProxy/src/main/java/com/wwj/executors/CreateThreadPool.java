package com.wwj.executors;


import java.util.concurrent.*;

/**
 * Created by Nancy on 2019/8/22 14:53\
 *
 * Executors 提供四种线程池
 *
 */
public class CreateThreadPool {
    public static void main(String[] args) {
        /**
         *  是一个可根据需要创建新线程的线程池
         *  即 来一个任务就创建一个新线程，创建之前先看看 有不有原来
         *  可用的线程，如果有就用。如果没有，就重新建，并添加到线程池中
         *  终止并从缓存中移除那些已有60秒为被使用的线程，因此长时间未被使用的线程不会使用任何资源
         */
        ExecutorService executorService01 = Executors.newCachedThreadPool();
        //是一个单线程池 保证任务顺序执行
        ExecutorService executorService02 = Executors.newSingleThreadExecutor();
        //创建一个固定大小的线程池
        ExecutorService executorService03 = Executors.newFixedThreadPool(2);
        //创建一个大小无限的线程池，此线程池支持定时以及周期性执行任务的需求
        ExecutorService executorService04 = Executors.newScheduledThreadPool(2);

        /**
         * int corePoolSize,  核心线程数
         * int maximumPoolSize,  最大线程数
         * long keepAliveTime,  线程空闲时间 存活时间
         * TimeUnit unit,  存活时间的单位
         * BlockingQueue<Runnable> workQueue,  任务队列
         * ThreadFactory threadFactory  线程生产的工厂
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 5
                , TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()
                , Executors.defaultThreadFactory());
    }
}
