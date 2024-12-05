package com.cake.easytrade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cake.easytrade.mapper")
public class EasytradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasytradeApplication.class, args);
	}

}
