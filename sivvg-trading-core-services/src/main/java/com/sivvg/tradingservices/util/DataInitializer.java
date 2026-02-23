package com.sivvg.tradingservices.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.Status;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.repository.RoleRepository;
import com.sivvg.tradingservices.repository.StatusRepository;
import com.sivvg.tradingservices.repository.UserRepository;

@Component
//@Profile("init")
public class DataInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) {

		log.info("Data initialization started (profile=init)");

		// ROLES
		createRoleIfNotExists("ROLE_USER");
		createRoleIfNotExists("ROLE_ADMIN");
		createRoleIfNotExists("ROLE_SUPER_ADMIN");

		// STATUSES
		createStatusIfNotExists("ACTIVE");
		createStatusIfNotExists("DISABLED");
		createStatusIfNotExists("BLOCKED");

		// SUPER ADMIN
		try {
			if (userRepo.count() == 0) {

				Role role = roleRepo.findByName("ROLE_SUPER_ADMIN")
						.orElseThrow(() -> new RuntimeException("ROLE_SUPER_ADMIN missing"));

				User user = new User();
				user.setUsername("superadmin");
				user.setUserId("SUPER001");
				user.setPasswordHash(encoder.encode("super123"));
				user.setEmail("durgakokkiri01@gmail.com");
				user.setPhoneNumber("+918639388764");
				user.setDob("1998-12-10");
				user.setGender("FEMALE");
				user.setEnabled(true);
				user.getRoles().add(role);

				userRepo.save(user);

				log.info("SUPER_ADMIN user created successfully");
			} else {
				log.info("Users already exist, skipping SUPER_ADMIN creation");
			}

		} catch (Exception e) {
			// ❗ NEVER stop application
			log.error("SuperAdmin initialization skipped due to error", e);
		}

		log.info("Data initialization completed");
	}

	// HELPER METHODS
	private void createRoleIfNotExists(String roleName) {

		roleRepo.findByName(roleName).orElseGet(() -> {
			log.info("Creating role: {}", roleName);
			return roleRepo.save(new Role(roleName));
		});
	}

	private void createStatusIfNotExists(String statusName) {

		statusRepository.findByName(statusName).orElseGet(() -> {
			log.info("Creating status: {}", statusName);
			return statusRepository.save(new Status(null, statusName));
		});
	}
}
