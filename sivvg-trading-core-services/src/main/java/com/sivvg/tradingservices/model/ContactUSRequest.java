package com.sivvg.tradingservices.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact_requests")
public class ContactUSRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 🔗 LINK TO LOGGED-IN USER
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@Column(nullable = false, length = 1000)
	private String message;

	@Column(nullable = false)
	private String type; // CALLBACK / EMAIL / CHAT

	@Column(nullable = false)
	private String status; // NEW / IN_PROGRESS / COMPLETED

	@Column(nullable = false)
	private boolean autoReplySent;

	@Column(length = 2000)
	private String adminReply;

	// ✅ FIXED FIELD
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	// ===== GETTERS & SETTERS =====

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAutoReplySent() {
		return autoReplySent;
	}

	public void setAutoReplySent(boolean autoReplySent) {
		this.autoReplySent = autoReplySent;
	}

	public String getAdminReply() {
		return adminReply;
	}

	public void setAdminReply(String adminReply) {
		this.adminReply = adminReply;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	// ✅ FIXED METHOD
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ContactUSRequest() {
		super();
	}

	@Override
	public String toString() {
		return "ContactUSRequest [id=" + id + ", message=" + message + ", type=" + type + ", status=" + status
				+ ", autoReplySent=" + autoReplySent + ", createdAt=" + createdAt + "]";
	}
}
