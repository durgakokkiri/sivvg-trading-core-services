package com.sivvg.tradingservices.exceptions;

@SuppressWarnings("serial")
public class HomeResourceNotFoundException extends RuntimeException {
	public HomeResourceNotFoundException(String message) {
		super(message);
	}
}
