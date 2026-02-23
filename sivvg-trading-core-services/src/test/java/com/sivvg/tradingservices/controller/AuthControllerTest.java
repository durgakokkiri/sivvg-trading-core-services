package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.exceptions.GlobalExceptionHandler;
import com.sivvg.tradingservices.playload.*;
import com.sivvg.tradingservices.service.AuthService;
import com.sivvg.tradingservices.service.UserService;
import com.sivvg.tradingservices.util.JwtAuthenticationFilter;


@WebMvcTest(controllers = AuthController.class)
  

@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    // 🔥 VERY IMPORTANT FIX
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // ================= REGISTER =================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void registerUser_success() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("ravalika");
        request.setEmail("ravalika@gmail.com");
        request.setPhoneNumber("9876543210");
        request.setGender("FEMALE");

        RegisterResponse response =
                new RegisterResponse("USR123", "User registered successfully");

        when(userService.registerNewUser(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/user-register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("USR123"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    // ================= LOGIN =================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public  void login_success() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUserId("USR123");
        request.setPassword("password123");

        when(authService.processLoginAndSendOtp(any(LoginRequest.class)))
                .thenReturn(new LoginResponse("OTP Sent"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("OTP sent to registered email & phone. Verify OTP to receive token."));
    }

    // ================= VERIFY OTP =================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public  void verifyOtp_success() throws Exception {

        OtpRequest request = new OtpRequest();
        request.setUserId("USR123");
        request.setOtp("123456");

        when(authService.verifyOtpAndReturnToken("USR123", "123456"))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/api/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    // ================= FORGOT OTP =================
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void forgotSendOtp_success() throws Exception {

        ForgotRequest request = new ForgotRequest();
        request.setEmailorphoneNumber("test@gmail.com");

        when(authService.sendForgotOtp("test@gmail.com"))
                .thenReturn("USR123");   // <-- this will be returned by controller

        mockMvc.perform(post("/api/v1/auth/forgot/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("USR123"));  // <-- MUST match mock
    }


    // ================= RESEND OTP =================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public   void resendOtp_success() throws Exception {

        OtpRequest request = new OtpRequest();
        request.setUserId("USR123");

        doNothing().when(authService).resendOtp("USR123");

        mockMvc.perform(post("/api/v1/auth/resend-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP resent successfully"));
    }

    // ================= RESET PASSWORD =================

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public  void resetPassword_success() throws Exception {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUserId("USR123");
        request.setNewPassword("newpass");
        request.setConfirmPassword("newpass");

        doNothing().when(authService).resetPassword("USR123", "newpass");

        mockMvc.perform(post("/api/v1/auth/forgot/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful"));
    }
}
