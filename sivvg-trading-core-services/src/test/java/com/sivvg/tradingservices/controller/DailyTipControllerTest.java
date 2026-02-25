package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.configuration.TestSecurityConfig;
import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.DailyTipRequest;
import com.sivvg.tradingservices.service.DailyTipService;
import com.sivvg.tradingservices.service.NotificationService;
import com.sivvg.tradingservices.service.YahooFinanceClient;

@WebMvcTest(DailyTipController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class DailyTipControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DailyTipService service;

	@MockitoBean
	private YahooFinanceClient yahooservice;

	@MockitoBean
	private NotificationService notificationservice;

	@Autowired
	private ObjectMapper objectMapper;

	// ==================================================
	// GET ALL TIPS - SUCCESS
	// ==================================================
	@Test
	@WithMockUser(authorities = "ROLE_USER")
	public	void getAllTips_shouldReturnList() throws Exception {

		DailyTipEntity tip = new DailyTipEntity();
		tip.setId(1L);
		tip.setStockCode("INFY");

		when(service.getAllTips()).thenReturn(List.of(tip));

		mockMvc.perform(get("/api/v1/tips/all")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].stockCode").value("INFY"));
	}

	// ==================================================
	// GET ALL TIPS - EMPTY
	// ==================================================
	@Test
    @WithMockUser(authorities = "ROLE_USER")
	public  void getAllTips_shouldReturnNoTipsMessage_whenEmpty() throws Exception {

        when(service.getAllTips()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tips/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("Today no tips provided."));
    }

	// ==================================================
	// GET TIPS BY CATEGORY
	// ==================================================
	@Test
	@WithMockUser(authorities = "ROLE_USER")
	public void getTipsByCategory_shouldReturnFilteredTips() throws Exception {

		DailyTipEntity tip = new DailyTipEntity();
		tip.setStockCode("INFY");
		tip.setCategory("EQUITY");

		when(service.getTipsByCategory("EQUITY")).thenReturn(List.of(tip));

		mockMvc.perform(get("/api/v1/tips/tips").param("category", "EQUITY")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].category").value("EQUITY"));
	}

	// ==================================================
	// GET SINGLE TIP
	// ==================================================
	@Test
	@WithMockUser(authorities = "ROLE_USER")
	public void getTipStatus_shouldReturnTip() throws Exception {

		DailyTipEntity tip = new DailyTipEntity();
		tip.setId(1L);
		tip.setStockCode("INFY");

		when(service.getTipStatus(1L)).thenReturn(tip);

		mockMvc.perform(get("/api/v1/tips/tip/{id}", 1L)).andExpect(status().isOk())
				.andExpect(jsonPath("$.stockCode").value("INFY"));
	}

	// ==================================================
	// UPDATE TIP
	// ==================================================
	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	public	void updateTip_shouldUpdateSuccessfully() throws Exception {

		DailyTipEntity tip = new DailyTipEntity();
		tip.setId(1L);
		tip.setStockCode("INFY");

		DailyTipRequest req = new DailyTipRequest();
		req.setStockCode("INFY");

		when(service.updateTip(eq(1L), any())).thenReturn(tip);

		mockMvc.perform(put("/api/v1/tips/update/{id}", 1L).contentType("application/json")
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(jsonPath("$.stockCode").value("INFY"));
	}

	// ==================================================
	// DELETE TIP
	// ==================================================
	@Test
	@WithMockUser(authorities = "ROLE_ADMIN")
	public void deleteTip_shouldDeleteSuccessfully() throws Exception {

		doNothing().when(service).deleteTip(1L);

		mockMvc.perform(delete("/api/v1/tips/delete/{id}", 1L)).andExpect(status().isNoContent());
	}

}
