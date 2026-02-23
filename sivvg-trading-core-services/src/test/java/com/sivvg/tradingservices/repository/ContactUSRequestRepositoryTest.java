package com.sivvg.tradingservices.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sivvg.tradingservices.model.ContactUSRequest;
import com.sivvg.tradingservices.model.User;

@DataJpaTest
public class ContactUSRequestRepositoryTest {

    @Autowired
    private ContactUSRequestRepository contactUSRequestRepository;

    @Autowired
    private UserRepository userRepository;

    // =====================================================
    // Helper : Create VALID User
    // =====================================================
    private User createValidUser(String email, String username, String phone) {

        User user = new User();
        user.setUserId("USR-" + UUID.randomUUID());
        user.setUsername(username);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPasswordHash("hashed-password");
        user.setEnabled(true);
        user.setOtpResendCount(0);
        user.setIsFollowingTips(false);

        return userRepository.save(user);
    }

    // =====================================================
    // Helper : Create VALID ContactUSRequest
    // =====================================================
    private ContactUSRequest createValidContactRequest(User user, String message) {

        ContactUSRequest req = new ContactUSRequest();
        req.setUser(user);                 // REQUIRED
        req.setMessage(message);           // REQUIRED
        req.setType("EMAIL");              // REQUIRED
        req.setStatus("NEW");              // REQUIRED
        req.setAutoReplySent(false);        // REQUIRED
        req.setAdminReply(null);            // OPTIONAL

        return req;
    }

    // =====================================================
    // TEST 1 : Requests exist for user
    // =====================================================
    @Test
    public void findByUser_shouldReturnUserRequests() {

        User user = createValidUser(
                "testuser@gmail.com",
                "testuser",
                "9999999999"
        );

        contactUSRequestRepository.save(
                createValidContactRequest(user, "Need help")
        );
        contactUSRequestRepository.save(
                createValidContactRequest(user, "Support required")
        );

        List<ContactUSRequest> result =
                contactUSRequestRepository.findByUser(user);

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(ContactUSRequest::getMessage)
                .containsExactlyInAnyOrder(
                        "Need help",
                        "Support required"
                );
    }

    // =====================================================
    // TEST 2 : No requests for user
    // =====================================================
    @Test
    public void findByUser_shouldReturnEmptyList_whenNoRequests() {

        User user = createValidUser(
                "nouser@gmail.com",
                "nouser",
                "8888888888"
        );

        List<ContactUSRequest> result =
                contactUSRequestRepository.findByUser(user);

        assertThat(result).isEmpty();
    }
}
