package com.wwj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tj.practice.*.mapper")
public class StartPractice {

	public static void main(String[] args) {
		SpringApplication.run(StartPractice.class, args);
	}

}
