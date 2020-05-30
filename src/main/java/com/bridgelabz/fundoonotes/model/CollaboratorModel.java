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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "collaborator_model")
public class CollaboratorModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "collaborator_id")
	private long id;

	@NotNull
	@Column(name = "collaborator_mail")
	private String email;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "note_id")
	private NoteModel note ;
	
	@JsonIgnore
	@ManyToMany(mappedBy="collaborator")
	private List<NoteModel> listcollaborator;

}