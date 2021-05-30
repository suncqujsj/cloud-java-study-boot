package com.java.study.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class WxConfig {

	@Value("${app.id}")
	private String appid;

	@Value("${app.secret}")
	private String appsecret;

	@Value("${wx.gettokenurl}")
	private String gettokenurl;

	@Value("${wx.getticketurl}")
	private String getticketurl;
}