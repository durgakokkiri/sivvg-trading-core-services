package com.sivvg.tradingservices.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PORTFOLIO_DAILY_RECORD")
public class PortfolioDailyRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "trade_date")
	private String tradeDate;

	@Column(name = "trade_month", nullable = false)
	private String month;

	@Column(name = "day_number")
	private int dayNumber;

	@Column(name = "pnl")
	private double pnl;

	@Column(name = "pnl_percentage")
	private double pnlPercentage;

	@Column(name = "sector")
	private String sector;

	public PortfolioDailyRecord() {
	}

	public PortfolioDailyRecord(Long id, String username, String tradeDate, String month, int dayNumber, double pnl,
			double pnlPercentage, String sector) {
		this.id = id;
		this.username = username;
		this.tradeDate = tradeDate;
		this.month = month;
		this.dayNumber = dayNumber;
		this.pnl = pnl;
		this.pnlPercentage = pnlPercentage;
		this.sector = sector;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	public double getPnl() {
		return pnl;
	}

	public void setPnl(double pnl) {
		this.pnl = pnl;
	}

	public double getPnlPercentage() {
		return pnlPercentage;
	}

	public void setPnlPercentage(double pnlPercentage) {
		this.pnlPercentage = pnlPercentage;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	@Override
	public String toString() {
		return "PortfolioDailyRecord [id=" + id + ", username=" + username + ", tradeDate=" + tradeDate + ", month="
				+ month + ", dayNumber=" + dayNumber + ", pnl=" + pnl + ", pnlPercentage=" + pnlPercentage + ", sector="
				+ sector + "]";
	}

}
