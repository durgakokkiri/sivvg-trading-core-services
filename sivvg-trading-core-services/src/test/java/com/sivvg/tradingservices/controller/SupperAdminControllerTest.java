package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.exceptions.GlobalExceptionHandler;
import com.sivvg.tradingservices.exceptions.ResourceNotFoundException;
import com.sivvg.tradingservices.playload.AdminCreateRequest;
import com.sivvg.tradingservices.service.AdminService;
import com.sivvg.tradingservices.util.JwtUtil;

@WebMvcTest(SupperAdminController.class)
@Import(GlobalExceptionHandler.class)
public class SupperAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    // -------- SUCCESS CASE --------
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    public void createAdmin_shouldReturnSuccessMessage_whenSuccess() throws Exception {

        // 1️⃣ Create request object
        AdminCreateRequest request = new AdminCreateRequest();
        request.setUsername("admin1");
        request.setEmail("admin1@example.com");
        request.setPhone("9999999999");
        request.setPasswordHash("password123");

        // 2️⃣ Mock service behavior
        when(adminService.createAdmin(any(AdminCreateRequest.class)))
                .thenReturn(request);

        // 3️⃣ Perform MockMvc request and assert
        mockMvc.perform(post("/api/v1/admin/create-admin") // Correct path
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // 201
                .andExpect(content().string("Admin created successfully"));
    }

    // -------- EXCEPTION CASE --------
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    public void createAdmin_shouldReturnNotFound_whenExceptionOccurs() throws Exception {

        AdminCreateRequest request = new AdminCreateRequest(); // empty

        when(adminService.createAdmin(any(AdminCreateRequest.class)))
                .thenThrow(new ResourceNotFoundException("Invalid request"));

        mockMvc.perform(post("/api/super-admin/create-admin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
