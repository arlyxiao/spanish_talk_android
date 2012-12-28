package com.example.tables;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Question {
	public int id;
	public int creator_id;
	public String title;
	public String content;
	public User creator;
	public ArrayList<Answer> answers;
	public String created_at;
	
	
	public Question() {

	}
	
	public Question(int it, int creator_id, String title, String content) {
		
	}
	
	
	public static ArrayList<Question> build_list_by_json(String questions_json) {
		Gson gson = new Gson();
		
		Type collectionType = new TypeToken<ArrayList<Question>>(){}.getType();
		ArrayList<Question> questions = gson.fromJson(questions_json, collectionType);
		
		return questions;
	}
	
	public static Question build_by_json(String question_json) {
		Gson gson = new Gson();
		
		return gson.fromJson(question_json, Question.class);
	}
	
	
}
