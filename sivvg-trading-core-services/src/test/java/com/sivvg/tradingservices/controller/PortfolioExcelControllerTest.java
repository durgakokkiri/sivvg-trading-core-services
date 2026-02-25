package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.configuration.TestSecurityConfig;
import com.sivvg.tradingservices.exceptions.PortfolioFileValidationException;
import com.sivvg.tradingservices.playload.PortfolioUploadResponseDTO;
import com.sivvg.tradingservices.service.PortfolioExcelService;

@WebMvcTest(PortfolioExcelController.class)
@AutoConfigureMockMvc(addFilters = false)   // 🔥 Disable JWT / filters
@Import(TestSecurityConfig.class)
public class PortfolioExcelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PortfolioExcelService excelService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ SUCCESS CASE (VALID EXCEL FILE)
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public  void uploadExcel_success() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "portfolio.xlsx",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "dummy excel content".getBytes()
        );

        PortfolioUploadResponseDTO response = new PortfolioUploadResponseDTO();
        when(excelService.uploadExcel(any())).thenReturn(response);

        mockMvc.perform(
                multipart("/api/v1/excel/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk());
    }

    // ❌ EMPTY FILE
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void uploadExcel_emptyFile() throws Exception {

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "portfolio.xlsx",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                new byte[0]
        );

        mockMvc.perform(
                multipart("/api/v1/excel/upload")
                        .file(emptyFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isBadRequest());
    }

    // ❌ INVALID FILE EXTENSION
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void uploadExcel_invalidFileExtension() throws Exception {

        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "portfolio.pdf",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "invalid content".getBytes()
        );

        mockMvc.perform(
                multipart("/api/v1/excel/upload")
                        .file(invalidFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
        .andExpect(status().isBadRequest());
    }
}
