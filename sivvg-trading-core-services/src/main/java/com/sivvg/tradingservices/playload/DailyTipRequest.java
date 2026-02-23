package com.sivvg.tradingservices.playload;

public class DailyTipRequest {

	// ===== COMMON FIELDS =====
	private String stockCode;
	private Double stockPrice;

	private String stockType; // BUY / SELL
	private String category; // EQUITY / FUTURES / OPTIONS
	private String planType; // BASIC / STANDARD / PREMIUM / VIP
	private String sectorType;

	// ===== EQUITY / FUTURES TARGETS =====
	private Double t1;
	private Double t2;
	private Double t3;

	// ===== OPTIONS FIELDS =====
	private String optionType; // CE / PE
	private Double strikePrice; // 1300
	private Double atm; // 1320 CE
	private Double otm; // 1340 CE
	private Double targetPrice; // 1340

	// ===== GETTERS & SETTERS =====

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public Double getT1() {
		return t1;
	}

	public void setT1(Double t1) {
		this.t1 = t1;
	}

	public Double getT2() {
		return t2;
	}

	public void setT2(Double t2) {
		this.t2 = t2;
	}

	public Double getT3() {
		return t3;
	}

	public void setT3(Double t3) {
		this.t3 = t3;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getSectorType() {
		return sectorType;
	}

	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public Double getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(double d) {
		this.strikePrice = d;
	}

	public Double getAtm() {
		return atm;
	}

	public void setAtm(double i) {
		this.atm = i;
	}

	public Double getOtm() {
		return otm;
	}

	public void setOtm(Double otm) {
		this.otm = otm;
	}

	public Double getTargetPrice() {
		return targetPrice;
	}

	public void setTargetPrice(Double targetPrice) {
		this.targetPrice = targetPrice;
	}

	public DailyTipRequest(String stockCode, Double stockPrice, String stockType, String category, String planType,
			String sectorType, Double t1, Double t2, Double t3, String optionType, Double strikePrice, Double atm,
			Double otm, Double targetPrice) {
		super();
		this.stockCode = stockCode;
		this.stockPrice = stockPrice;
		this.stockType = stockType;
		this.category = category;
		this.planType = planType;
		this.sectorType = sectorType;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.optionType = optionType;
		this.strikePrice = strikePrice;
		this.atm = atm;
		this.otm = otm;
		this.targetPrice = targetPrice;
	}

	public DailyTipRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DailyTipRequest [stockCode=" + stockCode + ", stockPrice=" + stockPrice + ", stockType=" + stockType
				+ ", category=" + category + ", planType=" + planType + ", sectorType=" + sectorType + ", t1=" + t1
				+ ", t2=" + t2 + ", t3=" + t3 + ", optionType=" + optionType + ", strikePrice=" + strikePrice + ", atm="
				+ atm + ", otm=" + otm + ", targetPrice=" + targetPrice + "]";
	}
}
