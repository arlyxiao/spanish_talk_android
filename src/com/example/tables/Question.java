package com.example.tables;

public class Question {
	int _id;
    String _title;
    String _content;
    
    public Question() {
    	
    }
    
    public Question(int id, String title, String content) {
    	this._id = id;
    	this._title = title;
    	this._content = content;
    }
    
    public Question(String title, String content) {
    	this._title = title;
    	this._content = content;
    }
    
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting title
    public String getTitle(){
        return this._title;
    }
 
    // setting title
    public void setTitle(String title){
        this._title = title;
    }
 
    // getting content
    public String getContent(){
        return this._content;
    }
 
    // setting content
    public void setContent(String content){
        this._content = content;
    }
}
