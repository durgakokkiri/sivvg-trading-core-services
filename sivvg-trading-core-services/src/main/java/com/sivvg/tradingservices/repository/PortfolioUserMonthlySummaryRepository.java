package com.sivvg.tradingservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
@Repository
public interface PortfolioUserMonthlySummaryRepository extends JpaRepository<PortfolioMonthlyUserSummary, Long> {

}
