package com.fundoonotes.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class UserModel {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
      private long id;
	   
	  @NotNull
      private String fname;
	   
	  @NotNull
      private String lname;
      
      @NotNull
      @Column(unique = true)
      private String email;
      
      @NotNull
      private String password;
      
      @NotNull
      @Column(columnDefinition = "boolean default false")
      private boolean isVerified;
      
      @Column(name = "created_at")
  	  public String createdAt;

  	  @Column(name = "modified_time")
  	  public String modifiedTime;
  	  
      public UserModel(){
      }
      
	  public UserModel(long id, @NotNull String fname, @NotNull String lname, @NotNull String email, @NotNull String password, @NotNull boolean isVerified, String createdAt, String modifiedTime) {
		
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.password = password;
		this.isVerified = isVerified;
		this.createdAt = createdAt;
		this.modifiedTime = modifiedTime;
	}

	public UserModel(String fname, String lname, String email, String password) {
		
		super();
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

}