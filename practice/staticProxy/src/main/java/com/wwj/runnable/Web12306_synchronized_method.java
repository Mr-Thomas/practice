package com.wwj.runnable;

/**
 * 同步方法解决安全问题
 * <p>
 *  同步方法: 使用synchronized关键字修饰的方法
 *  格式:
 *  public synchronized void method(){
 *      可能出现线程安全问题的代码
 *  }
 * <p>
 * Created by Nancy on 2019/7/15 0:06
 */
public class Web12306_synchronized_method implements Runnable {

    private int num = 100;

    public void run() {
        while (true) {
            method();
        }
    }

    public synchronized void method() {
        if (num > 0) {  //代表可以卖票
            try {
                //卖票的时候增加个出票时间
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() + "正在卖: " + num-- + " 号票");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //真实角色 [线程任务对象]
        Web12306_synchronized_method web = new Web12306_synchronized_method();
        //代理角色 持有真是角色的引用 [线程对象]
        Thread t1 = new Thread(web, "窗口1");
        Thread t2 = new Thread(web, "窗口2");
        Thread t3 = new Thread(web, "窗口3");
        //启动
        t1.start();
        t2.start();
        t3.start();
    }
}
