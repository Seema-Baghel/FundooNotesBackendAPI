package com.fundoonotes.dto;

import org.springframework.stereotype.Component;

@Component
public class CollaboratorDto {

	private String email;

	public String getEmail() {
		return email;
	}

	public CollaboratorDto() {
		super();
	}

	public CollaboratorDto(String email) {
		super();
		this.email = email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
