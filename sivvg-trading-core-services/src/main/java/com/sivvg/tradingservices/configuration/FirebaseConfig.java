package com.sivvg.tradingservices.configuration;

import java.io.InputStream;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            // 🔹 Load JSON from classpath (resources folder)
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream("firebase-service-account.json");

            if (serviceAccount == null) {
                throw new RuntimeException(
                    "❌ Firebase service account JSON not found! " +
                    "Make sure 'firebase-service-account.json' is in src/main/resources/");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("✅ Firebase initialized successfully");

        } catch (Exception e) {
            throw new RuntimeException("❌ Firebase initialization failed: " + e.getMessage(), e);
        }
    }
}