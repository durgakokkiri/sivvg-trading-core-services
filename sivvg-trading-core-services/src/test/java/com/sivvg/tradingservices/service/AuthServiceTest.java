package com.sivvg.tradingservices.service;

import static com.sivvg.tradingservices.util.MessageConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.sivvg.tradingservices.exceptions.BadRequestException;
import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.LoginRequest;
import com.sivvg.tradingservices.playload.LoginResponse;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.serviceImpl.UserServiceImpl;
import com.sivvg.tradingservices.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private EmailService emailService;

    @Mock
    private SmsService smsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    public void setup() {

        // Inject @Value fields
        ReflectionTestUtils.setField(authService, "otpExpirySeconds", 300);
        ReflectionTestUtils.setField(authService, "otpLength", 6);

        user = new User();
        user.setUserId("USR123");
        user.setPasswordHash("encodedPass");
        user.setEmail("test@gmail.com");
        user.setPhoneNumber("9876543210");

        Role role = new Role();
        role.setName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
    }

    // ================= LOGIN + OTP =================

    @Test
    public void processLoginAndSendOtp_success() {

        LoginRequest request = new LoginRequest("USR123", "plain");

        when(userService.findByUserId("USR123")).thenReturn(user);
        when(passwordEncoder.matches("plain", "encodedPass")).thenReturn(true);

        LoginResponse response = authService.processLoginAndSendOtp(request);

        verify(userService).setOtpForUser(any(), anyString(), anyInt());
        verify(emailService).sendSimpleMessage(anyString(), anyString(), anyString());
        verify(smsService).sendSms(anyString(), anyString());

        assertThat(response.getMessage()).isEqualTo(OTP_SENT);
    }

    @Test
    public void processLoginAndSendOtp_invalidPassword() {

        LoginRequest request = new LoginRequest("USR123", "wrong");

        when(userService.findByUserId("USR123")).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> authService.processLoginAndSendOtp(request));
    }

    // ================= VERIFY OTP =================

    @Test
    public void verifyOtpAndReturnToken_success() {

        user.setCurrentOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        when(userRepository.findByUserId("USR123")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("USR123", "ROLE_USER")).thenReturn("mock-token");

        String token = authService.verifyOtpAndReturnToken("USR123", "123456");

        assertThat(token).isEqualTo("mock-token");
        verify(userRepository).save(user);

        assertNull(user.getCurrentOtp());
        assertNull(user.getOtpExpiry());
    }

    @Test
    public void verifyOtpAndReturnToken_invalidOtp() {

        user.setCurrentOtp("111111");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        when(userRepository.findByUserId("USR123")).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class,
                () -> authService.verifyOtpAndReturnToken("USR123", "222222"));
    }

    @Test
    public void verifyOtpAndReturnToken_expiredOtp() {

        user.setCurrentOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByUserId("USR123")).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class,
                () -> authService.verifyOtpAndReturnToken("USR123", "123456"));
    }

    // ================= FORGOT OTP =================

    @Test
    public void sendForgotOtp_success() {

        when(userRepository.findByEmailOrPhoneNumber(anyString(), anyString()))
                .thenReturn(user);

        String userId = authService.sendForgotOtp("test@gmail.com");

        verify(userRepository).save(user);
        verify(emailService).sendSimpleMessage(anyString(), anyString(), anyString());
        verify(smsService).sendSms(anyString(), anyString());

        assertEquals("USR123", userId);
    }

    @Test
    public void sendForgotOtp_userNotFound() {

        when(userRepository.findByEmailOrPhoneNumber(anyString(), anyString()))
                .thenReturn(null);

        assertThrows(BadRequestException.class,
                () -> authService.sendForgotOtp("wrong@gmail.com"));
    }

    // ================= RESET PASSWORD =================

    @Test
    public void resetPassword_success() {

        when(userRepository.findByUserId("USR123")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("ENC");

        authService.resetPassword("USR123", "newpass");

        assertEquals("ENC", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    // ================= RESEND OTP =================

    @Test
    public void resendOtp_success() {

        user.setOtpResendCount(0);

        when(userRepository.findByUserId("USR123"))
                .thenReturn(Optional.of(user));

        authService.resendOtp("USR123");

        verify(userRepository).save(user);
        verify(emailService).sendSimpleMessage(anyString(), anyString(), anyString());
        verify(smsService).sendSms(anyString(), anyString());

        assertEquals(1, user.getOtpResendCount());
        assertNotNull(user.getCurrentOtp());
        assertNotNull(user.getOtpExpiry());
    }

    @Test
    public void resendOtp_blocked() {

        user.setOtpResendBlockedUntil(LocalDateTime.now().plusMinutes(5));

        when(userRepository.findByUserId("USR123"))
                .thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class,
                () -> authService.resendOtp("USR123"));
    }
}
