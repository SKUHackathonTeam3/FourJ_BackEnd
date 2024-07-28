package com.skuteam3.fourj.global.firebase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:" + "fourj_firebase_service_key.json");

        File serviceAccount = resource.getFile();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(serviceAccount)))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("FCMScheduler-");

        return scheduler;
    }
}
