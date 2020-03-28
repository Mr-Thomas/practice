package com.wwj.thread;

/**
 * 继承Thread类 重写run方法
 * 启动: 创建子类对象 + 对象.start
 *
 * Created by Nancy on 2019/7/15 9:12
 */
public class Programmer extends Thread {

    //有参数构造 给该线程取名字
    public Programmer(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getName()+"..."+i+",,,");
        }
    }
}
