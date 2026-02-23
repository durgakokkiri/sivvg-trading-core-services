package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.SectorPerformance;
import com.sivvg.tradingservices.repository.DailyTipRepository;

@ExtendWith(MockitoExtension.class)
public class SectoralIndexServiceImplTest {

    @Mock
    private DailyTipRepository repo;

    @InjectMocks
    private SectoralIndexServiceImpl service;

    // =====================================================
    // TEST 1: SECTOR TYPE COUNT
    // =====================================================
    @Test
    public void getTodaySectorTypeCount_shouldReturnSectorCounts() {

        SectorPerformance sp =
                new SectorPerformance("IT", 3L);

        when(repo.countTodayBySectorType(any(), any()))
                .thenReturn(List.of(sp));

        List<SectorPerformance> result =
                service.getTodaySectorTypeCount();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSectorType()).isEqualTo("IT");
        assertThat(result.get(0).getTotalTips()).isEqualTo(3L);

        verify(repo).countTodayBySectorType(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    // =====================================================
    // TEST 2: COMPANIES BY SECTOR
    // =====================================================
    @Test
    public void getTodayCompaniesBySector_shouldReturnCompanies() {

        DailyTipEntity tip = new DailyTipEntity();
        tip.setSectorType("IT");

        when(repo.findTodayBySectorType(
                eq("IT"), any(), any()))
                .thenReturn(List.of(tip));

        List<DailyTipEntity> result =
                service.getTodayCompaniesBySector("it");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSectorType()).isEqualTo("IT");

        verify(repo).findTodayBySectorType(
                eq("IT"),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }
}
