package com.sivvg.tradingservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.sivvg.tradingservices.configuration.TwilioConfig;
import com.sivvg.tradingservices.service.EmailService;
import com.sivvg.tradingservices.service.SmsService;
import com.sivvg.tradingservices.serviceImpl.YahooFinanceClientServiceImpl;

@SpringBootTest(classes = SivvgTradingCoreServicesApplication.class)
@ActiveProfiles("test")
public class SivvgTradingCoreServicesApplicationTests {

	@MockBean
	private TwilioConfig twilioConfig;

	@MockBean
	private EmailService emailService;

	@MockBean
	private SmsService smsService;

	@MockBean
	private YahooFinanceClientServiceImpl yahooFinanceClientService;

	@Test
	public void contextLoads() {

	}

}
