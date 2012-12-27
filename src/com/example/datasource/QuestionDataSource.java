package com.example.datasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.example.lib.HttpPack;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.tables.Question;


public class QuestionDataSource
{
	private static QuestionDataSource datasource = null;
	
	private static List<Question> data = null;
	
	private static int SIZE;
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
		JSONObject question, creator;
		String username, time;
		
		JSONObject r = HttpPack.getJsonByResponse(response);
		
		data = new ArrayList<Question>(SIZE);
		try {
			JSONArray questions = r.getJSONArray("questions");
			TOTAL = Integer.parseInt(r.getString("total"));
			SIZE = questions.length();
			
			
			for (int i = 0 ; i < SIZE; i++) {
				Question qr = new Question();
				question = questions.getJSONObject(i);
				
				qr.setID(Integer.parseInt(question.getString("id")));
				qr.setCreatorId(Integer.parseInt(question.getString("creator_id")));
				qr.setTitle(question.getString("title"));
				
				creator = question.getJSONObject("creator");
				username = creator.getString("username");
				qr.setUsername(username);
				
				
				time = question.getString("created_at");
				qr.setCreatedAt(time.substring(0, 10));
				
				
				JSONArray answers = question.getJSONArray("answers");
				String answerCount = Integer.toString(answers.length());
				qr.setAnswerCount(answerCount + SpanishTalkApplication.context.getString(R.string.answers_for_count));
				
				data.add(qr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	 
	
	public static int getTotal()
	{
		return TOTAL;
	}
	

}