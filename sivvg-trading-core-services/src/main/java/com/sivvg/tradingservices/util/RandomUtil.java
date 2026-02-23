package com.sivvg.tradingservices.util;

import java.security.SecureRandom;

public class RandomUtil {

	private static final SecureRandom RANDOM = new SecureRandom();

	private static final String NUMBERS = "0123456789";
	private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!?";

	private RandomUtil() {
		// Prevent object creation
	}

	// =================================================
	// USER ID GENERATION
	// Format: First 3 letters of username + 3 digits
	// Example: RAV123
	// =================================================
	public static String generateUserId(String username) {

		if (username == null) {
			throw new IllegalArgumentException("Username cannot be null");
		}

		username = username.trim();

		if (username.length() < 3) {
			throw new IllegalArgumentException("Username must have at least 3 letters to generate UserId");
		}

		String prefix = username.substring(0, 3).toUpperCase();
		StringBuilder userId = new StringBuilder(prefix);

		for (int i = 0; i < 3; i++) {
			userId.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
		}

		return userId.toString();
	}

	// =================================================
	// PASSWORD GENERATION
	// Minimum length: 6
	// =================================================
	public static String generatePassword(int length) {

		if (length < 6) {
			throw new IllegalArgumentException("Password length must be at least 6 characters");
		}

		StringBuilder password = new StringBuilder();

		for (int i = 0; i < length; i++) {
			password.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
		}

		return password.toString();
	}

	// =================================================
	// NUMERIC OTP GENERATION
	// =================================================
	public static String generateOtpNumeric(int length) {

		if (length <= 0) {
			throw new IllegalArgumentException("OTP length must be greater than zero");
		}

		StringBuilder otp = new StringBuilder();

		for (int i = 0; i < length; i++) {
			otp.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
		}

		return otp.toString();
	}
}
