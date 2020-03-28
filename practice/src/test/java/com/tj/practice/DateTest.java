package com.tj.practice;

import com.tj.practice.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/17 13:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DateTest {
    @Test
    public void test(){
        Date date = DateUtil.toDate("20190117140020000", DateUtil.DATE_FIXED_FORMAT);
        log.info("date:{}",date);
        long time = date.getTime();
        log.info(String.valueOf(time));
    }
}
