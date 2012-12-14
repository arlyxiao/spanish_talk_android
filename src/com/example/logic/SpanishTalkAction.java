package com.example.logic;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.example.lib.SessionManagement;

public class SpanishTalkAction {
	public static String login_url = "http://192.168.1.17:3000/users/do_login";
	public static String register_url = "http://192.168.1.17:3000/users";

	public static void saveInSession(Context context, JSONObject user) {
		try {
			String username = user.getString("username");
			String user_id = user.getString("user_id");

			SessionManagement session = new SessionManagement(context);
			session.createLoginSession(user_id, username);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
