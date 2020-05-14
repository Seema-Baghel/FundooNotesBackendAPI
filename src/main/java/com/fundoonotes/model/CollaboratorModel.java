package com.fundoonotes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "collaborator")
public class CollaboratorModel {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private String email;

	@JsonIgnore
	@NotNull
	@ManyToOne
	@JoinColumn(name = "noteID")
	private NoteModel note ;
	
	public CollaboratorModel() {
		super();
	}

	public CollaboratorModel(long id, @NotNull String email, @NotNull NoteModel note) {
		super();
		this.id = id;
		this.email = email;
		this.note = note;
	}

	@Override
	public String toString() {
		return "Collaborator [id=" + id + ", email=" + email + ", note=" + note + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public NoteModel getNote() {
		return note;
	}

	public void setNote(NoteModel note) {
		this.note = note;
	}

//	public List<NoteModel> getListcollaborator() {
//		return listcollaborator;
//	}
//
//	public void setListcollaborator(List<NoteModel> listcollaborator) {
//		this.listcollaborator = listcollaborator;
//	}
	
	
}