package com.sivvg.tradingservices.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.sivvg.tradingservices.configuration.TestSecurityConfig;
import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.service.SectoralIndexService;

@WebMvcTest(SectoralIndexController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 disable JWT / filters
@Import(TestSecurityConfig.class)
public class SectoralIndexControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SectoralIndexService service;

	// ✅ TYPE COUNT
	@Test
	@WithMockUser(authorities = "ROLE_USER")
	public void getTodaySectorTypeCount_success() throws Exception {

		when(service.getTodaySectorTypeCount()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/v1/sectors/type-count")).andExpect(status().isOk());
	}

	// ✅ COMPANIES BY SECTOR
	@Test
	@WithMockUser(authorities = "ROLE_USER")
	public	void getTodayCompaniesBySector_success() throws Exception {

		when(service.getTodayCompaniesBySector("PHARMA")).thenReturn(List.of(new DailyTipEntity()));

		mockMvc.perform(get("/api/v1/sectors/companies").param("sectorType", "PHARMA")).andExpect(status().isOk());
	}
}
