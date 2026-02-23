package com.sivvg.tradingservices.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String stockCode;

	// T1 / T2 / T3 / ATM / OTM / TARGET
	private String targetType;

	private Double price;

	private String category; // EQUITY / FUTURES / OPTIONS

	@Column(length = 500)
	private String message;

	private boolean readStatus = false;

	private LocalDateTime createdAt = LocalDateTime.now();

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

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public NotificationEntity(Long id, String stockCode, String targetType, Double price, String category,
			String message, boolean readStatus, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.stockCode = stockCode;
		this.targetType = targetType;
		this.price = price;
		this.category = category;
		this.message = message;
		this.readStatus = readStatus;
		this.createdAt = createdAt;
	}

	public NotificationEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "NotificationEntity [id=" + id + ", stockCode=" + stockCode + ", targetType=" + targetType + ", price="
				+ price + ", category=" + category + ", message=" + message + ", readStatus=" + readStatus
				+ ", createdAt=" + createdAt + "]";
	}

}