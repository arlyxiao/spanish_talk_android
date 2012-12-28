package com.example.tables;


import com.google.gson.Gson;


public class User {
  	public int id;
	public String username;
	
	public static User build_by_json(String user_json) {
		Gson gson = new Gson();
		
		return gson.fromJson(user_json, User.class);
	}
	
}
