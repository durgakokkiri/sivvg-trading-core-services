package com.sivvg.tradingservices.playload;

import static com.sivvg.tradingservices.util.MessageConstants.EMAIL_OR_PHONE_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.INVALID_EMAIL_OR_PHONE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ForgotRequest {

	@NotBlank(message = EMAIL_OR_PHONE_REQUIRED)
	@Pattern(regexp = "^(?:[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+|[6-9][0-9]{9})$", message = INVALID_EMAIL_OR_PHONE)
	private String emailorphoneNumber;

	public String getEmailorphoneNumber() {
		return emailorphoneNumber;
	}

	public void setEmailorphoneNumber(String emailorphoneNumber) {
		this.emailorphoneNumber = emailorphoneNumber;
	}

	public ForgotRequest(String emailorphoneNumber) {
		super();
		this.emailorphoneNumber = emailorphoneNumber;
	}

	public ForgotRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ForgotRequest [emailorphoneNumber=" + emailorphoneNumber + "]";
	}

}
