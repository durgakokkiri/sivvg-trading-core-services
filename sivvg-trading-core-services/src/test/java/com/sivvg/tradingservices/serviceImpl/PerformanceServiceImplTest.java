package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.model.DailyTipHistoryEntity;
import com.sivvg.tradingservices.playload.CategoryPerformance;
import com.sivvg.tradingservices.playload.PeriodStatdto;
import com.sivvg.tradingservices.repository.DailyTipHistoryRepository;
@ExtendWith(MockitoExtension.class)
public class PerformanceServiceImplTest {

    @Mock
    private DailyTipHistoryRepository repo;

    @InjectMocks
    private PerformanceServiceImpl service;

    @Test
    public void getCategoryPerformance_shouldReturnCorrectStats() {

        // ---------- Arrange ----------
        DailyTipHistoryEntity successTip = new DailyTipHistoryEntity();
        successTip.setT1Reached(true);

        DailyTipHistoryEntity failedTip = new DailyTipHistoryEntity();
        failedTip.setT1Reached(false);
        failedTip.setT2Reached(false);
        failedTip.setT3Reached(false);
        failedTip.setTargetReached(false);

        when(repo.findByCategoryAndCreatedAtBetween(
                anyString(), any(), any()
        )).thenReturn(List.of(successTip, failedTip));

        // ---------- Act ----------
        CategoryPerformance result =
                service.getCategoryPerformance("EQUITY");

        // ---------- Assert ----------
        assertThat(result.getCategory()).isEqualTo("EQUITY");

        List<PeriodStatdto> periods = result.getPeriods();
        assertThat(periods).hasSize(3); // 1D, 1W, 1M

        // ---- Check 1D ----
        PeriodStatdto stat1D = periods.get(0);
        assertThat(stat1D.getPeriod()).isEqualTo("1D");
        assertThat(stat1D.getTotalTips()).isEqualTo(2);
        assertThat(stat1D.getSuccessTips()).isEqualTo(1);
        assertThat(stat1D.getSuccessRate()).isEqualTo(50.0);

        // ---- Check 1W ----
        PeriodStatdto stat1W = periods.get(1);
        assertThat(stat1W.getPeriod()).isEqualTo("1W");

        // ---- Check 1M ----
        PeriodStatdto stat1M = periods.get(2);
        assertThat(stat1M.getPeriod()).isEqualTo("1M");
    }
}
