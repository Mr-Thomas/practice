package com.wwj.runnable;

/**
 推荐使用 Runnable 创建线程
      1).避免单继承的局限性
      2).便于共享资源
      3).增加了程序的健壮性,实现解耦操作,代码可被多个线程共享
      4).线程池只能放Runnable或Callable的线程,不能直接放继承Thread类

 使用Runnable创建线程
 1.Thread类实现了Runnable接口 (代理角色)
 1.类 实现runnable接口 + 重写 run() --> 真实角色
 2.启动多线程 使用静态代理
      1).创建真实角色
      2).创建代理角色+持有真实角色的引用
      3).调用start方法 启动线程

 * Created by Nancy on 2019/7/14 23:49
 */
public class Programmer implements Runnable {

//  Thread.currentThread() 当前执行的线程
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+" : hello..."+i);
        }
    }
}
