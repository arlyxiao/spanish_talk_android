package com.example.logic;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import com.example.lib.HttpPack;

public class HttpApi {
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
}
