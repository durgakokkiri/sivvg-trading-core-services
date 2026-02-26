package com.sivvg.tradingservices.configuration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class FirebaseConfigTest {

    @Test
    void initializeFirebase_fileNotFound() {

        FirebaseConfig firebaseConfig = new FirebaseConfig();

        // Set invalid path
        ReflectionTestUtils.setField(firebaseConfig,
                "firebaseConfigPath",
                "invalid/path/firebase.json");

        RuntimeException exception =
                assertThrows(RuntimeException.class,
                        firebaseConfig::init);

        assertTrue(exception.getMessage()
                .contains("Firebase initialization failed"));
    }
}