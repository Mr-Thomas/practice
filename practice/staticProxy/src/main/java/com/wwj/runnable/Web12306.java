package com.wwj.runnable;

/**
 * 三个窗口买票
 * 结果是 出现 重复票 0票 负数票
 * 引发线程安全问题
 *
 * 不安全前提
 *  1:多线程程序
 *  2:多个线程共享数据
 *  3:执行写的操作
 *
 * Created by Nancy on 2019/7/15 0:06
 */
public class Web12306 implements Runnable {

    private int num = 100;

    public void run() {
        while (true){
            if(num>0){  //代表可以卖票
                try {
                    //卖票的时候增加个出票时间
                    Thread.sleep(50); //计时等待(Timed Waiting)
                    System.out.println(Thread.currentThread().getName()+"正在卖: "+num--+" 号票");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        //真实角色 [线程任务对象]
        Web12306 web = new Web12306();
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
