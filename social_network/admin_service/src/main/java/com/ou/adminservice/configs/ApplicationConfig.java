package com.ou.adminservice.configs;

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
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ou.adminservice.components.DateFormatter;

@Configuration
@PropertySource("classpath:configs.properties")
public class ApplicationConfig implements WebFluxConfigurer {
    @Autowired
    private Environment environment;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }

    @Autowired
    private DateFormatter dateFormatter;

    // @Autowired
    // private ResourceLoader resourceLoader;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
        WebFluxConfigurer.super.addResourceHandlers(registry);
    }

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

    // @Bean
    // public FilterRegistrationBean<CharacterEncodingFilter> getFilterBean() {
    // FilterRegistrationBean<CharacterEncodingFilter> registrationBean =
    // new FilterRegistrationBean<CharacterEncodingFilter>();
    // registrationBean.setFilter(filter);
    // registrationBean.addUrlPatterns("/*");
    // return registrationBean;
    // }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(dateFormatter);
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    @Bean()
    public MultipartBodyBuilder bodyBuilder() {
        return new MultipartBodyBuilder();
    }

}
