package com.wwj;

/**
 * 动态代理 静态代理 区别:
 * 两者实现的效果是一样的
 * 静态代理 代理角色是写死的
 * 动态代理 代理角色是动态创建的
 *
 * 静态代理 设计模式
 * 1.真实角色
 * 2.代理角色 [代理角色 持有真是角色的引用]
 * 3.二者要实现相同的接口
 *
 * Created by Nancy on 2019/7/14 23:08
 *
 * 实现Runnable接口创建线程  即是 静态代理
 * 1.Thread类实现了Runnable接口 (代理角色)
 * 2.实现Runnable接口创建线程  重写run方法  (真实角色)
 *
 */
public class StaticProxy {
    public static void main(String[] args) {
        //创建真实角色
        Marry you = new You(); //多态
        //创建代理角色  持有真实角色对象
        Company company = new Company(you);
        company.marry();
    }
}

//相同的接口(结婚)
interface Marry {
    void marry();
}

//真是角色 实现Marry接口
class You implements Marry {

    public void marry() {
        System.out.println("你结婚了...");
    }
}

//代理角色(婚庆公司) 实现Marry接口
class Company implements Marry {

    private Marry you; //多态

    //无参带参构造
    public Company() {
    }
    public Company(Marry you) {
        this.you = you;
    }

    public void marry() {
        before();
        you.marry();
        after();
    }

    private void before(){
        System.out.println("布置新房");
    }
    private void after(){
        System.out.println("闹洞房");
    }
}