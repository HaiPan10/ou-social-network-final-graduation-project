package com.ou.realtimeservice.configs;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import com.ou.realtimeservice.components.DateFormatter;

import jakarta.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:configs.properties")
// @ComponentScan("com.ou.social_network")
// @EnableTransactionManagement
public class ApplicationConfig implements WebFluxConfigurer {
    @Autowired
    private ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory;
    @Autowired
    private DateFormatter dateFormatter;

    @PostConstruct
    void setup() {
        this.concurrentKafkaListenerContainerFactory.getContainerProperties().setObservationEnabled(true);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient(){
        return WebClient.builder();
    }

    @Bean
    public SimpleDateFormat getSimpleDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // registry.addFormatter(new CategoryFormatter());
        // In case of needed to format fields of pojo
        // create new class and
        // implements the Formatter<T> interface
        // might not necessary
        registry.addFormatter(dateFormatter);
    }
}
