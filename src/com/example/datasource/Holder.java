package com.example.datasource;

import android.widget.TextView;

public class Holder
{
	private TextView id;
	
	private TextView title;

	private TextView createdAt;
	
	private TextView answerCount;
	
	private TextView username;

	public TextView getID()
	{
		return id;
	}

	public void setID(TextView id)
	{
		this.id = id;
	}

	public TextView getTitle()
	{
		return title;
	}

	public void setTitle(TextView title)
	{
		this.title = title;
	}
	
	public TextView getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(TextView createdAt)
	{
		this.createdAt = createdAt;
	}
	
	public TextView getAnswerCount()
	{
		return answerCount;
	}

	public void setAnswerCount(TextView answerCount)
	{
		this.answerCount = answerCount;
	}
	
	
	public TextView getUsername()
	{
		return username;
	}

	public void setUsername(TextView username)
	{
		this.username = username;
	}

}