package com.sivvg.tradingservices.exceptions;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}
