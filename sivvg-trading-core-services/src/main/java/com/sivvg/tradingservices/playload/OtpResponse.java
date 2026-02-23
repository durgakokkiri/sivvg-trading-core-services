package com.sivvg.tradingservices.playload;

public class OtpResponse {

	private String token; // JWT token on successful verification
	private String message;

	public OtpResponse(String token, String message) {
		this.token = token;
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "OtpResponse [token=" + token + ", message=" + message + "]";
	}

	// getters/setters
}
