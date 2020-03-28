package com.wwj.thread;

/**
 * Created by Nancy on 2019/7/15 9:18
 */
public class ProgrammerApp {

    public static void main(String[] args) {
        //子类对象
        Programmer pro = new Programmer("HAHA...");
        //启动线程
        pro.start();

        //main方法也是线程的一条路径
        for (int i = 0; i < 100; i++) {
            System.out.println(i+",,,,,,,,,,,,");
        }
    }
}
