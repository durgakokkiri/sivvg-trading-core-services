package com.sivvg.tradingservices.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.sivvg.tradingservices.util.JwtUtil;

@TestConfiguration
public class TestSecurityConfig {

    /**
     * Dummy JwtUtil bean.
     * Only required so Spring context can load.
     * JWT logic is NOT executed because filters are disabled.
     */
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
