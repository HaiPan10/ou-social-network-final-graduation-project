package com.ou.adminservice.configs;

import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ou.adminservice.components.DateFormatter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@PropertySource("classpath:configs.properties")
@Slf4j
// @ComponentScan("com.ou.social_network")
public class ApplicationConfig implements WebMvcConfigurer{
    @Autowired
    private Environment environment;

    @Autowired
    private WebClient.Builder webClient;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClient() {
        return WebClient.builder();
    }

    // @Autowired
    // private AccountRepositoryJPA accountRepository;

    @Autowired
    private DateFormatter dateFormatter;

    @Autowired
    private ResourceLoader resourceLoader;

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
    // }

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

    // @Bean(name = "scheduledExecutorService")
    // public ScheduledExecutorService getScheduledService() {
    // int threadNumber =
    // Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
    // ScheduledExecutorService configs =
    // Executors.newScheduledThreadPool(threadNumber);
    // return configs;
    // }

    @Bean
    public SimpleDateFormat getSimpleDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    // @Bean(name = "executorService")
    // public ExecutorService getThreadPool() {
    // int threadNumber =
    // Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
    // ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
    // return executor;
    // }

    @Override
    public void addFormatters(FormatterRegistry registry) {
    // registry.addFormatter(new CategoryFormatter());
    // In case of needed to format fields of pojo
    // create new class and
    // implements the Formatter<T> interface
    // might not necessary
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

}
