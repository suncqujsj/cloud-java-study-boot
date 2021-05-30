package com.java.study.config;

import java.util.concurrent.Executors;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfiguration {
	@Bean
	public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory() {
		TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
		tomcatFactory.setPort(8200);
		tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
		return tomcatFactory;
	}
}

class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
	public void customize(Connector connector) {
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		protocol.setExecutor(Executors.newCachedThreadPool());
		// 设置最大连接数
		protocol.setMaxConnections(20000);
		// 设置最大线程数
		protocol.setMaxThreads(5000);
		protocol.setConnectionTimeout(60000);
	}
}