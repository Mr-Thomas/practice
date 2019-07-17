package com.wwj.iotest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by Nancy on 2019/7/17 14:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IoDemo {

    private static final String PUBLIC_KEY = "publickey.txt";

    @Test
    public void getPublicKey(){
        String publicKey = publicKey();
        log.info("publicKey: {}",publicKey);
    }
    //io流读取配置文件
    public String publicKey(){
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            String publicKey = br.lines().collect(Collectors.joining("\n"));
            return publicKey;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "获取公钥出错";
    }
}
