package com.java.study.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class RefreshConfig {

	@Value("${refresh.oneminutetime}")
	private int oneminutetime;

	@Value("${refresh.twominutetime}")
	private int twominutetime;

	@Value("${refresh.threeminutetime}")
	private int threeminutetime;

	@Value("${refresh.fourminutetime}")
	private int fourminutetime;

	@Value("${refresh.fiveminutetime}")
	private int fiveminutetime;

	@Value("${refresh.randomtime}")
	private int randomtime;

	@Value("${refresh.halfhourtime}")
	private int halfhourtime;

	@Value("${refresh.onehourtime}")
	private int onehourtime;

	@Value("${refresh.twohourtime}")
	private int twohourtime;

	@Value("${refresh.threehourtime}")
	private int threehourtime;

	@Value("${refresh.halfdaytime}")
	private int halfdaytime;

	@Value("${refresh.onedaytime}")
	private int onedaytime;
}