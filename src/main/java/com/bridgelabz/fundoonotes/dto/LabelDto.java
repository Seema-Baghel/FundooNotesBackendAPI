package com.bridgelabz.fundoonotes.dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class LabelDto {

	private String labelTitle;

	public LabelDto() {
		super();
	}

	public LabelDto(String labelTitle) {
		super();
		this.labelTitle = labelTitle;
	}
}
