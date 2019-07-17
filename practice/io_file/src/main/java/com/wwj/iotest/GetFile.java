package com.wwj.iotest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Created by Nancy on 2019/7/17 14:16
 */
@Slf4j
public class GetFile {

    private static final String PUBLIC_KEY = "publickey.txt";

    public static void main(String[] args) {
        String publicKey = publicKey();
        log.info("publicKey: {}",publicKey);
    }

    private static String publicKey() {
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
