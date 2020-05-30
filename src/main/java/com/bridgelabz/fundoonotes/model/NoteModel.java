package com.bridgelabz.fundoonotes.model;

import java.time.LocalDateTime;
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
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "note_model")
public class NoteModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "note_id")
	private long noteId;
	
	@Column(length = 200)
	private String title;
	
	@Column(length = 7000)
	private String description;
	
	@Column
	private String NoteColor;
	
	@Column
	private LocalDateTime createdDate;
	
	@Column
	private LocalDateTime updatedDate;
	
	private LocalDateTime reminder;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel createdBy;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isPinned;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isArchived;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isTrashed;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<LabelModel> labels;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<CollaboratorModel> collaborator;
	
	public NoteModel(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

}
