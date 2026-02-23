package com.sivvg.tradingservices.service;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.playload.RegisterResponse;

@Service
public interface UserService {

	public RegisterResponse registerNewUser(RegisterRequest req);

}
