package com.sivvg.tradingservices.exceptions;

@SuppressWarnings("serial")
public class PortfolioFileValidationException extends RuntimeException {
	public PortfolioFileValidationException(String message) {
		super(message);
	}
}