package com.sivvg.tradingservices.service;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class EmailServiceTest {
	 @Mock
	    private JavaMailSender mailSender;

	    @InjectMocks
	    private EmailService emailService;

	    @BeforeEach
	    public  void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public  void sendSimpleMessage_callsMailSender() {
	        // Arrange
	        String to = "test@example.com";
	        String subject = "Test Subject";
	        String text = "Hello World";

	        // Act
	        emailService.sendSimpleMessage(to, subject, text);

	        // Assert
	        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	    }

	    @Test
	    public  void sendSimpleMessage_messageContentCorrect() {
	        String to = "user@test.com";
	        String subject = "Welcome!";
	        String text = "Hello User";

	        emailService.sendSimpleMessage(to, subject, text);

	        verify(mailSender).send(argThat((SimpleMailMessage m) ->
	        m.getTo()[0].equals(to) &&
	        m.getSubject().equals(subject) &&
	        m.getText().equals(text) &&
	        m.getFrom().equals("baswaravalika2305@gmail.com")
	));
}}
