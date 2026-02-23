package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
import com.sivvg.tradingservices.repository.PortfolioDailyRecordRepository;
import com.sivvg.tradingservices.repository.PortfolioUserMonthlySummaryRepository;

@ExtendWith(MockitoExtension.class)
public class PortfolioRecordServiceImplTest {

    @Mock
    private PortfolioDailyRecordRepository dailyRepo;

    @Mock
    private PortfolioUserMonthlySummaryRepository monthlyRepo;

    @InjectMocks
    private PortfolioRecordServiceImpl service;

    private PortfolioDailyRecord dailyRecord;

    @BeforeEach
    public void setup() {
        dailyRecord = new PortfolioDailyRecord();
        dailyRecord.setUsername("testuser");
        dailyRecord.setTradeDate("2026-01-01");
        dailyRecord.setMonth("JAN");
        dailyRecord.setDayNumber(1);
        dailyRecord.setPnl(500.00);
        dailyRecord.setPnlPercentage(5.5);
        dailyRecord.setSector("IT");
    }

    // ---------- DAILY ----------

    @Test
    public void getAllDailyRecords_shouldReturnList() {
        when(dailyRepo.findAll()).thenReturn(List.of(dailyRecord));

        List<PortfolioDailyRecord> result = service.getAllDailyRecords();

        assertThat(result).hasSize(1);
        verify(dailyRepo).findAll();
    }

    @Test
    public void getDailyRecordById_shouldReturnRecord() {
        when(dailyRepo.findById(1L)).thenReturn(Optional.of(dailyRecord));

        PortfolioDailyRecord result = service.getDailyRecordById(1L);

        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    public void getDailyRecordById_shouldThrowException_whenNotFound() {
        when(dailyRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDailyRecordById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Daily record not found");
    }

    @Test
    public void updateDailyRecord_shouldUpdateAndSave() {
        when(dailyRepo.findById(1L)).thenReturn(Optional.of(dailyRecord));
        when(dailyRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PortfolioDailyRecord updated = new PortfolioDailyRecord();
        updated.setUsername("updatedUser");
        updated.setTradeDate("2026-01-02");
        updated.setMonth("FEB");
        updated.setDayNumber(2);
        updated.setPnl(1000.00);
        updated.setPnlPercentage(10.0);
        updated.setSector("BANK");

        PortfolioDailyRecord result = service.updateDailyRecord(1L, updated);

        assertThat(result.getUsername()).isEqualTo("updatedUser");
        assertThat(result.getPnl()).isEqualTo(1000);
    }

    @Test
    public  void patchDailyRecord_shouldPatchFields() {
        when(dailyRepo.findById(1L)).thenReturn(Optional.of(dailyRecord));
        when(dailyRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> updates = Map.of(
                "username", "patchedUser",
                "pnl", 2000,
                "pnlPercentage", 20.5
        );

        PortfolioDailyRecord result = service.patchDailyRecord(1L, updates);

        assertThat(result.getUsername()).isEqualTo("patchedUser");
        assertThat(result.getPnl()).isEqualTo(2000);
        assertThat(result.getPnlPercentage()).isEqualTo(20.5);
    }

    @Test
    public void deleteDailyRecord_shouldDelete() {
        service.deleteDailyRecord(1L);

        verify(dailyRepo).deleteById(1L);
    }

    // ---------- MONTHLY ----------

    @Test
    public void getAllMonthlySummaries_shouldReturnList() {
        PortfolioMonthlyUserSummary summary = new PortfolioMonthlyUserSummary();
        when(monthlyRepo.findAll()).thenReturn(List.of(summary));

        List<PortfolioMonthlyUserSummary> result =
                service.getAllMonthlySummaries();

        assertThat(result).hasSize(1);
        verify(monthlyRepo).findAll();
    }

    @Test
    public  void deleteMonthlySummary_shouldDelete() {
        service.deleteMonthlySummary(1L);

        verify(monthlyRepo).deleteById(1L);
    }
}
