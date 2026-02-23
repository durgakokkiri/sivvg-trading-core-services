package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.Status;
import com.sivvg.tradingservices.model.User;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ContactUSRequestRepository contactRequestRepository;

    private Role roleUser;
    private Status statusActive;
    private User testUser;

    @BeforeEach
    public void setup() {

        // ✅ DELETE CHILD TABLE FIRST (VERY IMPORTANT)
        contactRequestRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        statusRepository.deleteAll();

        // ✅ SAFE ROLE INSERT
        roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        // ✅ SAFE STATUS INSERT
        statusActive = statusRepository.findByName("ACTIVE")
        	    .orElseGet(() -> {
        	        Status s = new Status();
        	        s.setName("ACTIVE");
        	        return statusRepository.save(s);
        	    });


        // ✅ CREATE TEST USER
        testUser = new User();
        testUser.setUserId("USR001");
        testUser.setUsername("durga123");
        testUser.setEmail("durgak@example.com");
        testUser.setPhoneNumber("+919876543210"); // ✅ correct format
        testUser.setPasswordHash("hashedpassword123");

        HashSet<Role> roles = new HashSet<>();
        roles.add(roleUser);
        testUser.setRoles(roles);

        HashSet<Status> statusSet = new HashSet<>();
        statusSet.add(statusActive);
        testUser.setStatus(statusSet);

        testUser.setIsFollowingTips(true);
        testUser.setSubscriptionPlan("PREMIUM");
        testUser.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.saveAndFlush(testUser);
    }

    @Test
    public void testFindByUserId() {
        Optional<User> found = userRepository.findByUserId("USR001");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("durga123");
    }

    @Test
    public void testExistsByEmail() {
        assertThat(userRepository.existsByEmail("durgak@example.com")).isTrue();
    }

    @Test
    public void testExistsByEmailFalse() {
        assertThat(userRepository.existsByEmail("no@email.com")).isFalse();
    }

    @Test
    public void testExistsByPhoneNumber() {
        assertThat(userRepository.existsByPhoneNumber("+919876543210")).isTrue();
    }

    @Test
    public void testExistsByPhoneNumberFalse() {
        assertThat(userRepository.existsByPhoneNumber("+917777777777")).isFalse();
    }

    @Test
    public void testExistsByUsername() {
        assertThat(userRepository.existsByUsername("durga123")).isTrue();
    }

    @Test
    public void testExistsByUsernameFalse() {
        assertThat(userRepository.existsByUsername("ravi")).isFalse();
    }

    @Test
    public void testFindByEmailOrPhoneNumber_email() {
        User user = userRepository.findByEmailOrPhoneNumber(
                "durgak@example.com",
                "durgak@example.com"
        );
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("USR001");
    }

    @Test
    public void testFindByEmailOrPhoneNumber_phone() {
        User user = userRepository.findByEmailOrPhoneNumber(
                "+919876543210",
                "+919876543210"
        );
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("USR001");
    }

    @Test
    public void testCountTotalUsers() {
        assertThat(userRepository.countTotalUsers()).isEqualTo(1L);
    }

    @Test
    public void testCountByStatusName() {
        assertThat(userRepository.countByStatusName("ACTIVE")).isEqualTo(1L);
    }
}

