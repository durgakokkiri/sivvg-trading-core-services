package com.sivvg.tradingservices.service;

import static com.sivvg.tradingservices.util.MessageConstants.INVALID_OTP;
import static com.sivvg.tradingservices.util.MessageConstants.INVALID_PASSWORD;
import static com.sivvg.tradingservices.util.MessageConstants.OTP_EXPIRED;
import static com.sivvg.tradingservices.util.MessageConstants.OTP_NOT_GENERATED;
import static com.sivvg.tradingservices.util.MessageConstants.OTP_REQUIRED;
import static com.sivvg.tradingservices.util.MessageConstants.OTP_SENT;
import static com.sivvg.tradingservices.util.MessageConstants.USER_NOT_FOUND;
import static com.sivvg.tradingservices.util.MessageConstants.USER_NOT_FOUND_WITH_INPUT;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.exceptions.BadRequestException;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.LoginRequest;
import com.sivvg.tradingservices.playload.LoginResponse;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.serviceImpl.UserServiceImpl;
import com.sivvg.tradingservices.util.JwtUtil;
import com.sivvg.tradingservices.util.RandomUtil;

@Service
public class AuthService {

	@Value("${app.otp.expirationSeconds:300000}")
	private int otpExpirySeconds;

	@Value("${app.otp.length:6}")
	private int otpLength;

	private static final int MAX_RESEND_ATTEMPTS = 4;
	private static final int RESEND_BLOCK_MINUTES = 10;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// ================= LOGIN + OTP =================

	public LoginResponse processLoginAndSendOtp(LoginRequest loginRequest) {

		User user = userService.findByUserId(loginRequest.getUserId());
		if (user == null) {
			throw new BadRequestException(USER_NOT_FOUND);
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
			throw new BadRequestException(INVALID_PASSWORD);
		}

		String otp = RandomUtil.generateOtpNumeric(otpLength);

		userService.setOtpForUser(user, otp, otpExpirySeconds);

		String text = String.format("Your SIVVG OTP is: %s (valid %d minutes)", otp, otpExpirySeconds / 60);

		try {
			emailService.sendSimpleMessage(user.getEmail(), "SIVVG OTP", text);
		} catch (Exception ignored) {
		}

		try {
			smsService.sendSms(user.getPhoneNumber(), text);
		} catch (Exception ignored) {
		}

		return new LoginResponse(OTP_SENT);
	}

	// ================= VERIFY OTP =================

	public String verifyOtpAndReturnToken(String userId, String otp) {

		User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

		// 1️⃣ OTP NOT SENT FROM REQUEST
		if (otp == null || otp.trim().isEmpty()) {
			throw new BadRequestException(OTP_REQUIRED);
		}

		// 2️⃣ OTP NOT GENERATED / ALREADY VERIFIED
		if (user.getCurrentOtp() == null) {
			throw new BadRequestException(OTP_NOT_GENERATED);
		}

		// 3️⃣ WRONG OTP
		if (!user.getCurrentOtp().equals(otp)) {
			throw new BadRequestException(INVALID_OTP);
		}

		// 4️⃣ OTP EXPIRED
		if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
			throw new BadRequestException(OTP_EXPIRED);
		}

		// ✅ SUCCESS
		user.setCurrentOtp(null);
		user.setOtpExpiry(null);
		user.setOtpResendCount(0);
		user.setOtpResendBlockedUntil(null);

		userRepository.save(user);

		String role = user.getRoles().iterator().next().getName();
		return jwtUtil.generateToken(user.getUserId(), role);
	}

	// ================= FORGOT OTP =================

	public String sendForgotOtp(String emailOrphoneNumber) {

		User user = userRepository.findByEmailOrPhoneNumber(emailOrphoneNumber, emailOrphoneNumber);

		if (user == null) {
			throw new BadRequestException(USER_NOT_FOUND_WITH_INPUT + emailOrphoneNumber);
		}

		String otp = RandomUtil.generateOtpNumeric(otpLength);

		user.setCurrentOtp(otp);
		user.setOtpExpiry(LocalDateTime.now().plusSeconds(otpExpirySeconds));
		userRepository.save(user);

		String message = "Your SIVGG Forgot Password OTP is: " + otp + " (Valid for " + otpExpirySeconds + " seconds)";

		try {
			emailService.sendSimpleMessage(user.getEmail(), "SIVGG Forgot Password OTP", message);
		} catch (Exception ignored) {
		}

		try {
			smsService.sendSms(user.getPhoneNumber(), message);
		} catch (Exception ignored) {
		}

		return user.getUserId();
	}

	// ================= RESET PASSWORD =================

	public void resetPassword(String userId, String newPassword) {

		User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

		user.setPasswordHash(passwordEncoder.encode(newPassword));
		user.setCurrentOtp(null);
		user.setOtpExpiry(null);

		userRepository.save(user);
	}

	// ================= RESEND OTP =================

	public void resendOtp(String userId) {

		User user = userRepository.findByUserId(userId).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

		LocalDateTime now = LocalDateTime.now();

		if (user.getOtpResendBlockedUntil() != null && user.getOtpResendBlockedUntil().isAfter(now)) {

			throw new BadRequestException("OTP resend blocked. Try again after " + user.getOtpResendBlockedUntil());
		}

		if (user.getOtpResendCount() >= MAX_RESEND_ATTEMPTS) {

			user.setOtpResendBlockedUntil(now.plusMinutes(RESEND_BLOCK_MINUTES));
			user.setOtpResendCount(0);
			userRepository.save(user);

			throw new BadRequestException(
					"Maximum OTP resend attempts reached. Please wait " + RESEND_BLOCK_MINUTES + " minutes.");
		}

		String newOtp = RandomUtil.generateOtpNumeric(otpLength);

		user.setCurrentOtp(newOtp);
		user.setOtpExpiry(now.plusSeconds(otpExpirySeconds));
		user.setOtpResendCount(user.getOtpResendCount() + 1);

		userRepository.save(user);

		String message = String.format("Your SIVVG OTP is: %s (valid %d minutes)", newOtp, otpExpirySeconds / 60);

		try {
			emailService.sendSimpleMessage(user.getEmail(), "SIVVG OTP", message);
		} catch (Exception ignored) {
		}

		try {
			smsService.sendSms(user.getPhoneNumber(), message);
		} catch (Exception ignored) {
		}
	}
}
