package com.sivvg.tradingservices.playload;

import static com.sivvg.tradingservices.util.MessageConstants.DOB_INVALID;
import static com.sivvg.tradingservices.util.MessageConstants.EMAIL_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.GENDER_INVALID;
import static com.sivvg.tradingservices.util.MessageConstants.INVALID_EMAIL;
import static com.sivvg.tradingservices.util.MessageConstants.INVALID_PHONE;
import static com.sivvg.tradingservices.util.MessageConstants.PHONE_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.USERNAME_LENGTH;
import static com.sivvg.tradingservices.util.MessageConstants.USERNAME_REQUIRED;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = USERNAME_REQUIRED)
	@Size(min = 3, max = 50, message = USERNAME_LENGTH)
	private String username;

	@NotBlank(message = EMAIL_REQUIRED)
	@Email(message = INVALID_EMAIL)
	private String email;

	@NotBlank(message = PHONE_REQUIRED)
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = INVALID_PHONE)
	private String phoneNumber;

	@Pattern(regexp = "MALE|FEMALE|OTHER", message = GENDER_INVALID)
	private String gender;

	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = DOB_INVALID)
	private String dob;

	private String role;
	private String status;

	public RegisterRequest(
			@NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "Username must be 3 to 50 characters") String username,
			@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
			@NotBlank(message = "Phone number is required") @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid phone number") String phoneNumber,
			@Pattern(regexp = "MALE|FEMALE|OTHER", message = "Gender must be MALE, FEMALE or OTHER") String gender,
			@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "DOB must be in yyyy-mm-dd format") String dob,
			String role, String status) {
		super();
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.dob = dob;
		this.role = role;
		this.status = status;
	}

	public RegisterRequest() {
		super();
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RegisterRequest [username=" + username + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", gender=" + gender + ", dob=" + dob + ", role=" + role + ", status=" + status + "]";
	}

}
