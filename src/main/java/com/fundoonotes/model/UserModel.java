package com.fundoonotes.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
      
//      private Date creatorStamp;
      
      @Column(name = "created_at")
  	  public Date createdAt;

  	   @Column(name = "modified_time")
  	   public Date modifiedTime;
  	  
      public UserModel(){
      }
      
	  public UserModel(long id, @NotNull String fname, @NotNull String lname, @NotNull String email, @NotNull String password, @NotNull boolean isVerified, Date createdAt, Date modifiedTime) {
		
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	
//	public Date getCreatorStamp() {
//		return creatorStamp;
//	}
//
//	public void setCreatorStamp(Date creatorStamp) {
//		this.creatorStamp = creatorStamp;
//	}
	
	
  
}