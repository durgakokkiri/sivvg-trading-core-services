package com.sivvg.tradingservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.PortfolioDailyRecord;
@Repository
public interface PortfolioDailyRecordRepository extends JpaRepository<PortfolioDailyRecord, Long> {

	List<PortfolioDailyRecord> findByUsername(String username);
}
