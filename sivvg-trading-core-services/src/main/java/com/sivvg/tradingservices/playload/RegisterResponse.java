package com.sivvg.tradingservices.playload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponse {
	private String userId;

	@JsonProperty("message")
	private String message;

	public RegisterResponse(String userId, String message) {
		this.userId = userId;
		this.message = message;
	}

	public RegisterResponse() {
	}

	public RegisterResponse(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RegisterResponse [userId=" + userId + ", message=" + message + "]";
	}

}
