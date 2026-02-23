package com.sivvg.tradingservices.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.sivvg.tradingservices.configuration.TestSecurityConfig;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)   // 🔥 Disable JWT / filters
@Import(TestSecurityConfig.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ SUCCESS CASE ONLY
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void home_success() throws Exception {

        mockMvc.perform(get("/api/v1/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Welcome to SIVVG Home - authenticated!"));
    }
}
