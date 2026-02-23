package com.sivvg.tradingservices.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sivvg.tradingservices.model.Role;
import com.sivvg.tradingservices.model.Status;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.repository.RoleRepository;
import com.sivvg.tradingservices.repository.StatusRepository;
import com.sivvg.tradingservices.repository.UserRepository;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("init") // make sure DataInitializer runs
@Component
//@Profile("!test")
public class DataInitializerTest {

	@Mock
	private RoleRepository roleRepo;

	@Mock
	private StatusRepository statusRepository;

	@Mock
	private UserRepository userRepo;

	@Mock
	private PasswordEncoder encoder;

	@InjectMocks
	private DataInitializer initializer;

	@Test
	public  void superAdminUser_shouldBeCreated_mocked() throws Exception {

        // user table empty
        when(userRepo.count()).thenReturn(0L);

        // ANY ROLE NAME -> return a Role
        when(roleRepo.findByName(anyString()))
                .thenReturn(Optional.of(new Role("ROLE_SUPER_ADMIN")));

        // ANY STATUS NAME -> return a Status
        when(statusRepository.findByName(anyString()))
                .thenReturn(Optional.of(new Status(1L, "ACTIVE")));

        when(userRepo.save(any(User.class)))
                .thenReturn(new User());

        when(encoder.encode(anyString()))
                .thenReturn("encoded-password");

        // RUN
        initializer.run();

        // VERIFY
        verify(userRepo, times(1)).save(any(User.class));
    }
}
