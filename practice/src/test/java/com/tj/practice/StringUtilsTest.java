package com.tj.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StringUtilsTest {

    @Test
    public void test(){
        String str = "a,b,c";
        String and = str.replace(",", " and ");
        System.out.println(and);

        String[] split = str.split(",");
        long start = System.currentTimeMillis();
        Stream.of(split).forEach(s -> System.out.println(s));
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        long start1 = System.currentTimeMillis();
        Arrays.asList(split).forEach(s -> System.out.println(s));
        long end1 = System.currentTimeMillis();
        System.out.println(end1-start1);
    }

    @Test
    public void test01(){
        String s = "DD220190518MCFKZN522257506";
                  //DD2_20190518MCFKZN522257506
        String s1 = s.substring(0, 3) + "_" + s.substring(3, s.length());
        System.out.println(s1);
    }

    @Test
    public void tesr02(){
//        String s ="'[1548:6022,1539:6022]'";
        String s ="'[1548:6022]'";
        String substring = s.substring(2, s.length() - 2);
        if(substring.contains(",")){
            String[] split = substring.split(",");
        }else {
            String[] split = substring.split(":");
            System.out.println(split[0]+"  "+split[1]);
        }
    }
    @Test
    public void tesr03(){
        System.out.println("测试svn");
    }
}
