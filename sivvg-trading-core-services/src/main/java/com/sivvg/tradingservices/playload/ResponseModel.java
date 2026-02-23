package com.sivvg.tradingservices.playload;

public class ResponseModel {

	private Double regularMarketPrice;
	private Double regularMarketOpen;
	private Double regularMarketDayHigh;
	private Double regularMarketDayLow;

	public Double getRegularMarketPrice() {
		return regularMarketPrice;
	}

	public void setRegularMarketPrice(Double regularMarketPrice) {
		this.regularMarketPrice = regularMarketPrice;
	}

	public Double getRegularMarketOpen() {
		return regularMarketOpen;
	}

	public void setRegularMarketOpen(Double regularMarketOpen) {
		this.regularMarketOpen = regularMarketOpen;
	}

	public Double getRegularMarketDayHigh() {
		return regularMarketDayHigh;
	}

	public void setRegularMarketDayHigh(Double regularMarketDayHigh) {
		this.regularMarketDayHigh = regularMarketDayHigh;
	}

	public Double getRegularMarketDayLow() {
		return regularMarketDayLow;
	}

	public void setRegularMarketDayLow(Double regularMarketDayLow) {
		this.regularMarketDayLow = regularMarketDayLow;
	}

	public ResponseModel(Double regularMarketPrice, Double regularMarketOpen, Double regularMarketDayHigh,
			Double regularMarketDayLow) {
		super();
		this.regularMarketPrice = regularMarketPrice;
		this.regularMarketOpen = regularMarketOpen;
		this.regularMarketDayHigh = regularMarketDayHigh;
		this.regularMarketDayLow = regularMarketDayLow;
	}

	public ResponseModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ResponseModel [regularMarketPrice=" + regularMarketPrice + ", regularMarketOpen=" + regularMarketOpen
				+ ", regularMarketDayHigh=" + regularMarketDayHigh + ", regularMarketDayLow=" + regularMarketDayLow
				+ "]";
	}

}
