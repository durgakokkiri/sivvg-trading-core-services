package com.sivvg.tradingservices.playload;

import jakarta.validation.constraints.NotBlank;

public class ContactUSRequestDto {

	@NotBlank(message = "Message cannot be empty")
	private String message;

	@NotBlank(message = "Type is required")
	private String type;
	// CALLBACK / EMAIL / CHAT

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ContactUSRequestDto(@NotBlank(message = "Message cannot be empty") String message,
			@NotBlank(message = "Type is required") String type) {
		super();
		this.message = message;
		this.type = type;
	}

	@Override
	public String toString() {
		return "ContactRequestDto [message=" + message + ", type=" + type + "]";
	}

}