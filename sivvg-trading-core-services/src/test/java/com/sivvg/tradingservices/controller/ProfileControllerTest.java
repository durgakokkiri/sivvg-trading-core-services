package com.sivvg.tradingservices.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sivvg.tradingservices.exceptions.ResourceNotFoundException;
import com.sivvg.tradingservices.playload.Profilepayload;
import com.sivvg.tradingservices.service.Profileservice;
import com.sivvg.tradingservices.util.JwtUtil;

@WebMvcTest(controllers = ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProfileControllerTest {
	
	
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private Profileservice profileservice;

	@MockitoBean
	private JwtUtil jwtUtil;

	// ✅ Test case 1: Profile found
	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void getProfile_success() throws Exception {
		Profilepayload payload = new Profilepayload();
		payload.setUserId("U123");
		payload.setEmail("test@gmail.com");
		payload.setPhoneNumber("9876543210");
		payload.setPasswordHash("******");

		when(profileservice.getProfileByUserId("U123")).thenReturn(payload);

		// Correct URL: /api/v1/profile/{userId} (remove extra /user)
		mockMvc.perform(get("/api/v1/profile/{userId}", "U123").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.userId").value("U123"))
				.andExpect(jsonPath("$.email").value("test@gmail.com"))
				.andExpect(jsonPath("$.phoneNumber").value("9876543210"))
				.andExpect(jsonPath("$.passwordHash").value("******"));
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	public void getProfile_userNotFound() throws Exception {

	    when(profileservice.getProfileByUserId("INVALID"))
	            .thenThrow(new ResourceNotFoundException("User not found"));

	    mockMvc.perform(get("/api/v1/profile/{userId}", "INVALID")
	                    .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.message").value("User not found"));
	}

}
