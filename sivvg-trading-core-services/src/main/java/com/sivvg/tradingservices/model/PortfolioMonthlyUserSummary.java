package com.sivvg.tradingservices.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PORTFOLIO_MONTHLY_USER_SUMMARY")
public class PortfolioMonthlyUserSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	@Column(name = "trade_month", nullable = false)
	private String month;

	private int totalPnl;
	private int uniqueSectorCount;
	private double averagePnlPercentage;

	public PortfolioMonthlyUserSummary() {
	}

	public PortfolioMonthlyUserSummary(Long id, String username, String month, int totalPnl, int uniqueSectorCount,
			double averagePnlPercentage) {
		super();
		this.id = id;
		this.username = username;
		this.month = month;
		this.totalPnl = totalPnl;
		this.uniqueSectorCount = uniqueSectorCount;
		this.averagePnlPercentage = averagePnlPercentage;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getTotalPnl() {
		return totalPnl;
	}

	public void setTotalPnl(int totalPnl) {
		this.totalPnl = totalPnl;
	}

	public int getUniqueSectorCount() {
		return uniqueSectorCount;
	}

	public void setUniqueSectorCount(int uniqueSectorCount) {
		this.uniqueSectorCount = uniqueSectorCount;
	}

	public double getAveragePnlPercentage() {
		return averagePnlPercentage;
	}

	public void setAveragePnlPercentage(double averagePnlPercentage) {
		this.averagePnlPercentage = averagePnlPercentage;
	}

	@Override
	public String toString() {
		return "PortfolioMonthlyUserSummary [id=" + id + ", username=" + username + ", month=" + month + ", totalPnl="
				+ totalPnl + ", uniqueSectorCount=" + uniqueSectorCount + ", averagePnlPercentage="
				+ averagePnlPercentage + "]";
	}

}
