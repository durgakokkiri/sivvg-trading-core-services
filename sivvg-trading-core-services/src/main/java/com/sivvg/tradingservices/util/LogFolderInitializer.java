package com.sivvg.tradingservices.util;

import javax.annotation.PostConstruct;
import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class LogFolderInitializer {

	@PostConstruct
	public void init() {
	    File logDir = new File("C:\\sivvg\\log");
	    if (!logDir.exists()) {
	        logDir.mkdirs();
	    }
	}

}