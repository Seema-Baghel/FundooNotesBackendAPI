package com.bridgelabz.fundoonotes.responses;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private String email;
	private String subject;
	private String message;

	public EmailObject(String email, String subject, String message) {
		this.email = email;
		this.subject = subject;
		this.message = message;
	}

	public EmailObject() {
	}

}
