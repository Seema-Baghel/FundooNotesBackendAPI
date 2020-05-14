package com.fundoonotes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name= "note_model")
public class NoteModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length = 200)
	private String title;
	
	@Column(length = 7000)
	private String description;
	
	@Column(columnDefinition = "varchar(100) default '#ffffff'")
	private String NoteColor;
	
	@Column(length = 30)
	private String createdDate;
	
	@Column(length = 30)
	private String updatedDate;
	
	private LocalDate reminder;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel createdBy;
	
//	@OneToMany
//	@JoinColumn(name = "labelName")
//	private LabelModel label;
//	
//	@OneToMany
//	@JoinColumn(name = "Collaborator")
//	private CollaboratorModel collaborate;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<LabelModel> labels;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<CollaboratorModel> collaborator;

	public NoteModel() {
		
	}
	
	public NoteModel(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}
	public NoteModel(String title, String description, String createdDate, String updatedDate, UserModel createdBy, String notecolor) {
		super();
		this.title = title;
		this.description = description;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.createdBy = createdBy;
		this.NoteColor = notecolor;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNoteColor() {
		return NoteColor;
	}

	public void setNoteColor(String noteColor) {
		NoteColor = noteColor;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public LocalDate getReminder() {
		return reminder;
	}

	public void setReminder(LocalDate reminder) {
		this.reminder = reminder;
	}

	public UserModel getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserModel createdBy) {
		this.createdBy = createdBy;
	}

	public List<LabelModel> getLabels() {
		return labels;
	}

	public void setLabels(List<LabelModel> labels) {
		this.labels = labels;
	}

	public List<CollaboratorModel> getCollaborator() {
		return collaborator;
	}

	public void setCollaborator(List<CollaboratorModel> collaborator) {
		this.collaborator = collaborator;
	}
	
	
//	public LabelModel getLabel() {
//		return label;
//	}
//
//	public void setLabel(LabelModel label) {
//		this.label = label;
//	}
//
//	public CollaboratorModel getCollaborate() {
//		return collaborate;
//	}
//
//	public void setCollaborate(CollaboratorModel collaborate) {
//		this.collaborate = collaborate;
//	}

	

}
