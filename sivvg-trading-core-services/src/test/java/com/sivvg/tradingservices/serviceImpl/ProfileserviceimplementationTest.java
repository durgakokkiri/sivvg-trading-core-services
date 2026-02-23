package com.sivvg.tradingservices.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.Profilepayload;
import com.sivvg.tradingservices.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class ProfileserviceimplementationTest {

	 @Mock
	    private UserRepository userRepository;

	    @InjectMocks
	    private profileserviceimplementation profileService;
	
	    @Test
	    public void getUserProfile_success() {

	        // given
	        User user = new User();
	        user.setUserId("U123");
	        user.setEmail("test@gmail.com");
	        user.setPhoneNumber("9876543210L");
	        		

	        when(userRepository.findByUserId("U123"))
	                .thenReturn(Optional.of(user));

	        // when
	        Profilepayload response = profileService.getProfileByUserId("U123");

	        // then
	        assertNotNull(response);
	        assertEquals("U123", response.getUserId());
	        assertEquals("test@gmail.com", response.getEmail());
	        assertEquals("9876543210L", response.getPhoneNumber());
	        assertEquals("******", response.getPasswordHash());

	        verify(userRepository, times(1)).findByUserId("U123");
	    }

	
}
