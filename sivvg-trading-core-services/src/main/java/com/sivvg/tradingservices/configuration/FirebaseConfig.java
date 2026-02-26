package com.sivvg.tradingservices.configuration;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void init() {
        try (FileInputStream serviceAccount =
                     new FileInputStream(firebaseConfigPath)) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("✅ Firebase initialized using: " + firebaseConfigPath);

        } catch (IOException e) {
            throw new RuntimeException(
                    "❌ Firebase initialization failed. Path: " + firebaseConfigPath, e);
        }
    }
}