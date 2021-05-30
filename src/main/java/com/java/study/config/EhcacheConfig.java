package com.java.study.config;

//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//@Configuration
//@EnableCaching 使用的时候放开即可
//public class EhcacheConfig  {
//
//    /*
//          * 据shared与否的设置,Spring分别通过CacheManager.create()或new CacheManager()方式来创建一个ehcache基地.
//     */
//    @Bean("ehCacheManagerFactoryBean")
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        cacheManagerFactoryBean.setShared(true);
//        return cacheManagerFactoryBean;
//    }
//
//    /*
//      * ehcache 主要的管理器
//      */
//    @Bean
//    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
//        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
//    }
//}