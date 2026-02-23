package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.DailyTipHistoryEntity;

@DataJpaTest
public class DailyTipHistoryRepositoryTest {

    @Autowired
    private DailyTipHistoryRepository dailyTipHistoryRepository;

    // =====================================================
    // Helper: Create FULLY VALID DailyTipHistoryEntity
    // =====================================================
    private DailyTipHistoryEntity createHistory(
            String category,
            LocalDateTime createdAt
    ) {
        DailyTipHistoryEntity entity = new DailyTipHistoryEntity();

        // 🔴 REQUIRED BY VALIDATION
        entity.setStockCode("TCS");
        entity.setStockPrice(100.0);

        entity.setT1(105.0);
        entity.setT2(110.0);
        entity.setT3(115.0);

        entity.setStockType("BUY");
        entity.setCategory(category);
        entity.setPlanType("BASIC");
        entity.setSectorType("IT");

        // 🔴 REQUIRED (nullable = false)
        entity.setT1Reached(false);
        entity.setT2Reached(false);
        entity.setT3Reached(false);

        // Optional but safe
        entity.setCurrentPrice(100.0);
        entity.setPriceDifference(0.0);
        entity.setCreatedAt(createdAt);

        return dailyTipHistoryRepository.save(entity);
    }

    // =====================================================
    // TEST 1: Category + Date Range
    // =====================================================
    @Test
    public  void findByCategoryAndCreatedAtBetween_shouldReturnMatchingRecords() {

        LocalDateTime now = LocalDateTime.now();

        createHistory("EQUITY", now.minusDays(2));
        createHistory("EQUITY", now.minusDays(1));
        createHistory("FUTURES", now.minusDays(1));

        List<DailyTipHistoryEntity> result =
                dailyTipHistoryRepository.findByCategoryAndCreatedAtBetween(
                        "EQUITY",
                        now.minusDays(3),
                        now
                );

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(DailyTipHistoryEntity::getCategory)
                .containsOnly("EQUITY");
    }

    // =====================================================
    // TEST 2: Date Range Only
    // =====================================================
    @Test
    public void findByCreatedAtBetween_shouldReturnRecordsInRange() {

        LocalDateTime now = LocalDateTime.now();

        createHistory("EQUITY", now.minusDays(5));
        createHistory("EQUITY", now.minusDays(1));
        createHistory("EQUITY", now.plusDays(1));

        List<DailyTipHistoryEntity> result =
                dailyTipHistoryRepository.findByCreatedAtBetween(
                        now.minusDays(2),
                        now
                );

        assertThat(result).hasSize(1);
    }

    // =====================================================
    // TEST 3: Category Only
    // =====================================================
    @Test
    void findByCategory_shouldReturnOnlyCategoryRecords() {

        createHistory("EQUITY", LocalDateTime.now());
        createHistory("EQUITY", LocalDateTime.now());
        createHistory("OPTIONS", LocalDateTime.now());

        List<DailyTipHistoryEntity> result =
                dailyTipHistoryRepository.findByCategory("EQUITY");

        assertThat(result).hasSize(2);
    }

    // =====================================================
    // TEST 4: No Records
    // =====================================================
    @Test
    public void findByCategory_shouldReturnEmptyList_whenNoMatch() {

        List<DailyTipHistoryEntity> result =
                dailyTipHistoryRepository.findByCategory("CRYPTO");

        assertThat(result).isEmpty();
    }
}
