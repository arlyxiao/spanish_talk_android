package com.example.logic;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;

abstract public class SpanishTalkBaseActivity extends Activity {
	public final static String login_url = "http://192.168.1.17:3000/users/do_login.json";
	public final static String register_url = "http://192.168.1.17:3000/users.json";
	public final static String questions_url = "http://192.168.1.17:3000/questions.json";
	public final static String my_questions_url = "http://192.168.1.17:3000/questions/my.json";
	public final static String question_create_url = "http://192.168.1.17:3000/questions.json";
	public final static String question_show_url = "http://192.168.1.17:3000/questions";
	

	final public void clickGoBack(View view) {
		this.finish();
	}
	
	public void checkLogin() {
		SessionManagement session = new SessionManagement(getApplicationContext());
		if ((session.getUserId() == null) || (session.getCookie() == null)) {
			session.logoutUser();
		}
	}
	
	public void clickLogout(View view) {
		SessionManagement session = new SessionManagement(getApplicationContext());
		session.logoutUser();
	}


	final public void openActivity(Class<?> cls) {
		startActivity(new Intent(getApplicationContext(), cls));
	}
	
	final public void showFormNotice(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	
	public void saveUserSessionByResponse(HttpResponse response) {
		JSONObject user = HttpPack.getJsonByResponse(response);
		try {
			String username = user.getString("username");
			String user_id = user.getString("id");
			String cookie = HttpPack.getCookieByResponse(response);
			
			SessionManagement session = new SessionManagement(getApplicationContext());
			session.createLoginSession(user_id, username);
			session.saveCookie(cookie);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
