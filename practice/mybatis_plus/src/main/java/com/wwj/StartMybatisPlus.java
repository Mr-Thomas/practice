package com.wwj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wwj.*.mapper")
public class StartMybatisPlus {

	public static void main(String[] args) {
		SpringApplication.run(StartMybatisPlus.class, args);
	}

}
