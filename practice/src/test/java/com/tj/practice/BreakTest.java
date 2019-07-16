package com.tj.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BreakTest {

    @Test
    public void breakTest(){
        List<String> list = Arrays.asList("a", "b", "c", "d", "e","f");
        List<String> list1 = Arrays.asList("aa", "bb");
        List<String> list2 = Arrays.asList("c", "d");
        List<String> list3 = Arrays.asList("e", "f");
        for (String s : list) {
            for (String s1 : list1) {
                if(s.equals(s1)){
                    System.out.println(s1);
                    continue;
                }
            }
            for (String s2 : list2) {
                if(s.equals(s2)){
                    System.out.println(s2);
                    continue;
                }
            }
            for (String s3 : list3) {
                if(s.equals(s3)){
                    System.out.println(s3);
                    continue;
                }
            }
        }
    }
}
