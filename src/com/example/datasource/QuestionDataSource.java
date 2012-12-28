package com.example.datasource;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lib.HttpPack;
import com.example.tables.Question;


public class QuestionDataSource
{	
	private static int TOTAL;

	public static HttpResponse sendRequest(String url)
	{
		HttpResponse response = HttpPack.sendRequest(url);
		
		if (response == null) {
			return null;
		}
		
		return response;
	}
	
	
	public static List<Question> getQuestions(HttpResponse response) {
		ArrayList<Question> questionList = null;
		JSONObject r = HttpPack.getJsonByResponse(response);
		
		try {
			JSONArray questions = r.getJSONArray("questions");
			String questions_json = questions.toString();	
			TOTAL = Integer.parseInt(r.getString("total"));
			
			questionList = Question.build_list_by_json(questions_json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return questionList;
	}
	
	public static int getTotal()
	{
		return TOTAL;
	}
	

}