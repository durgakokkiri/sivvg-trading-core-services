package com.sivvg.tradingservices.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Profiles")
public class Profilemodel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private long phoneno;
	private String email;

	public Profilemodel(long id, String username, String password, long phoneno, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.phoneno = phoneno;
		this.email = email;
	}

	public Profilemodel() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(long phoneno) {
		this.phoneno = phoneno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Profilemodel [id=" + id + ", username=" + username + ", password=" + password + ", phoneno=" + phoneno
				+ ", email=" + email + "]";
	}

}
