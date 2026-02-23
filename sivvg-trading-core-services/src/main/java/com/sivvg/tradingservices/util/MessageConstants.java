package com.sivvg.tradingservices.util;

public class MessageConstants {

	private MessageConstants() {
	}

	// -------- VALIDATION --------

	public static final String USERNAME_REQUIRED = "Username is required";
	public static final String USERNAME_LENGTH = "Username must be 3 to 50 characters";

	public static final String EMAIL_REQUIRED = "Email is required";
	public static final String INVALID_EMAIL = "Invalid email format";

	public static final String PHONE_REQUIRED = "Phone number is required";
	public static final String INVALID_PHONE = "Invalid phone number";

	public static final String GENDER_INVALID = "Gender must be MALE, FEMALE or OTHER";
	public static final String DOB_INVALID = "DOB must be in yyyy-mm-dd format";

	public static final String ROLE_NOT_FOUND = "Role not found";
	public static final String ROLE_INVALID = "Role must be USER or ADMIN";

	public static final String STATUS_NOT_FOUND = "Status not found";
	public static final String STATUS_INVALID = "Status must be ACTIVE, INACTIVE or BLOCKED";

	// LOGIN
	public static final String USERID_REQUIRED = "UserId is required";
	public static final String INVALID_USERID = "Invalid UserId format";

	public static final String PASSWORD_REQUIRED = "Password is required";
	public static final String PASSWORD_MIN_LENGTH = "Password cannot be less than 6 characters";
	public static final String INVALID_PASSWORD = "Your password is invalid";

	// OTP
	public static final String OTP_REQUIRED = "OTP is required";
	public static final String OTP_NOT_GENERATED = "OTP not generated. Please login again";
	public static final String INVALID_OTP = "Invalid OTP";
	public static final String OTP_EXPIRED = "OTP expired. Please request new OTP";
	public static final String OTP_SENT = "OTP sent to registered email and phone number";

	// ===== OTP =====

	// FORGOT PASSWORD
	public static final String EMAIL_OR_PHONE_REQUIRED = "Email or phone number is required";
	public static final String INVALID_EMAIL_OR_PHONE = "Enter valid email or Indian phone number";

	// RESET PASSWORD

	public static final String NEW_PASSWORD_REQUIRED = "New password is required";
	public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required";
	public static final String PASSWORDS_NOT_MATCH = "Passwords do not match";

	// -------- USER --------
	public static final String USER_NOT_FOUND = "User not found";
	public static final String USER_ALREADY_EXISTS = "User already exists";

	// -------- COMMON --------
	public static final String SOMETHING_WENT_WRONG = "Something went wrong. Please try again";
	public static final String INVALID_CREDENTIALS = "Invalid userId or password";

	// ===== TOKEN =====
	public static final String TOKEN_GENERATION_FAILED = "Unable to generate token";

	// ===== FORGOT PASSWORD =====
	public static final String USER_NOT_FOUND_EMAIL_PHONE = "User not found with given email or phone number";
	public static final String USER_NOT_FOUND_WITH_INPUT = "User not found with given email or phone number";

	public static final String FORGOT_OTP_SENT = "Forgot password OTP sent successfully";

	// ===== PASSWORD =====
	public static final String PASSWORD_RESET_SUCCESS = "Password reset successful";

}
