package com.sivvg.tradingservices.serviceImpl;

import static com.sivvg.tradingservices.util.MessageConstants.USER_NOT_FOUND;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.Profilepayload;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.service.Profileservice;

@Service
public class profileserviceimplementation implements Profileservice {

	private static final Logger logger = LoggerFactory.getLogger(profileserviceimplementation.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Profilepayload getProfileByUserId(String userId) {

		logger.info("Get profile service started | userId={}", userId);

		User user = userRepository.findByUserId(userId).orElseThrow(() -> {
			logger.error("User not found while fetching profile | userId={}", userId);
			return new RuntimeException(USER_NOT_FOUND);
		});

		Profilepayload response = new Profilepayload();
		response.setUserId(user.getUserId());
		response.setEmail(user.getEmail());
		response.setPhoneNumber(user.getPhoneNumber());

		// 🔒 Never expose password
		response.setPasswordHash("******");

		logger.info("Profile fetched successfully | userId={}", userId);

		return response;
	}
}
