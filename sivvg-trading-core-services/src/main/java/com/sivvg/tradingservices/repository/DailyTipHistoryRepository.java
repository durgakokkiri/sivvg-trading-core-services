package com.sivvg.tradingservices.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.DailyTipHistoryEntity;

@Repository
public interface DailyTipHistoryRepository extends JpaRepository<DailyTipHistoryEntity, Long> {

	// 🔹 Category + DateTime Range
	List<DailyTipHistoryEntity> findByCategoryAndCreatedAtBetween(String category, LocalDateTime from,
			LocalDateTime to);

	// 🔹 Only DateTime Range
	List<DailyTipHistoryEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

	// 🔹 Only Category
	List<DailyTipHistoryEntity> findByCategory(String category);
}
