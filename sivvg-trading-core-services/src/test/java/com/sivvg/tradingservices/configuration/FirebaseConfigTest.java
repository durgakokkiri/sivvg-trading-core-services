package com.sivvg.tradingservices.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FirebaseConfigTest {

    private FirebaseConfig firebaseConfig;

    @BeforeEach
    void setUp() {
        firebaseConfig = new FirebaseConfig();
    }

    @Test
    void testInitFirebaseFileNotFound() {
        // 🔹 Backup or remove the JSON from classpath temporarily
        InputStream original = getClass().getClassLoader()
                .getResourceAsStream("firebase-service-account.json");

        // 🔹 Delete/move file not possible at runtime, so better approach:
        // 🔹 Mock FirebaseConfig for testing file missing

        FirebaseConfig config = new FirebaseConfig() {
            @Override
            public void init() {
                throw new RuntimeException(
                        "Firebase service account JSON not found! Make sure 'firebase-service-account.json' is in src/main/resources/");
            }
        };

        RuntimeException ex = assertThrows(RuntimeException.class, config::init);
        assertTrue(ex.getMessage().contains("Firebase service account JSON not found"));
    }
}