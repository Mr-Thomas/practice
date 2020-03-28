package com.wwj.thread._status;

/**
 * waiting 无限等待
 *      一个线程正在无限等待另一个线程执行唤醒动作的线程 处于这一状态
 *
 *  调用wait()方法 可以让 正在执行的线程 进入 无限等待的状态
 *  调用notify() 方法 可以让 正在等待的线程  脱离 无限等待状态
 *      1).必须在同步中 [同步三种方式: 同步代码块 同步方法 lock锁]
 *      2).必须使用同步锁调用
 *  调用notifyAll() 方法 可以让 所有正在等待的线程  脱离 无限等待状态
 *  三方法来自object
 *
 *  wait() 当前线程必须拥有此对象监视器(同步锁)
 *          即 wait()方法想要调用 有两条件
 *              1).必须在同步中 [同步三种方式: 同步代码块 同步方法 lock锁]
 *              2).必须使用同步锁调用
 *
 *  Thread.State  getState()   返回该线程的状态
 *
 * Created by Nancy on 2019/7/18 15:25
 */
public class Waiting01 {
    //锁对象
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        //等待线程
        final Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    synchronized (lock) {
                        System.out.println(Thread.currentThread().getName()+" 获取到锁对象,调用wait()方法,进入waiting状态...");
                        //让当前线程进入无限等待状态
                        lock.wait();  //必须使用同步锁对象调用
                        System.out.println("唤醒线程拿到锁对象后 t1才被唤醒[唤醒后没有拿到锁] 进入锁阻塞(Blocked)状态; 只有获取到锁对象 才能进入Runnable状态");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "等待线程");
        t1.start();
        Thread.sleep(1000);
        System.out.println("t1线程的状态: "+t1.getState());

        //唤醒线程
        new Thread(new Runnable() {
            public void run() {
                synchronized (lock){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+" 获取锁对象,调用notify()方法,唤醒waiting状态的线程...");
                    lock.notify();  //唤醒
                    System.out.println("t1线程的状态: "+t1.getState());
                }
            }
        }, "唤醒线程").start();


        /*new Thread(new Runnable() {
            public void run() {
                //让当前线程进入无限等待状态
                try {
                    //错误写法,不满足wait()方法调用的条件(java.lang.IllegalMonitorStateException)
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "等待线程").start();*/
    }
}
