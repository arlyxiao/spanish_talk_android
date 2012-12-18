package com.example.logic;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;

public class BaseActivity {
	
	public static void checkLogin(Context context) {
		SessionManagement session = new SessionManagement(context);
		if ((session.getUserId() == null) || (session.getCookie() == null)) {
			session.logoutUser();
		}
	}
	
	public static void clickLogout(Context context, View view) {
		SessionManagement session = new SessionManagement(context);
		session.logoutUser();
	}


	
	public static void showFormNotice(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	
	public static void saveUserSessionByResponse(Context context, HttpResponse response) {
		JSONObject user = HttpPack.getJsonByResponse(response);
		try {
			String username = user.getString("username");
			String user_id = user.getString("id");
			String cookie = HttpPack.getCookieByResponse(response);
			
			SessionManagement session = new SessionManagement(context);
			session.createLoginSession(user_id, username);
			session.saveCookie(cookie);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
