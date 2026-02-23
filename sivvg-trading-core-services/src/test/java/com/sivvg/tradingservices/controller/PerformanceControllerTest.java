package com.sivvg.tradingservices.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.sivvg.tradingservices.model.DailyTipHistoryEntity;
import com.sivvg.tradingservices.playload.CategoryPerformance;
import com.sivvg.tradingservices.repository.DailyTipHistoryRepository;
import com.sivvg.tradingservices.service.PerformanceService;

@WebMvcTest(PerformanceController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
public class PerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerformanceService service;

    @MockBean
    private DailyTipHistoryRepository repo;

    // ✅ ALL DATA (NO FILTER)
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void getHistoricalData_all() throws Exception {

        when(repo.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/pastperformance/all"))
                .andExpect(status().isOk());
    }

    // ✅ ONLY CATEGORY
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void getHistoricalData_byCategory() throws Exception {

        when(repo.findByCategory("EQUITY"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/pastperformance/all")
                .param("category", "EQUITY"))
                .andExpect(status().isOk());
    }

    // ✅ ONLY DATE RANGE
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void getHistoricalData_byDateRange() throws Exception {

        when(repo.findByCreatedAtBetween(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 5, 23, 59, 59)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/pastperformance/all")
                .param("from", "2026-01-01")
                .param("to", "2026-01-05"))
                .andExpect(status().isOk());
    }

    // ✅ CATEGORY + DATE RANGE
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void getHistoricalData_byCategoryAndDate() throws Exception {

        when(repo.findByCategoryAndCreatedAtBetween(
                "EQUITY",
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 5, 23, 59, 59)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/pastperformance/all")
                .param("category", "EQUITY")
                .param("from", "2026-01-01")
                .param("to", "2026-01-05"))
                .andExpect(status().isOk());
    }

    // ✅ CATEGORY PERFORMANCE (PERIOD)
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void getCategoryPerformance() throws Exception {

        when(service.getCategoryPerformance("EQUITY"))
                .thenReturn(new CategoryPerformance());

        mockMvc.perform(get("/api/v1/pastperformance/category")
                .param("category", "EQUITY")
                .param("period", "1W"))
                .andExpect(status().isOk());
    }

    // ✅ 1D
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void getTips1D() throws Exception {

        when(service.getTips1D("EQUITY"))
                .thenReturn(List.of(new DailyTipHistoryEntity()));

        mockMvc.perform(get("/api/v1/pastperformance/1d")
                .param("category", "EQUITY"))
                .andExpect(status().isOk());
    }

    // ✅ 1W
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void getTips1W() throws Exception {

        when(service.getTips1W("EQUITY"))
                .thenReturn(List.of(new DailyTipHistoryEntity()));

        mockMvc.perform(get("/api/v1/pastperformance/1w")
                .param("category", "EQUITY"))
                .andExpect(status().isOk());
    }

    // ✅ 1M
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void getTips1M() throws Exception {

        when(service.getTips1M("EQUITY"))
                .thenReturn(List.of(new DailyTipHistoryEntity()));

        mockMvc.perform(get("/api/v1/pastperformance/1m")
                .param("category", "EQUITY"))
                .andExpect(status().isOk());
    }

    // ✅ BY DATE RANGE
    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void getTipsByDateRange() throws Exception {

        when(service.getTipsByDateRange(
                "EQUITY",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 10)))
                .thenReturn(List.of(new DailyTipHistoryEntity()));

        mockMvc.perform(get("/api/v1/pastperformance/by-date")
                .param("category", "EQUITY")
                .param("from", "2026-01-01")
                .param("to", "2026-01-10"))
                .andExpect(status().isOk());
    }
}
