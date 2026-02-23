package com.sivvg.tradingservices.serviceImpl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.exceptions.BadRequestException;
import com.sivvg.tradingservices.exceptions.ResourceNotFoundException;
import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.Status;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.playload.RegisterResponse;
import com.sivvg.tradingservices.repository.RoleRepository;
import com.sivvg.tradingservices.repository.StatusRepository;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.service.EmailService;
import com.sivvg.tradingservices.service.SmsService;
import com.sivvg.tradingservices.service.UserService;
import com.sivvg.tradingservices.util.MessageConstants;
import com.sivvg.tradingservices.util.RandomUtil;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private StatusRepository statusRepository;

	// 🔹 USER REGISTRATION
	@Override
	public RegisterResponse registerNewUser(RegisterRequest req) {

		log.info("User registration started | username={}", req.getUsername());

		if (userRepository.existsByEmail(req.getEmail()) || userRepository.existsByPhoneNumber(req.getPhoneNumber())) {

			log.warn("User already exists | username={}", req.getUsername());
			throw new BadRequestException(MessageConstants.USER_ALREADY_EXISTS);
		}

		// 🔹 Generate system values
		String userId = RandomUtil.generateUserId(req.getUsername());
		String rawPassword = RandomUtil.generatePassword(6);
		String hashedPassword = passwordEncoder.encode(rawPassword);

		// 🔹 Create User entity
		User user = new User();
		user.setUserId(userId);
		user.setUsername(req.getUsername());
		user.setEmail(req.getEmail());
		user.setPhoneNumber("+91" + req.getPhoneNumber());
		user.setPasswordHash(hashedPassword);
		user.setGender(req.getGender());
		user.setDob(req.getDob());
		user.setEnabled(true);

		// 🔹 Role
		Role role = roleRepo.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
		user.getRoles().add(role);

		// 🔹 Status
		Status status = statusRepository.findByName("ACTIVE")
				.orElseThrow(() -> new RuntimeException("Status not found"));
		user.getStatus().add(status);

		User savedUser = userRepository.save(user);

		log.info("User saved successfully | userId={}", savedUser.getUserId());

		// 🔹 Send credentials
		String message = String.format("Welcome %s\nUserId: %s\nPassword: %s", savedUser.getUsername(), userId,
				rawPassword);

		try {
			emailService.sendSimpleMessage(savedUser.getEmail(), "Welcome to SIVVG", message);
			log.info("Registration email sent | userId={}", userId);
		} catch (Exception e) {
			log.error("Failed to send registration email | userId={}", userId, e);
		}

		try {
			smsService.sendSms(savedUser.getPhoneNumber(), message);
			log.info("Registration SMS sent | userId={}", userId);
		} catch (Exception e) {
			log.error("Failed to send registration SMS | userId={}", userId, e);
		}

		return new RegisterResponse(userId, "User registered successfully. Credentials sent to email & phone.");
	}

	// 🔹 FIND USER
	public User findByUserId(String userId) {

		log.debug("Finding user by userId={}", userId);

		return userRepository.findByUserId(userId).orElseThrow(() -> {
			log.error("User not found | userId={}", userId);
			return new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND);
		});
	}

	// 🔹 OTP SET
	public void setOtpForUser(User user, String otp, int expirySeconds) {

		if (user == null) {
			log.error("Attempt to set OTP for null user");
			throw new BadRequestException(MessageConstants.USER_NOT_FOUND);
		}

		user.setCurrentOtp(otp);
		user.setOtpExpiry(LocalDateTime.now().plusSeconds(expirySeconds));
		userRepository.save(user);

		log.info("OTP generated and saved | userId={}", user.getUserId());
	}

	// 🔹 OTP VERIFY
	public boolean verifyOtp(User user, String otp) {

		if (user == null) {
			log.error("OTP verification failed – user not found");
			throw new BadRequestException(MessageConstants.USER_NOT_FOUND);
		}

		if (user.getCurrentOtp() == null || user.getOtpExpiry() == null) {
			log.warn("OTP missing | userId={}", user.getUserId());
			throw new BadRequestException(MessageConstants.OTP_REQUIRED);
		}

		if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
			log.warn("OTP expired | userId={}", user.getUserId());
			throw new BadRequestException(MessageConstants.OTP_EXPIRED);
		}

		if (!user.getCurrentOtp().equals(otp)) {
			log.warn("Invalid OTP entered | userId={}", user.getUserId());
			throw new BadRequestException(MessageConstants.INVALID_OTP);
		}

		user.setCurrentOtp(null);
		user.setOtpExpiry(null);
		userRepository.save(user);

		log.info("OTP verified successfully | userId={}", user.getUserId());
		return true;
	}

	// 🔹 LOGIN VALIDATION
	public boolean validateCredentials(String userId, String rawPassword) {

		log.debug("Validating credentials | userId={}", userId);

		if (userId == null || userId.isEmpty()) {
			throw new BadRequestException(MessageConstants.USERID_REQUIRED);
		}

		if (rawPassword == null || rawPassword.isEmpty()) {
			throw new BadRequestException(MessageConstants.PASSWORD_REQUIRED);
		}

		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));

		boolean match = passwordEncoder.matches(rawPassword, user.getPasswordHash());

		log.info("Credential validation result | userId={}, success={}", userId, match);

		return match;
	}
}
