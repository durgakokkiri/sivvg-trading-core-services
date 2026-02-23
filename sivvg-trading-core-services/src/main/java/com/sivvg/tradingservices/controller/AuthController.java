package com.sivvg.tradingservices.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.playload.ForgotRequest;
import com.sivvg.tradingservices.playload.LoginRequest;
import com.sivvg.tradingservices.playload.LoginResponse;
import com.sivvg.tradingservices.playload.OtpRequest;
import com.sivvg.tradingservices.playload.OtpResponse;
import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.playload.RegisterResponse;
import com.sivvg.tradingservices.playload.ResetPasswordRequest;
import com.sivvg.tradingservices.service.AuthService;
import com.sivvg.tradingservices.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	@PostMapping("/user-register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req, BindingResult bindingResult) {

		log.info("User registration started: {}", req.getEmail());

		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			log.warn("Validation failed during registration: {}", errors);
			return ResponseEntity.badRequest().body(errors);
		}

		RegisterResponse resp = userService.registerNewUser(req);

		log.info("User registered successfully: {}", req.getEmail());

		return ResponseEntity.ok(resp);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		// validate credentials and send OTP
		log.info("Login attempt for userId: {}", loginRequest.getUserId());
		LoginResponse loginResponse = authService.processLoginAndSendOtp(loginRequest);
		log.info("OTP sent successfully for userId: {}", loginRequest.getUserId());
		return ResponseEntity
				.ok(new LoginResponse("OTP sent to registered email & phone. Verify OTP to receive token."));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<OtpResponse> verifyOtp(@Valid @RequestBody OtpRequest otpRequest) {
		log.info("OTP verification started for userId: {}", otpRequest.getUserId());
		String token = authService.verifyOtpAndReturnToken(otpRequest.getUserId(), otpRequest.getOtp());
		log.info("OTP verified successfully for userId: {}", otpRequest.getUserId());
		return ResponseEntity.ok(new OtpResponse(token, "OTP verified. Use token for authenticated calls."));
	}


	@PostMapping("/forgot/send-otp")
	public ResponseEntity<String> forgotSendOtp(@RequestBody ForgotRequest forgotRequest) {
		log.info("Forgot password OTP request for: {}", forgotRequest.getEmailorphoneNumber());
		String userId = authService.sendForgotOtp(forgotRequest.getEmailorphoneNumber());
		log.info("Forgot OTP sent successfully for userId: {}", userId);
		return ResponseEntity.ok(userId);
	}

	@PostMapping("/resend-otp")
	public ResponseEntity<String> resendOtp(@RequestBody OtpRequest request) {
		log.info("Resend OTP requested for userId: {}", request.getUserId());
		authService.resendOtp(request.getUserId());
		log.info("OTP resent successfully for userId: {}", request.getUserId());
		return ResponseEntity.ok("OTP resent successfully");
	}

	@PostMapping("/forgot/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
		log.info("Password reset started for userId: {}", req.getUserId());
		if (!req.getNewPassword().equals(req.getConfirmPassword())) {
			log.warn("Password mismatch for userId: {}", req.getUserId());
			return ResponseEntity.badRequest().body("Passwords do not match");
		}
		authService.resetPassword(req.getUserId(), req.getNewPassword());
		log.info("Password reset successful for userId: {}", req.getUserId());

		return ResponseEntity.ok("Password reset successful");
	}

}
