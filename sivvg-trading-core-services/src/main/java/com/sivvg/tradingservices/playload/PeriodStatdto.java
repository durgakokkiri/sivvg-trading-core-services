package com.sivvg.tradingservices.playload;

public class PeriodStatdto {

	private String period; // 1D, 1W, 1M
	private int totalTips;
	private int successTips;
	private double successRate;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getTotalTips() {
		return totalTips;
	}

	public void setTotalTips(int totalTips) {
		this.totalTips = totalTips;
	}

	public int getSuccessTips() {
		return successTips;
	}

	public void setSuccessTips(int successTips) {
		this.successTips = successTips;
	}

	public double getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(double successRate) {
		this.successRate = successRate;
	}

	public PeriodStatdto(String period, int totalTips, int successTips, double successRate) {
		super();
		this.period = period;
		this.totalTips = totalTips;
		this.successTips = successTips;
		this.successRate = successRate;
	}

	public PeriodStatdto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PeriodStatdto [period=" + period + ", totalTips=" + totalTips + ", successTips=" + successTips
				+ ", successRate=" + successRate + "]";
	}

}
