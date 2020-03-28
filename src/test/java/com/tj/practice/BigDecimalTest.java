package com.tj.practice;

import com.tj.practice.common.util.number.ArithUtil;
import com.tj.practice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BigDecimalTest {
    @Test
    public void testBigDecimal(){
        User user = new User();
        String amount = ArithUtil.mul(user.getAmount(), new BigDecimal(100)).intValue()+""; //乘100
        log.error("amount: {}",amount);         //10000

        String amount1 = ArithUtil.div(new BigDecimal(user.getAmount() + ""), new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString(); //除100 保留2位小数
        log.error("amount1: {}",amount1);       //1.00

    }
}
