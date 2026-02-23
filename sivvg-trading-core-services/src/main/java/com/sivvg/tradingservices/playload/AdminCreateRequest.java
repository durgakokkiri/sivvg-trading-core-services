package com.sivvg.tradingservices.playload;

import static com.sivvg.tradingservices.util.MessageConstants.INVALID_PHONE;
import static com.sivvg.tradingservices.util.MessageConstants.PHONE_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.USERNAME_LENGTH;
import static com.sivvg.tradingservices.util.MessageConstants.USERNAME_REQUIRED;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AdminCreateRequest {

	@NotBlank(message = USERNAME_REQUIRED)
	@Size(min = 3, max = 50, message = USERNAME_LENGTH)
	private String username;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = PHONE_REQUIRED)
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = INVALID_PHONE)
	private String phone;

	private String passwordHash;

	public AdminCreateRequest() {
	}

	public AdminCreateRequest(String username, String email, String phone, String passwordHash) {
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.passwordHash = passwordHash;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public String toString() {
		return "AdminCreateRequest{" + "username='" + username + '\'' + ", email='" + email + '\'' + ", phone='" + phone
				+ '\'' + '}';
	}
}
