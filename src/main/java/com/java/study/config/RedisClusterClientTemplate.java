package com.java.study.config;


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.log4j.Log4j;
//
///**
// * @Description://原文链接：https://blog.csdn.net/u010199866/article/details/80705797
// */
//@Service
//@Log4j  //集群专用
//public class RedisClusterClientTemplate {
//	
//	@Autowired
//	private JedisClusterConfig jedisClusterConfig;
//
//	public boolean setToRedis(String key, String value) {
//		try {
//			// NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
//			String str = jedisClusterConfig.getJedisCluster().set(key, value, "NX", "EX", 4 * 60 * 60);
//			if ("OK".equals(str))
//				return true;
//		} catch (Exception ex) {
//			log.error("setToRedis:{Key:" + key + ",value" + value + "}", ex);
//		}
//		return false;
//	}
//
//	public String getRedis(String key) {
//		String str = null;
//		try {
//			str = jedisClusterConfig.getJedisCluster().get(key);
//		} catch (Exception ex) {
//			log.error("getRedis:{Key:" + key + "}", ex);
//		}
//		return str;
//	}
//}