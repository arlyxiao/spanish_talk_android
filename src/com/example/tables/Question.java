package com.example.tables;


public class Question {
	int _id;
	int _creator_id;
	String _title;
	String _content;
	String _createdAt;
	String _answerCount;
	String _username;

	public Question() {

	}

	public Question(Integer id) {
		this._id = id;
	}

	public Question(int id, int creator_id, String title, String content) {
		this._id = id;
		this._creator_id = creator_id;
		this._title = title;
		this._content = content;
	}

	public Question(int creator_id, String title, String content) {
		this._creator_id = creator_id;
		this._title = title;
		this._content = content;
	}

	public Question(String title, String content) {
		this._title = title;
		this._content = content;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting user_id
	public int getCreatorId() {
		return this._creator_id;
	}

	// setting user_id
	public void setCreatorId(int creator_id) {
		this._creator_id = creator_id;
	}

	// getting title
	public String getTitle() {
		return this._title;
	}

	// setting title
	public void setTitle(String title) {
		this._title = title;
	}

	// getting content
	public String getContent() {
		return this._content;
	}

	// setting content
	public void setContent(String content) {
		this._content = content;
	}

	public void setCreatedAt(String _createdAt) {
		this._createdAt = _createdAt;
	}

	public String getCreatedAt() {
		return _createdAt;
	}

	public void setAnswerCount(String _answerCount) {
		this._answerCount = _answerCount;
	}

	public String getAnswerCount() {
		return this._answerCount;
	}
	
	
	public String getUsername()
	{
		return _username;
	}

	public void setUsername(String username)
	{
		this._username = username;
	}
}
