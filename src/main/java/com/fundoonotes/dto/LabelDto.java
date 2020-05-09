package com.fundoonotes.dto;

public class LabelDto {

	private String labelTitle;

	public LabelDto() {
		super();
	}

	public LabelDto(String labelTitle) {
		super();
		this.labelTitle = labelTitle;
	}

	public String getLabelTitle() {
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle;
	}
}
