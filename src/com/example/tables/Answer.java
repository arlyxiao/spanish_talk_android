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
	
	
	public static ArrayList<Answer> build_list_by_json(String answers_json) {
		Gson gson = new Gson();
		
		Type collectionType = new TypeToken<ArrayList<Answer>>(){}.getType();
		ArrayList<Answer> answers = gson.fromJson(answers_json, collectionType);
		
		return answers;
	}
}
