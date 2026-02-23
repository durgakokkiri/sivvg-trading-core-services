package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PortfolioUserMonthlySummaryRepositoryTest {

    @Autowired
    private PortfolioUserMonthlySummaryRepository repository;

    // =====================================================
    // Helper : Create PortfolioMonthlyUserSummary
    // =====================================================
    private PortfolioMonthlyUserSummary createSummary() {

        PortfolioMonthlyUserSummary summary = new PortfolioMonthlyUserSummary();
        summary.setUsername("testuser");
        summary.setMonth("JAN");
        summary.setTotalPnl(7500);
        summary.setUniqueSectorCount(3);
        summary.setAveragePnlPercentage(6.8);

        return summary;
    }

    // =====================================================
    // TEST 1 : Save summary
    // =====================================================
    @Test
    public void save_shouldPersistMonthlySummary() {

        PortfolioMonthlyUserSummary saved =
                repository.save(createSummary());

        assertThat(saved.getId()).isNotNull();
    }

    // =====================================================
    // TEST 2 : findAll
    // =====================================================
    @Test
    public void findAll_shouldReturnMonthlySummaries() {

        repository.save(createSummary());

        assertThat(repository.findAll()).hasSize(1);
    }
}
