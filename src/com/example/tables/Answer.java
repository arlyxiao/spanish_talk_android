package com.example.tables;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Answer {
	
	public int id;
	public int creator_id;
	public String content;
	public User creator;
	public String created_at;
	
	
	public static ArrayList<Answer> build_by_json(String answer_json) {
		Gson gson = new Gson();
		
		Type collectionType = new TypeToken<ArrayList<Answer>>(){}.getType();
		ArrayList<Answer> answers = gson.fromJson(answer_json, collectionType);
		
		return answers;
	}
}
