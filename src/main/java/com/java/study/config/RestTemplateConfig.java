package com.java.study.config;

import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置模板
 *
 * @author like
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		RestTemplate template = new RestTemplate(factory);
		template.getMessageConverters().set(1, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return template;
	}

	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(10000);// 单位为ms
		factory.setConnectTimeout(10000);// 单位为ms
		return factory;
	}
}