package com.tj.practice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BCrypt_test {

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 密码加密
     * Spring Security 提供了BCryptPasswordEncoder类,实现Spring的PasswordEncoder接口使用BCrypt强哈希方法来加密密码
     */
    @Test
    public void miMaJiaMi(){
        String psw ="123";

        //密码加密 存数据库
        String encode = encoder.encode(psw);
        log.info("密码加密: {}",encode); //$2a$10$hj6y03QfLHitD8iAMyGT1OYC/BPP1293IuF.GoazTFPJbTR6mHLai

        //匹配密码
        boolean matches = encoder.matches("123", "$2a$10$hj6y03QfLHitD8iAMyGT1OYC/BPP1293IuF.GoazTFPJbTR6mHLai");
        log.info("密码匹配: {}",matches);
    }
}
