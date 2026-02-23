package com.sivvg.tradingservices.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @BeforeEach
    void setUp() {
        doNothing().when(emailService)
                .sendSimpleMessage(anyString(), anyString(), anyString());

        doNothing().when(smsService)
                .sendSms(anyString(), anyString());
    }

    @Test
    public  void registerNewUser_success() {

        // ---------- REQUEST ----------
        RegisterRequest req = new RegisterRequest();
        req.setUsername("durga");
        req.setEmail("durga@gmail.com");
        req.setPhoneNumber("9876543210"); // IMPORTANT: without +91

        // ---------- ROLE ----------
        Role role = new Role();
        role.setName("ROLE_USER");

        // ---------- STATUS ----------
        Status status = new Status();
        status.setName("ACTIVE");

        // ---------- SAVED USER ----------
        User savedUser = new User();
        savedUser.setUserId("USR123");
        savedUser.setUsername("durga");
        savedUser.setEmail("durga@gmail.com");
        savedUser.setPhoneNumber("+919876543210");
        savedUser.setPasswordHash("hashedPassword");
        savedUser.setEnabled(true);
        savedUser.setRoles(new HashSet<>());
        savedUser.setStatus(new HashSet<>());
        savedUser.getRoles().add(role);
        savedUser.getStatus().add(status);

        // ---------- MOCKS ----------
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
       

        when(passwordEncoder.encode(anyString()))
                .thenReturn("hashedPassword");

        when(roleRepo.findByName("ROLE_USER"))
                .thenReturn(Optional.of(role));

        when(statusRepository.findByName("ACTIVE"))
                .thenReturn(Optional.of(status));

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // ---------- CALL ----------
        RegisterResponse response = userService.registerNewUser(req);

        // ---------- ASSERT ----------
        assertNotNull(response);
        assertNotNull(response.getUserId());
        assertEquals(
                "User registered successfully. Credentials sent to email & phone.",
                response.getMessage()
        );

        // ---------- VERIFY ----------
        verify(userRepository).save(any(User.class));
        verify(emailService).sendSimpleMessage(anyString(), anyString(), anyString());
        verify(smsService).sendSms(anyString(), anyString());
    }
}
