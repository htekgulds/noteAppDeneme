package com.hasantekgul.todoappdeneme.model;

public class Tag {
	
	int id;
	String tagName;
	String createdAt;
	
	// CONSTRUCTORS
	public Tag() {
		// TODO Auto-generated constructor stub
	}
	
	public Tag(String tagName) {
		this.tagName = tagName;
	}
	
	// SETTERS
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	// GETTERS
	public int getId() {
		return id;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

}
