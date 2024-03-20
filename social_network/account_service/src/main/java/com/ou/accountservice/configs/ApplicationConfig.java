package com.ou.accountservice.configs;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
@PropertySource("classpath:configs.properties")
// @ComponentScan("com.ou.social_network")
@EnableTransactionManagement
public class ApplicationConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient(){
        return WebClient.builder();
    }

    @Autowired
    private Environment environment;

    @Bean
    public SimpleDateFormat getSimpleDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Bean
    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getProperty("CLOUDINARY_CLOUD_NAME"),
                "api_key", environment.getProperty("CLOUDINARY_API_KEY"),
                "api_secret", environment.getProperty("CLOUDINARY_API_SECRET"),
                "secure", true));
    }

    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService getScheduledService() {
        int threadNumber = Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
        ScheduledExecutorService configs = Executors.newScheduledThreadPool(threadNumber);
        return configs;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}