package com.tj.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CalendarTest {

    Calendar calendar = null;

    @Before
    public void test() {
        calendar = Calendar.getInstance();  //获取日历类
    }

    /**
     * 当前时间 前24小时 整点
     */
    @Test
    public void TimeTest() {
        long st = System.currentTimeMillis();
        for (int i = 0; i < 24; i++) {
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
            calendar.add(Calendar.HOUR_OF_DAY, -i);
            Date date = calendar.getTime();
            calendar = Calendar.getInstance();
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            System.out.println(format);
        }
        long ed = System.currentTimeMillis();
        System.out.println(ed-st);
    }

    //系统当前时间戳  三种方法
    @Test
    public void testTimeMillis() {
        String orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        System.out.println(orderTime);

        String l = System.currentTimeMillis() + "";
        String substring = l.substring(0, l.length() - 3);
        System.out.println(l);
        System.out.println(substring);

        long timeInMillis = calendar.getTimeInMillis();
        System.out.println(timeInMillis);    //效率 最慢

        long time = new Date().getTime();
        System.out.println(time);
    }

    @Test
    public void testt() {
        Date time = calendar.getTime();
        System.out.println(time);   //  Mon May 20 09:35:22 CST 2019
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        System.out.println(format);   //   2019-05-20 09:35:22
    }

    @Test
    public void CalendarDemo01() {
        // 获取年
        int year = calendar.get(Calendar.YEAR);
        // 获取月，这里需要需要月份的范围为0~11，因此获取月份的时候需要+1才是当前月份值
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获取日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 获取时
        int hour = calendar.get(Calendar.HOUR);
        // int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时表示
        // 获取分
        int minute = calendar.get(Calendar.MINUTE);
        // 获取秒
        int second = calendar.get(Calendar.SECOND);
        // 星期，英语国家星期从星期日开始计算
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        log.error("现在是" + year + "年" + month + "月" + day + "日" + hour
                + "时" + minute + "分" + second + "秒" + "星期" + weekday);
    }

    @Test
    public void CalendarDemo02() {
        int month = 5;  //5月
        calendar.set(Calendar.MONTH, month - 1); //月份的范围为0~11
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        int days = calendar.getActualMaximum(Calendar.DATE);
        log.error("当前月份最大天数: {}", days);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

//        calendar.set(Calendar.DAY_OF_MONTH, 1);  //每月第一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DATE));  //每月第一天 [作用同上]
        String firstDate = df.format(calendar.getTime());
        log.error("第一天: {}", firstDate);

//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
//        calendar.set(Calendar.DAY_OF_MONTH, 0);  //每月最后一天 [0 默认取上月最后一天 所以需要set月份]
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  //每月最后一天 [作用同上]
        String lastDate = df.format(calendar.getTime());
        log.error("最后一天: {}", lastDate);

        List<String> list = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            String date = df.format(calendar.getTime());
            list.add(date);
        }
        log.error("日期: {}", list);
    }

    // 一年后的今天
    @Test
    public void test2() {
        // 同理换成下个月的今天calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.YEAR, 1); //可以正负  -1:即去年今天
        // 获取年
        int year = calendar.get(Calendar.YEAR);
        // 获取月
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获取日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        log.error("一年后的今天：" + year + "年" + month + "月" + day + "日");
        calendar.add(Calendar.MONTH, -1); //上个月
        calendar.add(Calendar.DATE, -1);  //昨天
        String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        log.error("一年后的上个月的前一天: {}", date);
    }

    @Test
    public void test3() {
//        calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)-10);
        calendar.add(Calendar.MINUTE, -10);  //作用同上
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        log.error("10分钟前: {}", date);
    }

    @Test
    //开始时间
    public void test4() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        log.error("开始时间: {}", date);
    }

    /**
     * 迭代遍历
     */
    @Test
    public void test5() {
        Set<String> set = new HashSet<>();    //去重
        set.add("1001" + "_" + "123" + "_" + "abc");
        set.add("1111" + "_" + "222" + "_" + "333" + "_" + "5555");
        Set<String> one = new HashSet<>();
        Set<String> two = new HashSet<>();
        Set<String> three = new HashSet<>();
        Set<String> four = new HashSet<>();

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            log.error("next: {}", next);
            List<String> list = Arrays.asList(next.split("_"));
            log.error("list.size(): {}", list.size());
            if (list.size() == 3) {
                log.error("list3: {}", list);
                String a = next.substring(0, next.indexOf("_"));
                String b = next.substring(next.indexOf("_") + 1, next.lastIndexOf("_"));
                String c = next.substring(next.lastIndexOf("_") + 1, next.length());
                System.out.println(a + ":" + b + ":" + c);
                one.add(a);
                two.add(b);
                three.add(c);
            }
            if (list.size() == 4) {
                log.error("list4: {}", list);
                one.add(list.get(0));
                two.add(list.get(1));
                three.add(list.get(2));
                four.add(list.get(3));
            }
        }
        System.out.println(one);
        System.out.println(two);
        System.out.println(three);
        System.out.println(four);
    }
}
