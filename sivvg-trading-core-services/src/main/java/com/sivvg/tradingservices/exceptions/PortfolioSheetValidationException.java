package com.sivvg.tradingservices.exceptions;

@SuppressWarnings("serial")
public class PortfolioSheetValidationException extends RuntimeException {
	public PortfolioSheetValidationException(String message) {
		super(message);
	}
}