package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.configuration.TestSecurityConfig;
import com.sivvg.tradingservices.playload.ContactUSRequestDto;
import com.sivvg.tradingservices.service.ContactUSService;

@WebMvcTest(ContactUSController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class ContactUSControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ContactUSService contactService;

	// ✅ SUCCESS CASE
	@Test
	@WithMockUser(username = "user123", roles = "USER")
	public void createContact_success() throws Exception {

		ContactUSRequestDto dto = new ContactUSRequestDto("Need help with trading app", "EMAIL");

		doNothing().when(contactService).createContactRequest(any(ContactUSRequestDto.class), eq("user123"));

		mockMvc.perform(post("/api/v1/contact").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk())
				.andExpect(content().string("Contact request submitted successfully"));

		verify(contactService).createContactRequest(any(ContactUSRequestDto.class), eq("user123"));
	}

	// ❌ VALIDATION ERROR
	@Test
	@WithMockUser(username = "user123", roles = "USER")
	public void createContact_validationError() throws Exception {

		ContactUSRequestDto dto = new ContactUSRequestDto("", "");

		mockMvc.perform(post("/api/v1/contact").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());

		verifyNoInteractions(contactService);
	}
}
