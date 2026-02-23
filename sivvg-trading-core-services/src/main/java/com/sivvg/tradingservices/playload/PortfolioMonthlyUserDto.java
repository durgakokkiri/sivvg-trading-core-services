package com.sivvg.tradingservices.playload;

public class PortfolioMonthlyUserDto {

	private String username;
	private String month;
	private int totalPnl;
	private int uniqueSectorCount;
	private double pnlPercentage;

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

	public double getPnlPercentage() {
		return pnlPercentage;
	}

	public void setPnlPercentage(double pnlPercentage) {
		this.pnlPercentage = pnlPercentage;
	}
}
