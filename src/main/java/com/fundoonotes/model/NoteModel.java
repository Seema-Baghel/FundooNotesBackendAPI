package com.fundoonotes.model;

import java.time.LocalDateTime;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name= "note_model")
public class NoteModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "note_id")
	private long id;
	
	@Column(length = 200)
	private String title;
	
	@Column(length = 7000)
	private String description;
	
	@Column(columnDefinition = "varchar(100) default '#ffffff'")
	private String NoteColor;
	
	@Column(length = 30)
	private LocalDateTime createdDate;
	
	@Column(length = 30)
	private LocalDateTime updatedDate;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel createdBy;
	
//	private String reminder;
//
//	@Column(columnDefinition = "boolean default false")
//	private boolean reminderStatus;
	
	@ManyToMany
	@JsonIgnore
	private List<LabelModel> labels;
	
	public NoteModel() {
		
	}
	
	public NoteModel(String title, String description) {
		super();
		this.title = title;
		this.description = description;
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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate() {
		setUpdatedDate();
		this.createdDate = LocalDateTime.now();
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate() {
		this.updatedDate = LocalDateTime.now();
	}

	public UserModel getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserModel createdBy) {
		this.createdBy = createdBy;
	}

//	public String getReminder() {
//		return reminder;
//	}
//
//	public void setReminder(String time) {
//		this.reminder = time;
//	}
//
//	public boolean getReminderStatus() {
//		return reminderStatus;
//	}
//
//	public void setReminderStatus(boolean reminderStatus) {
//		this.reminderStatus = reminderStatus;
//	}
}
