package com.sivvg.tradingservices.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.exceptions.BadRequestException;
import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.Status;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.AdminCreateRequest;
import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.repository.RoleRepository;
import com.sivvg.tradingservices.repository.StatusRepository;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.service.AdminService;
import com.sivvg.tradingservices.service.EmailService;
import com.sivvg.tradingservices.service.SmsService;
import com.sivvg.tradingservices.util.RandomUtil;

@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	// ✅ Strict email regex (same as DB constraint)
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private ModelMapper modelMapper;

	// 🔹 CREATE ADMIN
	@Override
	public AdminCreateRequest createAdmin(AdminCreateRequest request) {

		logger.info("Create Admin started | username={}, email={}", request.getUsername(), request.getEmail());

		// ---------- VALIDATIONS (FINAL SAFETY) ----------
		if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
			logger.warn("Invalid email format received: {}", request.getEmail());
			throw new BadRequestException("Invalid email format");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadRequestException("Email already registered");
		}

		if (userRepository.existsByPhoneNumber("+91" + request.getPhone())) {
			throw new BadRequestException("Phone already registered");
		}

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new BadRequestException("Username already taken");
		}

		// ---------- GENERATE CREDENTIALS ----------
		String userId = RandomUtil.generateUserId(request.getUsername());
		String rawPassword = RandomUtil.generatePassword(10);
		String encodedPassword = passwordEncoder.encode(rawPassword);

		// ---------- PREPARE USER ENTITY ----------
		User admin = modelMapper.map(request, User.class);
		admin.setUserId(userId);
		admin.setPasswordHash(encodedPassword);
		admin.setPhoneNumber("+91" + request.getPhone());
		admin.setEnabled(true);

		Role adminRole = roleRepository.findByName("ROLE_ADMIN")
				.orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

		admin.getRoles().add(adminRole);

		User savedAdmin = userRepository.save(admin);

		logger.info("Admin created successfully | userId={}", savedAdmin.getUserId());

		// ---------- SEND CREDENTIALS ----------
		String message = """
				Welcome %s,

				Your admin account has been created.

				UserId : %s
				Password : %s

				Please change your password after first login.

				- SIVVG Team
				""".formatted(request.getUsername(), userId, rawPassword);

		try {
			emailService.sendSimpleMessage(savedAdmin.getEmail(), "SIVVG Admin Login Credentials", message);
		} catch (Exception e) {
			logger.error("Email sending failed", e);
		}

		try {
			smsService.sendSms(savedAdmin.getPhoneNumber(), message);
		} catch (Exception e) {
			logger.error("SMS sending failed", e);
		}

		return modelMapper.map(savedAdmin, AdminCreateRequest.class);
	}

	// 🔹 GET ALL USERS
	@Override
	public List<RegisterRequest> getAllUsers() {

		logger.info("Fetching all users");

		return userRepository.findAll().stream().map(user -> {
			RegisterRequest dto = modelMapper.map(user, RegisterRequest.class);

			dto.setRole(user.getRoles().stream().map(Role::getName).findFirst().orElse("NA"));

			dto.setStatus(getUserDisplayStatus(user));
			return dto;
		}).collect(Collectors.toList());
	}

	private String getUserDisplayStatus(User user) {

		Set<String> statuses = user.getStatus().stream().map(Status::getName).collect(Collectors.toSet());

		if (statuses.contains("BLOCKED"))
			return "BLOCKED";
		if (statuses.contains("DISABLED"))
			return "DISABLED";
		if (statuses.contains("ACTIVE"))
			return "ACTIVE";

		return "UNKNOWN";
	}

	// 🔹 DELETE USER
	@Override
	public void deleteUser(Long id) {

		logger.warn("Deleting user | id={}", id);

		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		user.getRoles().clear();
		user.getStatus().clear();

		userRepository.delete(user);
	}

	// 🔹 UPDATE USER
	@Override
	public RegisterRequest updateUser(Long id, RegisterRequest req) {

		logger.info("Updating user | id={}", id);

		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		modelMapper.map(req, user);

		if (req.getRole() != null) {
			Role role = roleRepository.findByName(req.getRole())
					.orElseThrow(() -> new RuntimeException("Role not found"));

			user.getRoles().clear();
			user.getRoles().add(role);
		}

		if (req.getStatus() != null) {
			Status status = statusRepository.findByName(req.getStatus())
					.orElseThrow(() -> new RuntimeException("Status not found"));

			user.getStatus().clear();
			user.getStatus().add(status);
		}

		User updated = userRepository.save(user);

		req.setRole(updated.getRoles().stream().map(Role::getName).findFirst().orElse("NA"));

		req.setStatus(updated.getStatus().stream().map(Status::getName).findFirst().orElse("NA"));

		return req;
	}

	// 🔹 DASHBOARD COUNTS
	@Override
	public Map<String, Long> getDashboardCounts() {

		Map<String, Long> data = new HashMap<>();
		data.put("totalUsers", userRepository.countTotalUsers());
		data.put("activeUsers", userRepository.countByStatusName("ACTIVE"));
		data.put("disabledUsers", userRepository.countByStatusName("DISABLED"));
		data.put("blockedUsers", userRepository.countByStatusName("BLOCKED"));

		return data;
	}
}
