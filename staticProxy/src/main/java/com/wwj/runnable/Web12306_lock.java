package com.wwj.runnable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock锁 解决线程安全问题
 *      jdk 1.5 以后出现的
 *      Lock 实现提供比使用 synchronized 方法和语句可以获得的更广泛的锁定操作
 *      更灵活
 *      定义了两个方法
 *          lock()   加锁
 *          unlock() 释放锁
 *      java.util.concurrent.locks
 *      Interface Lock
 *      实现类 ReentrantLock
 *
 * Created by Nancy on 2019/7/15 0:06
 */
public class Web12306_lock implements Runnable {

    private int num = 100;

    Lock lock = new ReentrantLock(); //多态

    public void run() {
        while (true){

            lock.lock();

            if(num>0){  //代表可以卖票
                try {
                    //卖票的时候增加个出票时间
                    Thread.sleep(50);
                    System.out.println(Thread.currentThread().getName()+"正在卖: "+num--+" 号票");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lock.unlock();

        }
    }

    public static void main(String[] args) {
        //真实角色 [线程任务对象]
        Web12306_lock web = new Web12306_lock();
        //代理角色 持有真是角色的引用 [线程对象]
        Thread t1 = new Thread(web,"窗口1");
        Thread t2 = new Thread(web,"窗口2");
        Thread t3 = new Thread(web,"窗口3");
        //启动
        t1.start();
        t2.start();
        t3.start();
    }
}
