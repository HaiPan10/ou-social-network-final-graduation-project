package com.ou.mailservice.configs;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:configs.properties")
// @ComponentScan("com.ou.social_network")
// @EnableTransactionManagement
public class ApplicationConfig implements WebFluxConfigurer  {

    @Autowired
    private Environment environment;

    @Autowired
    private ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService getScheduledService() {
        int threadNumber = Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
        ScheduledExecutorService configs = Executors.newScheduledThreadPool(threadNumber);
        return configs;
    }

    @Bean
    public SimpleDateFormat getSimpleDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Bean(name = "executorService")
    public ExecutorService getThreadPool() {
        int threadNumber = Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        return executor;
    }

    @PostConstruct
    void setup() {
        this.concurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient(){
        return WebClient.builder();
    }
}
