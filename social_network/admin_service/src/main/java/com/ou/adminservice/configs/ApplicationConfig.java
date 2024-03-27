package com.ou.adminservice.configs;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.cloudinary.Cloudinary;
// import com.cloudinary.utils.ObjectUtils;
// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;
// import com.ou.social_network.components.DateFormatter;
// import com.ou.social_network.pojo.Account;
// import com.ou.social_network.repository.repositoryJPA.AccountRepositoryJPA;

@Configuration
@PropertySource("classpath:configs.properties")
// @ComponentScan("com.ou.social_network")
@EnableTransactionManagement
public class ApplicationConfig implements WebMvcConfigurer {
    @Autowired
    private Environment environment;

    // @Autowired
    // private AccountRepositoryJPA accountRepository;

    // @Autowired
    // private DateFormatter dateFormatter;

    @Autowired
    private ResourceLoader resourceLoader;

    // @Bean
    // public Cloudinary getCloudinary() {
    //     return new Cloudinary(ObjectUtils.asMap(
    //             "cloud_name", environment.getProperty("CLOUDINARY_CLOUD_NAME"),
    //             "api_key", environment.getProperty("CLOUDINARY_API_KEY"),
    //             "api_secret", environment.getProperty("CLOUDINARY_API_SECRET"),
    //             "secure", true));
    // }

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
    // }

    // @Bean
    // public UserDetailsService getUserDetailsService() {
    //     return new UserDetailsService() {
    //         @Override
    //         public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //             Account account = accountRepository.findByEmail(email).get();
    //             if (account == null) {
    //                 throw new UsernameNotFoundException("Email không tồn tại");
    //             }
    //             Set<GrantedAuthority> authorities = new HashSet<>();
    //             authorities.add(new SimpleGrantedAuthority(account.getRoleId().getName()));
    //             return new org.springframework.security.core.userdetails.User(
    //                     account.getEmail(), account.getPassword(), authorities);
    //         }
    //     };
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
    //     int threadNumber = Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
    //     ScheduledExecutorService configs = Executors.newScheduledThreadPool(threadNumber);
    //     return configs;
    // }

    @Bean
    public SimpleDateFormat getSimpleDate() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    // @Bean(name = "executorService")
    // public ExecutorService getThreadPool() {
    //     int threadNumber = Integer.parseInt(environment.getProperty("THREAD_NUMBER"));
    //     ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
    //     return executor;
    // }

    // @Override
    // public void addFormatters(FormatterRegistry registry) {
    //     // registry.addFormatter(new CategoryFormatter());
    //     // In case of needed to format fields of pojo
    //     // create new class and
    //     // implements the Formatter<T> interface
    //     // might not necessary
    //     registry.addFormatter(dateFormatter);
    // }

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
    
    // @Bean
    // public FirebaseApp firebaseApp(GoogleCredentials credentials) {
    //     if(FirebaseApp.getApps().isEmpty()){
    //         System.out.println("[DEBUG] - Starting create Firebase app");
    //         FirebaseOptions options = FirebaseOptions.builder()
    //                 .setCredentials(credentials)
    //                 .setDatabaseUrl("https://ou-social-network-bf1ea-default-rtdb.asia-southeast1.firebasedatabase.app")
    //                 .build();
    //         System.out.println("[DEBUG] - Successfully create Firebase options");
    //         return FirebaseApp.initializeApp(options);
    //     } else {
    //         return FirebaseApp.getInstance();
    //     }
        
    // }

    // @Bean
    // public GoogleCredentials googleCredentials() throws IOException {
    //     try {
    //         Resource resource = resourceLoader.getResource("classpath:serviceAccountKey.json");
    //         try (InputStream serviceAccount = resource.getInputStream()) {
    //             System.out.println("[DEBUG] - ACCOUNT SERVICE CREDENTIAL");
    //             return GoogleCredentials.fromStream(serviceAccount);
    //         }

    //     } catch (IOException ex) {
    //         System.out.println("[DEBUG] - DEFAULT CREDENTIAL");
    //         throw new IOException("FAIL TO INIT FIREBASE APP");
    //     }
    // }
}
