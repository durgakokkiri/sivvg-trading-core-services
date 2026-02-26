package com.sivvg.tradingservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.sivvg.tradingservices.configuration.FirebaseConfig;
import com.sivvg.tradingservices.configuration.TwilioConfig;
import com.sivvg.tradingservices.service.EmailService;
import com.sivvg.tradingservices.service.SmsService;
import com.sivvg.tradingservices.serviceImpl.YahooFinanceClientServiceImpl;

@SpringBootTest(classes = SivvgTradingCoreServicesApplication.class)
@ActiveProfiles("test")
public class SivvgTradingCoreServicesApplicationTests {

	@MockitoBean
	private TwilioConfig twilioConfig;

	@MockitoBean
	private EmailService emailService;

	@MockitoBean
	private SmsService smsService;

	@MockitoBean
	private YahooFinanceClientServiceImpl yahooFinanceClientService;
	
	@MockitoBean
	private FirebaseConfig firebaseConfig;

	@Test
	public void contextLoads() {

	}

}
