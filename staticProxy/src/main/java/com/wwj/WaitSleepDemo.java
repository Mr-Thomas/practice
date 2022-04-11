package com.wwj;

/**
 * 1、sleep是Thread类的方法，wait是object类定义的方法
 * 2、sleep方法可以在任何地方使用
 * 3、wait方法只能在synchronized方法/块中使用
 * <p>
 * 4、sleep只会让出cpu，不会导致锁行为改变；即当前线程拿到锁会一直持有锁等待，直到该线程其他方法释放锁
 * 5、wait不仅让出cpu，还会释放已经占有的同步资源锁，
 * 这也解释了wait方法为什么只能在synchronized方法/块中使用，因为只有先获取到了锁，才能使用同步锁调用wait方法释放锁
 */
public class WaitSleepDemo {
    public static void main(String[] args) throws InterruptedException {

        final Object lock = new Object();
        new Thread(() -> {
            System.out.println("A is waiting to get lock");
            synchronized (lock) {
                try {
                    Thread.sleep(20);//不会改变锁的行为，当前线程拿到锁会一直持有锁
                    System.out.println("A do wait method");
                    lock.wait(1000);//该方法会释放锁
                    System.out.println("A is done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(10);
        new Thread(() -> {
            System.out.println("B is waiting to get lock");
            synchronized (lock) {
                try {
                    System.out.println("B get lock");
                    System.out.println("B is sleeping 1000 ms");
                    Thread.sleep(1000);//不会改变锁的行为，当前线程拿到锁会一直持有锁
                    System.out.println("B is done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
