package com.sivvg.tradingservices.serviceImpl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sivvg.tradingservices.exceptions.BadRequestException;
import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.Status;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.AdminCreateRequest;
import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.repository.RoleRepository;
import com.sivvg.tradingservices.repository.StatusRepository;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.service.EmailService;
import com.sivvg.tradingservices.service.SmsService;


@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

	  @InjectMocks
	    private AdminServiceImpl adminService;

	    @Mock
	    private UserRepository userRepository;

	    @Mock
	    private RoleRepository roleRepository;

	    @Mock
	    private StatusRepository statusRepository;

	    @Mock
	    private PasswordEncoder passwordEncoder;

	    @Mock
	    private EmailService emailService;

	    @Mock
	    private SmsService smsService;

	    @Mock
	    private ModelMapper modelMapper;

	    private User user;
	    private Role adminRole;

	    @BeforeEach
	    public   void setup() {
	        user = new User();
	        user.setRoles(new HashSet<>());
	        user.setStatus(new HashSet<>());

	        adminRole = new Role();
	        adminRole.setName("ROLE_ADMIN");
	    }

	    // ================= CREATE ADMIN =================

	    @Test
	    public  void createAdmin_success() {

	        AdminCreateRequest req = new AdminCreateRequest();
	        req.setUsername("admin");
	        req.setEmail("admin@gmail.com");
	        req.setPhone("9876543210");

	        when(userRepository.existsByEmail(any())).thenReturn(false);
	        when(userRepository.existsByPhoneNumber(any())).thenReturn(false);
	        when(userRepository.existsByUsername(any())).thenReturn(false);

	        when(passwordEncoder.encode(any())).thenReturn("ENC_PASS");
	        when(roleRepository.findByName("ROLE_ADMIN"))
	                .thenReturn(Optional.of(adminRole));

	        when(modelMapper.map(req, User.class)).thenReturn(user);
	        when(userRepository.save(any(User.class))).thenReturn(user);
	        when(modelMapper.map(user, AdminCreateRequest.class))
	                .thenReturn(req);

	        AdminCreateRequest response = adminService.createAdmin(req);

	        assertNotNull(response);
	        verify(emailService).sendSimpleMessage(any(), any(), any());
	        verify(smsService).sendSms(any(), any());
	    }

	    @Test
	    public  void createAdmin_emailAlreadyExists() {

	        AdminCreateRequest req = new AdminCreateRequest();
	        req.setEmail("admin@gmail.com");

	        when(userRepository.existsByEmail(any()))
	                .thenReturn(true);

	        assertThrows(BadRequestException.class,
	                () -> adminService.createAdmin(req));
	    }

	    // ================= GET ALL USERS =================

	    @Test
	    public   void getAllUsers_success() {

	        User u = new User();
	        Role role = new Role();
	        role.setName("ROLE_USER");
	        Status status = new Status();
	        status.setName("ACTIVE");

	        u.setRoles(Set.of(role));
	        u.setStatus(Set.of(status));

	        when(userRepository.findAll()).thenReturn(List.of(u));
	        when(modelMapper.map(any(User.class), eq(RegisterRequest.class)))
	                .thenReturn(new RegisterRequest());

	        List<RegisterRequest> result = adminService.getAllUsers();

	        assertEquals(1, result.size());
	        assertEquals("ROLE_USER", result.get(0).getRole());
	        assertEquals("ACTIVE", result.get(0).getStatus());
	    }

	    // ================= DELETE USER =================

	    @Test
	    public  void deleteUser_success() {

	        User u = new User();
	        u.setRoles(new HashSet<>());
	        u.setStatus(new HashSet<>());

	        when(userRepository.findById(1L))
	                .thenReturn(Optional.of(u));

	        adminService.deleteUser(1L);

	        verify(userRepository).delete(u);
	    }

	    // ================= UPDATE USER =================

	    @Test
	    public  void updateUser_success() {

	        RegisterRequest req = new RegisterRequest();
	        req.setRole("ROLE_ADMIN");
	        req.setStatus("ACTIVE");

	        User u = new User();
	        u.setRoles(new HashSet<>());
	        u.setStatus(new HashSet<>());

	        Role role = new Role();
	        role.setName("ROLE_ADMIN");

	        Status status = new Status();
	        status.setName("ACTIVE");

	        when(userRepository.findById(1L))
	                .thenReturn(Optional.of(u));
	        when(roleRepository.findByName("ROLE_ADMIN"))
	                .thenReturn(Optional.of(role));
	        when(statusRepository.findByName("ACTIVE"))
	                .thenReturn(Optional.of(status));
	        when(userRepository.save(any(User.class)))
	                .thenReturn(u);

	        RegisterRequest result = adminService.updateUser(1L, req);

	        assertEquals("ROLE_ADMIN", result.getRole());
	        assertEquals("ACTIVE", result.getStatus());
	    }

	    // ================= DASHBOARD COUNTS =================

	    @Test
	    public  void getDashboardCounts_success() {

	        when(userRepository.countTotalUsers()).thenReturn(10L);
	        when(userRepository.countByStatusName("ACTIVE")).thenReturn(6L);
	        when(userRepository.countByStatusName("DISABLED")).thenReturn(2L);
	        when(userRepository.countByStatusName("BLOCKED")).thenReturn(2L);

	        Map<String, Long> map = adminService.getDashboardCounts();

	        assertEquals(10L, map.get("totalUsers"));
	        assertEquals(6L, map.get("activeUsers"));
	    }
	
	
	
}
