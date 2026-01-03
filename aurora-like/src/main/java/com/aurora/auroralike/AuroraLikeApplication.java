package com.aurora.auroralike;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
@MapperScan("com.aurora.auroralike.mapper")
public class AuroraLikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuroraLikeApplication.class, args);
	}

}
