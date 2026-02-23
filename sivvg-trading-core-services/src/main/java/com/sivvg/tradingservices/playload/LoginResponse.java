package com.sivvg.tradingservices.playload;

public class LoginResponse {
	private String message;

	// no token yet; token returned after OTP verification
	public LoginResponse(String message) {
		this.message = message;
	}

	public LoginResponse() {
		super();
	}

	// getter
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
