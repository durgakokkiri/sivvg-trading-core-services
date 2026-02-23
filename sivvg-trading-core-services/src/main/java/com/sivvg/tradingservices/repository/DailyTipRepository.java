package com.sivvg.tradingservices.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.SectorPerformance;

@Repository
public interface DailyTipRepository extends JpaRepository<DailyTipEntity, Long> {

	DailyTipEntity findByStockCode(String code);

	List<DailyTipEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	boolean existsByStockCode(String stockCode);

	List<DailyTipEntity> findByCategoryIgnoreCase(String category);

	@Query("""
			    SELECT new com.sivvg.tradingservices.playload.SectorPerformance(
			        d.sectorType,
			        COUNT(d)
			    )
			    FROM DailyTipEntity d
			    WHERE d.createdAt BETWEEN :start AND :end
			    GROUP BY d.sectorType
			""")
	List<SectorPerformance> countTodayBySectorType(@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	@Query("""
			    SELECT d
			    FROM DailyTipEntity d
			    WHERE d.sectorType = :sectorType
			      AND d.createdAt BETWEEN :start AND :end
			    ORDER BY d.createdAt DESC
			""")
	List<DailyTipEntity> findTodayBySectorType(@Param("sectorType") String sectorType,
			@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
