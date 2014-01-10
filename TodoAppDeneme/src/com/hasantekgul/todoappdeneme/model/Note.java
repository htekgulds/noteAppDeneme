package com.hasantekgul.todoappdeneme.model;

public class Note {
	
	int id;
	String note;
	int status;
	String createdAt;
	
	
	// CONSTRUCTORS
	
	public Note() {
		// TODO Auto-generated constructor stub
	}
	
	public Note(String note, int status) {
		this.note = note;
		this.status = status;
	}
	
	public Note(int id, String note, int status) {
		this.id = id;
		this.note = note;
		this.status = status;
	}
	
	// SETTERS
	public void setId(int id) {
		this.id = id;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	// GETTERS
	public int getId() {
		return id;
	}
	
	public String getNote() {
		return note;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

}
