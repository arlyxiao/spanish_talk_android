package com.example.logic;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;

public class BaseAction {
	
	public static void checkLogin() {
		SessionManagement session = new SessionManagement();
		if ((session.getUserId() == null) || (session.getCookie() == null)) {
			session.logoutUser();
		}
	}
	
	public static void clickLogout(View view) {
		SessionManagement session = new SessionManagement();
		session.logoutUser();
	}


	
	public static void showFormNotice(String message) {
		Toast toast = Toast.makeText(SpanishTalkApplication.context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public static void showTopNotice(String message) {
		Toast toast = Toast.makeText(SpanishTalkApplication.context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
	}

	
	public static void saveUserSessionByResponse(HttpResponse response) {
		JSONObject user = HttpPack.getJsonByResponse(response);
		try {
			String username = user.getString("username");
			Log.d("aadasdfasdf", username);
			String user_id = user.getString("id");
			String cookie = HttpPack.getCookieByResponse(response);
			
			SessionManagement session = new SessionManagement();
			session.createLoginSession(user_id, username);
			session.saveCookie(cookie);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
