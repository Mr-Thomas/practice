package com.tj.practice;

import com.alibaba.fastjson.JSONObject;
import com.tj.practice.common.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.RoundingMode;
import java.util.*;
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
        Map<String, String> paramMap = new HashMap<String, String>() {
            {
                put("merchant_id","merchant_id");// 平台分配的商户号
                put("pay_type","pay_type");   //支付类型
                put("out_trade_no","fdsf154545");// 商户订单号
                put("amount","2.00");// 金额
                put("notify_url","localhost");
                put("timestamp",new Date().getTime()+"");
                put("secret","fdskladfjsiogjw");  //秘钥
            }
        };
        sign4Mqf(paramMap);
    }

    public String sign4Mqf(Map<String, String> paramMap) {
        if (paramMap == null || paramMap.isEmpty()) {
            return null;
        }
        Set<String> keyset = new TreeSet<>();
        keyset.addAll(paramMap.keySet());
        StringBuilder paramBuilder = new StringBuilder();
        keyset.forEach(key -> {
            if (StringUtils.isNotBlank(paramMap.get(key)) && !StringUtils.equals(key, "sign")) {
                paramBuilder.append(",").append(key).append(":").append(paramMap.get(key));
            }
        });
        String substring = paramBuilder.substring(1);
        log.error("签名前 {}",substring);
        String ss = MD5Util.MD5Encode(substring, "UTF-8");
        log.error("签名后 {}",ss);
        return ss;

    }
}
