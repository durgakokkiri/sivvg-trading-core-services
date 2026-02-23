package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.Followers;
import com.sivvg.tradingservices.model.User;

@DataJpaTest
public class FollowerRepositoryTest {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    // =====================================================
    // Helper : Create VALID User
    // =====================================================
    private User createValidUser() {

        User user = new User();
        user.setUserId("USR-" + UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("testuser@gmail.com");
        user.setPhoneNumber("9999999999");
        user.setPasswordHash("hashed-password");
        user.setEnabled(true);
        user.setOtpResendCount(0);
        user.setIsFollowingTips(false);

        return userRepository.save(user);
    }

    // =====================================================
    // Helper : Create VALID Followers
    // =====================================================
    private Followers createValidFollower(User user) {

        Followers follower = new Followers();
        follower.setUser(user);     // 🔴 REQUIRED (FK)
        follower.setActive(true);

        return follower;
    }

    // =====================================================
    // TEST 1 : Save follower
    // =====================================================
    @Test
    public void save_shouldPersistFollower() {

        User user = createValidUser();

        Followers saved =
                followerRepository.save(createValidFollower(user));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUser()).isNotNull();
        assertThat(saved.isActive()).isTrue();
    }

    // =====================================================
    // TEST 2 : findAll
    // =====================================================
    @Test
    public void findAll_shouldReturnFollowers() {

        User user = createValidUser();

        followerRepository.save(createValidFollower(user));

        assertThat(followerRepository.findAll()).hasSize(1);
    }
}
