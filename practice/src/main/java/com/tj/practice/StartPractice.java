package com.tj.practice;

import com.tj.practice.common.util.idWorker.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class StartPractice {

	public static void main(String[] args) {
		SpringApplication.run(StartPractice.class, args);
	}

	/**
	 * 随机生成id
	 * 将IdWorker放入spring容器
	 * @return
	 */
	@Bean
	public IdWorker idWorker(){
		return new IdWorker(1,1);
	}

	/**
	 * Spring Security 提供了BCryptPasswordEncoder类,实现Spring的PasswordEncoder接口使用BCrypt强哈希方法来加密密码
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
}
