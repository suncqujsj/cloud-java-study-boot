package com.bob.boot;

import java.io.IOException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.bob.boot.util.ComputerInfo;

import lombok.extern.log4j.Log4j;

@SpringBootApplication
@MapperScan("com.bob.boot.datasource.mappers")
@ServletComponentScan
@EnableScheduling
@Log4j
public class BobCloudBoot {
	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(BobCloudBoot.class, args);
		Environment environment = context.getBean(Environment.class);
		log.info("启动成功，后端服务API地址：http://" + ComputerInfo.getIpAddr() + ":" + environment.getProperty("server.port")
				+ "");
	}
}
