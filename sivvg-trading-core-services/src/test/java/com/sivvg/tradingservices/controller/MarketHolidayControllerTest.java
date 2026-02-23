package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.sivvg.tradingservices.service.MarketHolidayService;
import com.sivvg.tradingservices.util.JwtAuthenticationFilter;

@WebMvcTest(controllers = MarketHolidayController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MarketHolidayControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MarketHolidayService marketHolidayService;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	// =========================
	// /today -> true
	// =========================
	@Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void checkTodayHoliday_shouldReturnTrue() throws Exception {

        when(marketHolidayService.isMarketHoliday()).thenReturn(true);

        mockMvc.perform(get("/api/v1/holidays/today"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

      
    }

	// =========================
	// /today -> false
	// =========================
	@Test
    @WithMockUser(username = "user", roles = {"USER"})
    public  void checkTodayHoliday_shouldReturnFalse() throws Exception {

        when(marketHolidayService.isMarketHoliday()).thenReturn(false);

        mockMvc.perform(get("/api/v1/holidays/today"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

       
    }

	// =========================
	// /check?date=2026-01-26
	// =========================
	@Test
	@WithMockUser(username = "user", roles = { "USER" })
	public void checkHoliday_withDate_shouldReturnFalse() throws Exception {

		LocalDate date = LocalDate.of(2026, 1, 26);

		when(marketHolidayService.isMarketHoliday(date)).thenReturn(false);

		mockMvc.perform(get("/api/v1/holidays/check").param("date", "2026-01-26")).andExpect(status().isOk())
				.andExpect(content().string("false"));

	}

	// =========================
	// /check (without date)
	// =========================
	@Test
    @WithMockUser(username = "user", roles = {"USER"})
    public  void checkHoliday_withoutDate_shouldReturnTrue() throws Exception {

        when(marketHolidayService.isMarketHoliday(any(LocalDate.class)))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/holidays/check"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

       
    }
}
