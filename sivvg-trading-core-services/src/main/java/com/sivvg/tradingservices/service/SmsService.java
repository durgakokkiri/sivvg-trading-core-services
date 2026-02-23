package com.sivvg.tradingservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.configuration.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

	@Value("${twilio.account.sid}")
	private String accountSid;
	@Value("${twilio.auth.token}")
	private String authToken;
	@Value("${twilio.phone.number}")
	private String fromPhone;

	@Autowired
	private TwilioConfig config;

	@PostConstruct
	public void init() {
		if (accountSid != null && !accountSid.isEmpty()) {
			Twilio.init(accountSid, authToken);
		}
	}

	public void sendSms(String toPhone, String message) {
		// ensure phone includes +countrycode
		if (accountSid == null || accountSid.isEmpty()) {
			// Twilio not configured; log or skip in dev
			System.out.println("Twilio not configured. SMS to " + toPhone + ": " + message);
			return;
		}
		Message.creator(new com.twilio.type.PhoneNumber(toPhone), new com.twilio.type.PhoneNumber(fromPhone), message)
				.create();
	}

}
