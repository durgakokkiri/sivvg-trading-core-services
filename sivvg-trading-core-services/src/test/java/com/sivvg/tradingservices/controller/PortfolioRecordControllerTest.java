package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.configuration.TestSecurityConfig;
import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
import com.sivvg.tradingservices.service.PortfolioRecordService;

@WebMvcTest(PortfolioRecordController.class)
@AutoConfigureMockMvc(addFilters = false)   // 🔥 disable JWT / filters
@Import(TestSecurityConfig.class)
public class PortfolioRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PortfolioRecordService recordService;

    // ---------- DAILY ----------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void getAllDaily_success() throws Exception {

        when(recordService.getAllDailyRecords())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/records/daily"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void getDailyById_success() throws Exception {

        when(recordService.getDailyRecordById(1L))
                .thenReturn(new PortfolioDailyRecord());

        mockMvc.perform(get("/api/v1/records/daily/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void updateDaily_success() throws Exception {

        PortfolioDailyRecord record = new PortfolioDailyRecord();

        when(recordService.updateDailyRecord(eq(1L), any()))
                .thenReturn(record);

        mockMvc.perform(put("/api/v1/records/daily/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void patchDaily_success() throws Exception {

        Map<String, Object> updates = new HashMap<>();
        updates.put("profit", 1000);

        when(recordService.patchDailyRecord(eq(1L), any()))
                .thenReturn(new PortfolioDailyRecord());

        mockMvc.perform(patch("/api/v1/records/daily/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void deleteDaily_success() throws Exception {

        doNothing().when(recordService).deleteDailyRecord(1L);

        mockMvc.perform(delete("/api/v1/records/daily/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Daily record deleted successfully"));
    }

    // ---------- MONTHLY ----------

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void getMonthly_success() throws Exception {

        when(recordService.getAllMonthlySummaries())
                .thenReturn(List.of(new PortfolioMonthlyUserSummary()));

        mockMvc.perform(get("/api/v1/records/monthly"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public  void deleteMonthly_success() throws Exception {

        doNothing().when(recordService).deleteMonthlySummary(1L);

        mockMvc.perform(delete("/api/v1/records/monthly/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Monthly summary deleted successfully"));
    }
}
