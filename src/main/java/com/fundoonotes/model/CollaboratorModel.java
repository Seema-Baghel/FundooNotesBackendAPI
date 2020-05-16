package com.fundoonotes.model;

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

	public List<NoteModel> getListcollaborator() {
		return listcollaborator;
	}

	public void setListcollaborator(List<NoteModel> listcollaborator) {
		this.listcollaborator = listcollaborator;
	}
	
	

}