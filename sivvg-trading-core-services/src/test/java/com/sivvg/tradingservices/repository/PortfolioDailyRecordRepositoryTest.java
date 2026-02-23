package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.PortfolioDailyRecord;

@DataJpaTest
public class PortfolioDailyRecordRepositoryTest {

    @Autowired
    private PortfolioDailyRecordRepository portfolioDailyRecordRepository;

    // =====================================================
    // Helper : Create PortfolioDailyRecord
    // =====================================================
    private PortfolioDailyRecord createRecord() {

        PortfolioDailyRecord record = new PortfolioDailyRecord();
        record.setUsername("testuser");
        record.setTradeDate("2026-01-15");
        record.setMonth("JAN");
        record.setDayNumber(15);
        record.setPnl(1500.00);
        record.setPnlPercentage(3.5);
        record.setSector("IT");

        return record;
    }

    // =====================================================
    // TEST 1 : Save record
    // =====================================================
    @Test
    public void save_shouldPersistPortfolioDailyRecord() {

        PortfolioDailyRecord saved =
                portfolioDailyRecordRepository.save(createRecord());

        assertThat(saved.getId()).isNotNull();
    }

    // =====================================================
    // TEST 2 : findAll
    // =====================================================
    @Test
    public void findAll_shouldReturnPortfolioDailyRecords() {

        portfolioDailyRecordRepository.save(createRecord());

        assertThat(portfolioDailyRecordRepository.findAll()).hasSize(1);
    }
}
