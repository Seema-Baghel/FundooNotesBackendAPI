package com.fundoonotes.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class LabelModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long labelId;

	@NotBlank
	private String labelTitle;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel userLabel;

	@ManyToMany(mappedBy = "labels")
	@JsonIgnore
	private List<NoteModel> listnote;

	public LabelModel() {
	}

	public LabelModel(long labelId, @NotBlank String labelTitle) {
		super();
		this.labelId = labelId;
		this.labelTitle = labelTitle;
	}

	public LabelModel(long labelId, @NotBlank String labelTitle, UserModel userLabel, List<NoteModel> listnote) {
		super();
		this.labelId = labelId;
		this.labelTitle = labelTitle;
		this.userLabel = userLabel;
		this.listnote = listnote;
	}

	public long getLabelId() {
		return labelId;
	}

	public void setLabelId(long labelId) {
		this.labelId = labelId;
	}

	public String getLabelTitle() {
		return labelTitle;
	}

	public void setLabelTitle(String labelTitle) {
		this.labelTitle = labelTitle;
	}

	public UserModel getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(UserModel userLabel) {
		this.userLabel = userLabel;
	}

	public List<NoteModel> getListnote() {
		return listnote;
	}

	public void setListnote(List<NoteModel> listnote) {
		this.listnote = listnote;
	}

}
