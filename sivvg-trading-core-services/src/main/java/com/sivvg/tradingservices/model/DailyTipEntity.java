package com.sivvg.tradingservices.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "daily_tips", uniqueConstraints = { @UniqueConstraint(columnNames = "stockCode") })
public class DailyTipEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ===== COMMON FIELDS =====

	@NotBlank(message = "Stock code is required")
	private String stockCode;    //AUROPHARMA

	private Double stockPrice;

	@NotBlank(message = "Stock type is required")// BUY /SELL
	private String stockType;

	@NotBlank(message = "Category is required") //EQUITY/FUTURES/OPTIONS
	private String category;

	@NotBlank(message = "Plan type is required") // BASIC/PREMIUM
	private String planType;

	@NotBlank(message = "Sector type is required")
	private String sectorType;                     //IT/BANK

	// ===== EQUITY / FUTURES TARGETS =====

	private Double t1;
	private Double t2;
	private Double t3;

	@Column(nullable = false)
	private Boolean t1Reached = false;

	@Column(nullable = false)
	private Boolean t2Reached = false;

	@Column(nullable = false)
	private Boolean t3Reached = false;

	
	@Column(name = "previous_price")
	private Double previousPrice = 0.0;

	private LocalDateTime t1ReachedTime;
	private LocalDateTime t2ReachedTime;
	private LocalDateTime t3ReachedTime;

	// ===== OPTIONS FIELDS =====

	private String optionType; // CE / PE
	private Double strikePrice; // 1300

	private Double atm; // 1320 CE
	private Double otm; // 1340 CE

	private Double targetPrice; // 1340

	private Boolean targetReached = false;

	// ===== LIVE TRACKING =====

	private Double currentPrice = 0.0;

	private Double priceDifference = 0.0;

	// ===== TIME FIELDS =====

	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime postedTime;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// ===== CONSTRUCTORS =====

	public DailyTipEntity() {
		super();
	}

	// ===== GETTERS & SETTERS =====

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Boolean getT1Reached() {
		return t1Reached;
	}

	public void setT1Reached(Boolean t1Reached) {
		this.t1Reached = t1Reached;
	}

	public Boolean getT2Reached() {
		return t2Reached;
	}

	public void setT2Reached(Boolean t2Reached) {
		this.t2Reached = t2Reached;
	}

	public Boolean getT3Reached() {
		return t3Reached;
	}

	public void setT3Reached(Boolean t3Reached) {
		this.t3Reached = t3Reached;
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

	public void setStrikePrice(Double strikePrice) {
		this.strikePrice = strikePrice;
	}

	public Double getAtm() {
		return atm;
	}

	public void setAtm(Double atm) {
		this.atm = atm;
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

	public Boolean getTargetReached() {
		return targetReached;
	}

	public void setTargetReached(Boolean targetReached) {
		this.targetReached = targetReached;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Double getPriceDifference() {
		return priceDifference;
	}

	public void setPriceDifference(Double priceDifference) {
		this.priceDifference = priceDifference;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getPostedTime() {
		return postedTime;
	}

	public void setPostedTime(LocalDateTime postedTime) {
		this.postedTime = postedTime;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getT1ReachedTime() {
		return t1ReachedTime;
	}

	public void setT1ReachedTime(LocalDateTime t1ReachedTime) {
		this.t1ReachedTime = t1ReachedTime;
	}

	public LocalDateTime getT2ReachedTime() {
		return t2ReachedTime;
	}

	public void setT2ReachedTime(LocalDateTime t2ReachedTime) {
		this.t2ReachedTime = t2ReachedTime;
	}

	public LocalDateTime getT3ReachedTime() {
		return t3ReachedTime;
	}

	public void setT3ReachedTime(LocalDateTime t3ReachedTime) {
		this.t3ReachedTime = t3ReachedTime;
	}

	public Double getPreviousPrice() {
		return previousPrice;
	}

	public void setPreviousPrice(Double previousPrice) {
		this.previousPrice = previousPrice;
	}

	public DailyTipEntity(Long id, @NotBlank(message = "Stock code is required") String stockCode, Double stockPrice,
			@NotBlank(message = "Stock type is required") String stockType,
			@NotBlank(message = "Category is required") String category,
			@NotBlank(message = "Plan type is required") String planType,
			@NotBlank(message = "Sector type is required") String sectorType, Double t1, Double t2, Double t3,
			Boolean t1Reached, Boolean t2Reached, Boolean t3Reached, Double previousPrice, LocalDateTime t1ReachedTime,
			LocalDateTime t2ReachedTime, LocalDateTime t3ReachedTime, String optionType, Double strikePrice, Double atm,
			Double otm, Double targetPrice, Boolean targetReached, Double currentPrice, Double priceDifference,
			LocalDateTime createdAt, LocalDateTime postedTime, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.stockCode = stockCode;
		this.stockPrice = stockPrice;
		this.stockType = stockType;
		this.category = category;
		this.planType = planType;
		this.sectorType = sectorType;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t1Reached = t1Reached;
		this.t2Reached = t2Reached;
		this.t3Reached = t3Reached;
		this.previousPrice = previousPrice;
		this.t1ReachedTime = t1ReachedTime;
		this.t2ReachedTime = t2ReachedTime;
		this.t3ReachedTime = t3ReachedTime;
		this.optionType = optionType;
		this.strikePrice = strikePrice;
		this.atm = atm;
		this.otm = otm;
		this.targetPrice = targetPrice;
		this.targetReached = targetReached;
		this.currentPrice = currentPrice;
		this.priceDifference = priceDifference;
		this.createdAt = createdAt;
		this.postedTime = postedTime;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "DailyTipEntity [id=" + id + ", stockCode=" + stockCode + ", stockPrice=" + stockPrice + ", stockType="
				+ stockType + ", category=" + category + ", planType=" + planType + ", sectorType=" + sectorType
				+ ", t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + ", t1Reached=" + t1Reached + ", t2Reached=" + t2Reached
				+ ", t3Reached=" + t3Reached + ", previousPrice=" + previousPrice + ", t1ReachedTime=" + t1ReachedTime
				+ ", t2ReachedTime=" + t2ReachedTime + ", t3ReachedTime=" + t3ReachedTime + ", optionType=" + optionType
				+ ", strikePrice=" + strikePrice + ", atm=" + atm + ", otm=" + otm + ", targetPrice=" + targetPrice
				+ ", targetReached=" + targetReached + ", currentPrice=" + currentPrice + ", priceDifference="
				+ priceDifference + ", createdAt=" + createdAt + ", postedTime=" + postedTime + ", updatedAt="
				+ updatedAt + "]";
	}

}
