package com.sivvg.tradingservices.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_tokens")
public class DeviceTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String token;

	private String platform; // ANDROID / IOS

	private LocalDateTime createdAt = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public DeviceTokenEntity(Long id, String token, String platform, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.token = token;
		this.platform = platform;
		this.createdAt = createdAt;
	}

	public DeviceTokenEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DeviceTokenEntity [id=" + id + ", token=" + token + ", platform=" + platform + ", createdAt="
				+ createdAt + "]";
	}

}