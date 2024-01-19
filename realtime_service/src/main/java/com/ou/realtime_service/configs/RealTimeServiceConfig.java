package com.ou.realtime_service.configs;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
@PropertySource("classpath:configs.properties")
public class RealTimeServiceConfig {
    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        if(FirebaseApp.getApps().isEmpty()){
            System.out.println("[DEBUG] - Starting create Firebase app");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://ou-social-network-bf1ea-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();
            System.out.println("[DEBUG] - Successfully create Firebase options");
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
        
    }

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:serviceAccountKey.json");
            try (InputStream serviceAccount = resource.getInputStream()) {
                System.out.println("[DEBUG] - ACCOUNT SERVICE CREDENTIAL");
                return GoogleCredentials.fromStream(serviceAccount);
            }

        } catch (IOException ex) {
            System.out.println("[DEBUG] - DEFAULT CREDENTIAL");
            throw new IOException("FAIL TO INIT FIREBASE APP");
        }
    }
}
