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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StringUtilsTest {
    @Test
    public void oo(){
        String sss ="https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx030957263728778ebf2fcebc1500667700&package=3232615319";
        int i1 = sss.indexOf("=")+1;
        int i2 = sss.indexOf("&");
        String substring = sss.substring(i1, i2);
        System.out.println(substring);
        String string = UUID.randomUUID().toString().replace("-","");
        log.error("string:{}",string);
        String code = "654024580000";
//        String code = null;
        log.error("code:{}",code != null ? code.substring(0,6) : "654000");
    }
    @Test
    public void ttt(){
        String s ="sjdd=DD2_20190808SNBWUM449397062&content=wcm6V78CrILSZEorkqQubmkBP11nuqvTg1Sjs4V6gZxuHalkqLRhfJEuEsS6Bz8pzIbc2fp%2Bmz%2BM4HV0BKB0gQ0eUZ7z8blY%2FCg1OupT1wp3UDXbkk%2BU0crI9OJ3rE9R&encryptType=AES";
        Map<String, String> paramMap = Stream.of(s.split("&"))
                .map(obj -> obj.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry.length > 1 ? entry[1] : ""));
        log.error("结果map：{}", paramMap);
    }

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

        int i = new Random().nextInt(100);
        System.out.println(i);
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
