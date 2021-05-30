package com.java.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")// 允许跨域访问的路径
				.allowedOrigins("*")// 允许跨域访问的源
				.allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")// 允许请求方法
				.maxAge(168000)// 预检间隔时间
				.allowCredentials(true);// 是否发送cookie
	}
}