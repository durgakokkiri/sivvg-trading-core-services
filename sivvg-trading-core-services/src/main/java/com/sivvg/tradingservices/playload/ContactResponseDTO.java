package com.sivvg.tradingservices.playload;

public class ContactResponseDTO {

	private Long id;
	private String message;
	private String type;
	private String status;
	private String adminReply;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdminReply() {
		return adminReply;
	}

	public void setAdminReply(String adminReply) {
		this.adminReply = adminReply;
	}

	public ContactResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactResponseDTO(Long id, String message, String type, String status, String adminReply) {
		super();
		this.id = id;
		this.message = message;
		this.type = type;
		this.status = status;
		this.adminReply = adminReply;
	}

	@Override
	public String toString() {
		return "ContactResponseDTO [id=" + id + ", message=" + message + ", type=" + type + ", status=" + status
				+ ", adminReply=" + adminReply + "]";
	}

}
