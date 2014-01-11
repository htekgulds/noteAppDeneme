package com.hasantekgul.todoappdeneme.model;

public class CustomList {
	
	int status;
	String title;
	String text;
	int color;
	boolean isChecked;
	
	// CONSTRUCTORS	
	public CustomList() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomList(String title, String text) {
		
		this.title = title;
		this.text = text;
		this.status = 0;
	}
	
	public CustomList(String title, String text, int status) {

		this.title = title;
		this.text = text;
		this.status = status;
	}
	
	// SETTERS
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	// GETTERS
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}
	
	public int getStatus() {
		return status;
	}
}
