package com.cake.easytrade.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.ecommerce.mapper") // Scans the mapper interfaces
public class MyBatisConfig {
    // Additional MyBatis configuration if needed
}
