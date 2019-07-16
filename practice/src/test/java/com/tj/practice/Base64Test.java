package com.tj.practice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tj.practice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Base64Test {

    /**
     * Base64互转
     */
    @Test
    public void testBase64(){
        User user = new User();
        user.setId(1);
        user.setName("张三");
        String jsonString = JSON.toJSONString(user);
        log.error("jsonString：{}", jsonString);

//        String base64Str = new String(Base64.encodeBase64(jsonString.getBytes()));   //base64加密
        String base64Str = java.util.Base64.getEncoder().encodeToString(jsonString.getBytes(StandardCharsets.UTF_8)); //base64加密
        log.error("base64加密：{}", base64Str);
//        String reqJson = new String(Base64.decodeBase64(base64Str.getBytes(StandardCharsets.UTF_8)));    //base64解密
        String reqJson = new String(java.util.Base64.getDecoder().decode(base64Str.getBytes(StandardCharsets.UTF_8))); //base64解密
        log.error("base64解密：{}", reqJson);

        JSONObject response = JSONObject.parseObject(jsonString);
        log.error("response：{}", response);

        log.error("----------------------------------------------");

        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("name","张三");
        String jsonStrMap = JSON.toJSONString(map);
        log.error("jsonStrMap：{}", jsonStrMap);

        String base64StrMap = new String(Base64.encodeBase64(jsonStrMap.getBytes()));  //base64加密
        log.error("base64加密：{}", base64StrMap);
        String reqJsonMap = new String(Base64.decodeBase64(base64StrMap.getBytes(StandardCharsets.UTF_8)));    //base64解密
        log.error("base64解密：{}", reqJsonMap);

        JSONObject responseMap = JSONObject.parseObject(jsonStrMap);
        log.error("responseMap：{}", responseMap);

    }
}
