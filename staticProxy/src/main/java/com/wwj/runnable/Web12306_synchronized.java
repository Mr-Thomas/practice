package com.wwj.runnable;

/**
 * 同步代码块解决安全问题
 *
 * 同步代码块:
 *      sychronized 关键字  可以用于某个方法中的某块区域
 *      表示对这块区域的资源实现互斥访问
 *  各式:
 *      synchronized(锁对象){
 *          需要进行同步操作的代码
 *      }
 *     锁对象 即同步锁
 *          1.锁对象 可以是任意类型
 *          2.多个线程 使用同一把锁
 *        提示: 在任何时候 最多允许一个线程拥有同步锁,其他线程在外边等
 *
 * Created by Nancy on 2019/7/15 0:06
 */
public class Web12306_synchronized implements Runnable {

    private int num = 100;
    //锁对象
    Object lock = new Object();


    public void run() {
        while (true){

            synchronized (lock){

                if(num>0){  //代表可以卖票
                    try {
                        //卖票的时候增加个出票时间
                        Thread.sleep(50);
                        System.out.println(Thread.currentThread().getName()+"正在卖: "+num--+" 号票");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        //真实角色 [线程任务对象]
        Web12306_synchronized web = new Web12306_synchronized();
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
