package com.example.lib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.spanishtalk.LoginActivity;
import com.example.spanishtalk.SpanishTalkApplication;

public class SessionManagement {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "SpanishTalkPref";

	private static final String IS_LOGIN = "IsLoggedIn";

	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_COOKIE = "cookie";

	public SessionManagement() {
		this._context = SpanishTalkApplication.context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void createLoginSession(int user_id, String username) {
		editor.putBoolean(IS_LOGIN, true);
		editor.putInt(KEY_USER_ID, user_id);
		editor.putString(KEY_USERNAME, username);
		
		editor.commit();
	}
	
	public void saveCookie(String cookie) {
		editor.putString(KEY_COOKIE, cookie);
		editor.commit();
	}

	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}

	}
	
	public void clear() {
		editor.clear();
		editor.commit();
	}

	public void logoutUser() {
		clear();

		Intent i = new Intent(_context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	public int getUserId() {
		return pref.getInt(KEY_USER_ID, 0);
	}
	
	public String getCookie() {
		return pref.getString(KEY_COOKIE, null);
	}

}