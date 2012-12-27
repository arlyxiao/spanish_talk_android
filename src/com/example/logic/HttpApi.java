package com.example.logic;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;

import android.util.Log;

import com.example.lib.HttpPack;

public class HttpApi {
	public static HttpResponse testAndroidPage() {
		Log.d(BaseUrl.android, "asdfaf");
		HttpResponse response = HttpPack.sendRequest(BaseUrl.android);
		return response;
	}
	
	
	public static HttpResponse doLogin(String email, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user[email]", email);
		params.put("user[password]", password);

		HttpResponse response = HttpPack.sendPost(BaseUrl.login, params);
		return response;
	}
	
	public static HttpResponse doRegister(String email, String username, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user[username]", username);
		params.put("user[email]", email);
		params.put("user[password]", password);

		HttpResponse response = HttpPack.sendPost(BaseUrl.register, params);
		return response;
	}
	
	public static HttpResponse createQuestion(String title, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("question[title]", title);
		params.put("question[content]", content);

		HttpResponse response = HttpPack.sendPost(BaseUrl.questionCreate, params);
		return response;
	}
	
	
	public static HttpResponse answerQuestion(Integer questionId, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("answer[content]", content);
		
		String url = BaseUrl.answerCreate + "/"
					+ Integer.toString(questionId) + "/answers.json";

		HttpResponse response = HttpPack.sendPost(url, params);
		return response;
	}
	
	
	public static HttpResponse getQuestion(Integer questionId) {
		String url = BaseUrl.questionShow + "/"
				+ Integer.toString(questionId) + ".json";
		HttpResponse response = HttpPack.sendRequest(url);
		
		return response;
	}
	
	public static HttpResponse deleteAnswer(Integer answerId) {
		String url = BaseUrl.answerDelete + "/"
				+ Integer.toString(answerId) + ".json";
		HttpResponse response = HttpPack.sendDelete(url);
		
		return response;
	}
	
	
	public static HttpResponse deleteQuestion(Integer questionId) {
		String url = BaseUrl.questionDelete + "/"
				+ Integer.toString(questionId) + ".json";
		HttpResponse response = HttpPack.sendDelete(url);
		return response;
	}
}
