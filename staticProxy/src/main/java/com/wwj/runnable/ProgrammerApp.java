package com.wwj.runnable;

/**
 * 2.启动多线程 使用静态代理
 *  *      1).创建真实角色
 *  *      2).创建代理角色+持有真实角色的引用
 *  *      3).调用start方法 启动线程
 *
 * Created by Nancy on 2019/7/14 23:55
 */
public class ProgrammerApp {

    public static void main(String[] args) {
//      1).创建真实角色
        Programmer pro = new Programmer();
//      2).创建代理角色 + 持有真实角色的引用
        Thread proxy = new Thread(pro,"小强");
//      3).调用start方法 启动线程
        proxy.start();

//      main主线程
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+" : world..."+i);
        }
    }
}
