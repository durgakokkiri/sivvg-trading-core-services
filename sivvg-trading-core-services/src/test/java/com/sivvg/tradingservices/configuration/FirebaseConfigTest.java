package com.sivvg.tradingservices.configuration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

class FirebaseConfigFailureTest {

    @Test
    void initializeFirebase_fileNotFound() {

        FirebaseConfig config = new FirebaseConfig() {

            @Override
            protected InputStream getServiceAccountStream() {
                return null; // 🔥 simulate file missing
            }
        };

        RuntimeException exception =
                assertThrows(RuntimeException.class, config::init);

        assertTrue(exception.getMessage()
                .contains("Firebase initialization failed"));
    }
}