package com.example.tables;

public class Answer {
	int _id;
	int _creator_id;
 	String _content;
	String _createdAt;
	User _creator;
	String _username;
	 
	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public int getCreatorId() {
		return this._creator_id;
	}

	public void setCreatorId(int creator_id) {
		this._creator_id = creator_id;
	}
	
	public String getUsername() {
		return this._username;
	}

	public void setUsername(String _username) {
		this._username = _username;
	}
	
	


	public String getContent() {
		return this._content;
	}

	public void setContent(String content) {
		this._content = content;
	}
	
	public void setCreatedAt(String _createdAt) {
		this._createdAt = _createdAt;
	}

	public String getCreatedAt() {
		return _createdAt;
	}
}
