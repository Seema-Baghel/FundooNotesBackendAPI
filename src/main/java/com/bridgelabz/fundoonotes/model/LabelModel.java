package com.bridgelabz.fundoonotes.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LabelModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "label_id")
	private long labelId;

	@NotBlank
	private String labelTitle;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel userLabel;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "labels")
	private List<NoteModel> listnote;

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
}