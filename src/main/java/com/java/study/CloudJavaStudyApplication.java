package com.java.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching 
@EnableScheduling
@ComponentScan(basePackages = { "com.java.study" })
@MapperScan("com.java.study.mapper")
@EnableAutoConfiguration
@ImportResource(locations={"classpath:spring-config.xml"})
@EnableTransactionManagement(proxyTargetClass = true)
@EnableFeignClients(basePackages = "com.java.study.service")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CloudJavaStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudJavaStudyApplication.class, args);
	}
}