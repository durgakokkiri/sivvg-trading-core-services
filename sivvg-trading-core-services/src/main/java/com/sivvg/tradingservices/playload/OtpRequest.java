package com.sivvg.tradingservices.playload;

import static com.sivvg.tradingservices.util.MessageConstants.INVALID_OTP;
import static com.sivvg.tradingservices.util.MessageConstants.OTP_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.USERID_REQUIRED;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OtpRequest {

	@NotBlank(message = USERID_REQUIRED)

	private String userId;

	@NotBlank(message = OTP_REQUIRED)
	@Pattern(regexp = "^[0-9]{6}$", message = INVALID_OTP)
	private String otp;

	public OtpRequest(@NotBlank String userId, @NotBlank String otp) {
		super();
		this.userId = userId;
		this.otp = otp;
	}

	public OtpRequest() {
		super();
	}

	// getters/setters
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
