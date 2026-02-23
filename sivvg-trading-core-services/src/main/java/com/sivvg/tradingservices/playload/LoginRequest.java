package com.sivvg.tradingservices.playload;

import static com.sivvg.tradingservices.util.MessageConstants.PASSWORD_MIN_LENGTH;
import static com.sivvg.tradingservices.util.MessageConstants.PASSWORD_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.USERID_REQUIRED;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

	@NotBlank(message = USERID_REQUIRED)

	private String userId;

	@NotBlank(message = PASSWORD_REQUIRED)
	@Size(min = 6, message = PASSWORD_MIN_LENGTH)
	private String password;

	// getters/setters
	public LoginRequest(@NotBlank String userId, @NotBlank String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	public LoginRequest() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
