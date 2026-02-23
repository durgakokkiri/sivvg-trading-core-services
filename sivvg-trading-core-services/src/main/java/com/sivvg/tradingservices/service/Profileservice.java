package com.sivvg.tradingservices.service;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.playload.Profilepayload;

@Service
public interface Profileservice {

	public Profilepayload getProfileByUserId(String userId);

}