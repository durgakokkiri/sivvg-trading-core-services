package com.sivvg.tradingservices.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.MarketHolidayEntity;

@Repository
public interface MarketHolidayRepository extends JpaRepository<MarketHolidayEntity, Long> {

	boolean existsByHolidayDate(LocalDate holidayDate);
}
