package com.sivvg.tradingservices.util;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtUtilTest {

	private JwtUtil jwtUtil;

    private final String secret =
            "897b592bf60a58263a66a2540b04b8d24a6c062cf5e12ac204e91c5064676b7e38d8a123ec4b3080";

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();

        // Inject private fields manually
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 86400000L);
    }

    @Test
    public  void generateToken_shouldGenerateValidToken() {
        String token = jwtUtil.generateToken("101", "ADMIN");

        assertNotNull(token);
        assertTrue(token.length() > 20);
    }

    @Test
    public  void validateToken_shouldReturnTrue_forValidToken() {
        String token = jwtUtil.generateToken("101", "USER");

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    public  void extractUserId_shouldReturnCorrectUserId() {
        String token = jwtUtil.generateToken("555", "USER");

        String userId = jwtUtil.extractUserId(token);

        assertEquals("555", userId);
    }

    @Test
    public void extractRole_shouldReturnRoleWithPrefix() {
        String token = jwtUtil.generateToken("200", "ADMIN");

        String role = jwtUtil.extractRole(token);

        assertEquals("ROLE_ADMIN", role);
    }

    @Test
    public  void validateToken_shouldReturnFalse_forInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    public  void validateToken_shouldReturnFalse_forExpiredToken() {
        // set expiration to 1 ms
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 1L);

        String token = jwtUtil.generateToken("101", "USER");

        try {
            Thread.sleep(5);
        } catch (InterruptedException ignored) {}

        boolean isValid = jwtUtil.validateToken(token);

        assertFalse(isValid);
    }
	
	
}
