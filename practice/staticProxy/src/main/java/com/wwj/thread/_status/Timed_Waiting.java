package com.wwj.thread._status;

/**
 * 1).TIMED WAITING  计时等待
 *  sleep(...) 与锁无关 ,线程睡眠到期后就自动苏醒,并返回到可运行状态 Runnable
 *
 * 2).BLOCKED 锁阻塞
 *  runnable状态 --没有拿到锁对象--> blocked[锁阻塞] --获取锁对象--> runnable状态
 *
 * 3).waiting 无限等待
 *
 *
 * Created by Nancy on 2019/7/18 14:57
 */
public class Timed_Waiting extends Thread{

    public static void main(String[] args) {
        Timed_Waiting timedWaiting = new Timed_Waiting();
        timedWaiting.start();
    }

    //计数到100,每个数字间暂停1秒,每10个数字 输出个字符串
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            try {
                if (i % 10 == 1) {
                    System.out.println("---每10个数字---" + i);
                }
                System.out.print(i);
                Thread.sleep(1000);    //timed waiting  计时等待
                System.out.println("    停顿1秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
