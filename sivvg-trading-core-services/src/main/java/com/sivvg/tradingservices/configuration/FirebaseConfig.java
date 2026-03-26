package com.sivvg.tradingservices.configuration;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            // 🔥 use separate method
            InputStream serviceAccount = getServiceAccountStream();

            if (serviceAccount == null) {
                throw new RuntimeException("❌ Firebase JSON file not found in resources");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("✅ Firebase initialized successfully");

        } catch (Exception e) {
            throw new RuntimeException("❌ Firebase initialization failed", e);
        }
    }

    // 🔥 VERY IMPORTANT (for testing)
    protected InputStream getServiceAccountStream() {
        return getClass().getClassLoader()
                .getResourceAsStream("firebase-service-account.json");
    }
}