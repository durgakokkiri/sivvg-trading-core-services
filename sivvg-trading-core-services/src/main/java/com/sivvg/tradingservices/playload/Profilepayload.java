package com.sivvg.tradingservices.playload;

public class Profilepayload {
	private long id;
	private String userId;
	private String passwordHash;
	private String phoneNumber;
	private String email;

	public Profilepayload(long id, String userId, String passwordHash, String phoneNumber, String email) {
		super();
		this.id = id;
		this.userId = userId;
		this.passwordHash = passwordHash;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public Profilepayload() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Profilepayload [id=" + id + ", userId=" + userId + ", passwordHash=" + passwordHash + ", phoneNumber="
				+ phoneNumber + ", email=" + email + "]";
	}

}
