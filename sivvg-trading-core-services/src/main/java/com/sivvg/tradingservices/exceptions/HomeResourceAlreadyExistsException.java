package com.sivvg.tradingservices.exceptions;

@SuppressWarnings("serial")
public class HomeResourceAlreadyExistsException extends RuntimeException {
	public HomeResourceAlreadyExistsException(String message) {
		super(message);
	}
}
