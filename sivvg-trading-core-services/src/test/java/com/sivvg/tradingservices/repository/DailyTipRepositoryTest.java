package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.SectorPerformance;

@DataJpaTest
public class DailyTipRepositoryTest {

    @Autowired
    private DailyTipRepository dailyTipRepository;

    // =====================================================
    // Helper: Create FULLY VALID DailyTipEntity
    // =====================================================
    private DailyTipEntity createDailyTip(
            String stockCode,
            String sectorType,
            LocalDateTime createdAt
    ) {
        DailyTipEntity tip = new DailyTipEntity();

        // ---- REQUIRED FIELDS (based on your entity style) ----
        tip.setStockCode(stockCode);
        tip.setStockPrice(100.0);

        tip.setT1(105.0);
        tip.setT2(110.0);
        tip.setT3(115.0);

        tip.setStockType("BUY");
        tip.setCategory("EQUITY");
        tip.setPlanType("BASIC");
        tip.setSectorType(sectorType);

        tip.setT1Reached(false);
        tip.setT2Reached(false);
        tip.setT3Reached(false);

        tip.setCurrentPrice(100.0);
        tip.setPriceDifference(0.0);
        tip.setCreatedAt(createdAt);

        return dailyTipRepository.save(tip);
    }

    // =====================================================
    // TEST 1: findByStockCode
    // =====================================================
    @Test
    public void findByStockCode_shouldReturnTip() {

        createDailyTip("TCS", "IT", LocalDateTime.now());

        DailyTipEntity result =
                dailyTipRepository.findByStockCode("TCS");

        assertThat(result).isNotNull();
        assertThat(result.getStockCode()).isEqualTo("TCS");
    }

    // =====================================================
    // TEST 2: existsByStockCode
    // =====================================================
    @Test
    public  void existsByStockCode_shouldReturnTrue() {

        createDailyTip("INFY", "IT", LocalDateTime.now());

        boolean exists =
                dailyTipRepository.existsByStockCode("INFY");

        assertThat(exists).isTrue();
    }

    // =====================================================
    // TEST 3: findByCreatedAtBetween
    // =====================================================
    @Test
    public  void findByCreatedAtBetween_shouldReturnRecordsInRange() {

        LocalDateTime now = LocalDateTime.now();

        createDailyTip("SBIN", "BANK", now.minusDays(3));
        createDailyTip("HDFCBANK", "BANK", now.minusDays(1));
        createDailyTip("AXIS", "BANK", now.plusDays(1));

        List<DailyTipEntity> result =
                dailyTipRepository.findByCreatedAtBetween(
                        now.minusDays(2),
                        now
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStockCode()).isEqualTo("HDFCBANK");
    }

    // =====================================================
    // TEST 4: countTodayBySectorType
    // =====================================================
    @Test
    public  void countTodayBySectorType_shouldReturnSectorCounts() {

        LocalDateTime start = LocalDateTime.now().minusHours(12);
        LocalDateTime end = LocalDateTime.now().plusHours(12);

        createDailyTip("TCS", "IT", LocalDateTime.now());
        createDailyTip("INFY", "IT", LocalDateTime.now());
        createDailyTip("SBIN", "BANK", LocalDateTime.now());

        List<SectorPerformance> result =
                dailyTipRepository.countTodayBySectorType(start, end);

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(SectorPerformance::getSectorType)
                .containsExactlyInAnyOrder("IT", "BANK");
    }

    // =====================================================
    // TEST 5: findTodayBySectorType
    // =====================================================
    @Test
    public void findTodayBySectorType_shouldReturnOnlySectorRecords() {

        LocalDateTime start = LocalDateTime.now().minusHours(12);
        LocalDateTime end = LocalDateTime.now().plusHours(12);

        createDailyTip("TCS", "IT", LocalDateTime.now());
        createDailyTip("INFY", "IT", LocalDateTime.now());
        createDailyTip("SBIN", "BANK", LocalDateTime.now());

        List<DailyTipEntity> result =
                dailyTipRepository.findTodayBySectorType(
                        "IT",
                        start,
                        end
                );

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(DailyTipEntity::getSectorType)
                .containsOnly("IT");
    }
}
