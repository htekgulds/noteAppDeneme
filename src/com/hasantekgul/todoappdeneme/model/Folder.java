package com.hasantekgul.todoappdeneme.model;

public class Folder {
	
	int id;
	String folderName;
	String createdAt;
	
	// CONSTRUCTORS
	public Folder() {
		// TODO Auto-generated constructor stub
	}
	
	public Folder(String folderName) {
		this.folderName = folderName;
	}
	
	// SETTERS
	public void setId(int id) {
		this.id = id;
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	// GETTERS
	public int getId() {
		return id;
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

}
