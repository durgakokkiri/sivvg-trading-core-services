package com.sivvg.tradingservices.playload;

import jakarta.validation.constraints.NotBlank;

public class AdminReplyDTO {

	@NotBlank(message = "Reply message cannot be empty")
	private String replyMessage;

	@NotBlank(message = "Status is required")
	private String status; // IN_PROGRESS / RESOLVED

	public String getReplyMessage() {
		return replyMessage;
	}

	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AdminReplyDTO(@NotBlank(message = "Reply message cannot be empty") String replyMessage,
			@NotBlank(message = "Status is required") String status) {
		super();
		this.replyMessage = replyMessage;
		this.status = status;
	}

	public AdminReplyDTO() {
		super();
	
	}

}
