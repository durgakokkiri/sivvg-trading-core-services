package com.sivvg.tradingservices.playload;

import static com.sivvg.tradingservices.util.MessageConstants.CONFIRM_PASSWORD_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.NEW_PASSWORD_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.PASSWORD_MIN_LENGTH;
import static com.sivvg.tradingservices.util.MessageConstants.USERID_REQUIRED;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

	@NotBlank(message = USERID_REQUIRED)
	private String userId;

	@NotBlank(message = NEW_PASSWORD_REQUIRED)
	@Size(min = 6, message = PASSWORD_MIN_LENGTH)
	private String newPassword;

	@NotBlank(message = CONFIRM_PASSWORD_REQUIRED)
	@Size(min = 6, message = PASSWORD_MIN_LENGTH)
	private String confirmPassword;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public ResetPasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResetPasswordRequest(String userId, String newPassword, String confirmPassword) {
		super();
		this.userId = userId;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "ResetPasswordRequest [userId=" + userId + ", newPassword=" + newPassword + ", confirmPassword="
				+ confirmPassword + "]";
	}

}
