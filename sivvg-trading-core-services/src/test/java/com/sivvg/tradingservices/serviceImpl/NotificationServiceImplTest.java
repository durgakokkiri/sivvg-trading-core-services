package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

import com.sivvg.tradingservices.model.NotificationEntity;
import com.sivvg.tradingservices.repository.NotificationRepository;
@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

	    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	   
	    private final PrintStream originalOut = System.out;
	   
	    @InjectMocks
	    private NotificationServiceImpl service;
	    @Mock
	    private NotificationRepository repo;

//	    @BeforeEach
//	    public void setup() {
//	        service = new NotificationServiceImpl();
//	        System.setOut(new PrintStream(outContent)); // capture console output
//	    }
	    @BeforeEach
	    public void setup() {
	        System.setOut(new PrintStream(outContent));
	    }

	    @AfterEach
	    public  void restore() {
	        System.setOut(originalOut); // restore console
	    }

	   
	    // -------------------------------------------------
	    // TEST 2: General Notification
	    // -------------------------------------------------
	    @Test
	    public void sendGeneralNotification_shouldPrintCorrectMessage() {

	        assertDoesNotThrow(() ->
	                service.sendGeneralNotification(
	                        "NO_TIPS",
	                        "Today no tips provided"
	                )
	        );

	        String output = outContent.toString();

	        assertThat(output).contains("NO_TIPS");
	       // assertThat(output).contains("Today no tips provided");

	        verify(repo, times(1)).save(any(NotificationEntity.class));
	    }
}
