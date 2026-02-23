package com.sivvg.tradingservices.configuration;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	@PostConstruct
	public void init() {
		try {
			InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}

		} catch (Exception e) {
			throw new RuntimeException("❌ Firebase initialization failed", e);
		}
	}
}
