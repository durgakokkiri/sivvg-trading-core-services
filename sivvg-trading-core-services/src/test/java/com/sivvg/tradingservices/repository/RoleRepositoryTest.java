package com.sivvg.tradingservices.repository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.Role;

@DataJpaTest
public class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@BeforeEach
	public void setUp() {

		if (!roleRepository.existsByName("ROLE_SUPER_ADMIN")) {
			roleRepository.save(new Role("ROLE_SUPER_ADMIN"));
		}

		// roleRepository.deleteAll();

		if (!roleRepository.existsByName("ROLE_USER")) {
			roleRepository.save(new Role("ROLE_USER"));
		}
		if (!roleRepository.existsByName("ROLE_ADMIN")) {
			roleRepository.save(new Role("ROLE_ADMIN"));
		}

	}

	// ✅ Positive test: find by name
	@Test
	public void shouldFindRoleByName_whenExists() {
		Optional<Role> role = roleRepository.findByName("ROLE_USER");
		assertThat(role).isPresent();
		assertThat(role.get().getName()).isEqualTo("ROLE_USER");
	}

	// ✅ Positive test: existsByName
	@Test
	public	void shouldReturnTrue_whenRoleExists() {
		assertThat(roleRepository.existsByName("ROLE_ADMIN")).isTrue();
	}

	@Test
	public void shouldFindSuperAdminRole() {
		Optional<Role> role = roleRepository.findByName("ROLE_SUPER_ADMIN");

		assertThat(role).isPresent();
		assertThat(role.get().getName()).isEqualTo("ROLE_SUPER_ADMIN");
	}

	// ✅ existsByName
	@Test
	public void shouldReturnTrue_whenSuperAdminExists() {
		boolean exists = roleRepository.existsByName("ROLE_SUPER_ADMIN");

		assertThat(exists).isTrue();
	}

	@org.junit.jupiter.api.AfterEach
	public	void cleanup() {
		roleRepository.deleteAll();
	}
}
