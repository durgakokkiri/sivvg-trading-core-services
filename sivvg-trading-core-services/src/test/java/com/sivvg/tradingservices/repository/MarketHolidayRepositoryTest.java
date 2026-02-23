package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.sivvg.tradingservices.model.MarketHolidayEntity;

@DataJpaTest
@ActiveProfiles("test")
public class MarketHolidayRepositoryTest {

    @Autowired
    private MarketHolidayRepository repo;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void clearDb() {
        repo.deleteAll();
        
    }

    // =========================
    // EXISTS → TRUE CASE
    // =========================
    @Test
    public  void existsByHolidayDate_shouldReturnTrue_whenHolidayExists() {

        // GIVEN
        LocalDate date = LocalDate.of(2026, 1, 26);

        MarketHolidayEntity h = new MarketHolidayEntity();
        h.setHolidayDate(date);
        h.setDescription("Republic Day");
        h.setYear(2026);

        // SAVE
        repo.saveAndFlush(h);     // 🔥 very important
        entityManager.clear();

        // WHEN
        boolean exists = repo.existsByHolidayDate(date);

        // THEN
        assertThat(exists).isTrue();
    }

    // =========================
    // EXISTS → FALSE CASE
    // =========================
    @Test
    public void existsByHolidayDate_shouldReturnFalse_whenHolidayNotExists() {

        // GIVEN
        LocalDate date = LocalDate.of(2026, 8, 15);

        // WHEN
        boolean exists = repo.existsByHolidayDate(date);

        // THEN
        assertThat(exists).isFalse();
    }
}
