package com.tj.practice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Create_Jwt_token_test {

    /**
     * setSubject 用户名
     * setIssuedAt用于设置签发时间
     * signWith用于设置签名秘钥
     * 生成token
     */
    @Test
    public void createToken(){

        Map<String,Object> claims = new HashMap<String, Object>(){
            {
                put("uid","123456");
                put("userName","admin");
                put("nickName","管理员");
            }
        };

        SecretKey key = generalKey();
        log.info("key: {}",key);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)      //自定义
                .claim("role","草鸡管理员")    //自定义
                .setId("888")
                .setSubject("小白")
                .setIssuedAt(new Date())   //iat :jwt签发时间
                .setExpiration(new Date(new Date().getTime()+60*3*1000))   //过期时间 3分钟
                .signWith(SignatureAlgorithm.HS256,key);
        log.info("token: {}",jwtBuilder.compact());  //eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMjM0NTYiLCJzdWIiOiLlsI_nmb0iLCJyb2xlIjoi6I2J6bih566h55CG5ZGYIiwibmlja05hbWUiOiLnrqHnkIblkZgiLCJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNTYwNzU3NTM3LCJpYXQiOjE1NjA3NTczNTcsImp0aSI6Ijg4OCJ9.FSBNLGf_e4L0oJde0oda1Mq1arhJ9iLudc00jDa6jL0
    }

    public SecretKey generalKey(){
        String stringKey = "itCast";
        byte[] decode = Base64.getDecoder().decode(stringKey);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(decode, 0, decode.length, "AES");
        return key;
    }

    /**
     * 解析token
     */
    @Test
    public void parseToken(){
        SecretKey key = generalKey();
        log.info("key: {}",key);
        try {
            Claims claims = Jwts.parser().setSigningKey(key)
                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxMjM0NTYiLCJzdWIiOiLlsI_nmb0iLCJyb2xlIjoi6I2J6bih566h55CG5ZGYIiwibmlja05hbWUiOiLnrqHnkIblkZgiLCJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNTYwNzU3NTM3LCJpYXQiOjE1NjA3NTczNTcsImp0aSI6Ijg4OCJ9.FSBNLGf_e4L0oJde0oda1Mq1arhJ9iLudc00jDa6jL0")
                    .getBody();

            System.out.println("uid: "+claims.get("uid", String.class));
            System.out.println("role: "+claims.get("role"));
            System.out.println("token签发时间: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
            System.out.println("token过期时间: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
            log.info("解析token: {}",claims);
            log.info("claims: {}", claims.getId() + " " + claims.getSubject() + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
