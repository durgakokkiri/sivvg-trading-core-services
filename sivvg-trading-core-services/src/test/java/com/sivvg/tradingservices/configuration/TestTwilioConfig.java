package com.sivvg.tradingservices.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.sivvg.tradingservices.configuration.TwilioConfig;
import com.sivvg.tradingservices.service.SmsService;

@TestConfiguration
@ConditionalOnProperty(name = "twilio.accountSid")
public class TestTwilioConfig {
	
	 @Bean
	    public TwilioConfig twilioConfig() {
	        return new TwilioConfig(); // dummy/fake config
	    }
	 
	 
	 @Bean
	    public SmsService smsService() {
	        return new SmsService() {
	            @Override
	            public void sendSms(String to, String message) {
	                System.out.println("Twilio not configured. SMS to " + to + ": " + message);
	            }
	        };
	    }
	 
}
