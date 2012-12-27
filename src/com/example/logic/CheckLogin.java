package com.example.logic;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;

import com.example.spanishtalk.LoginActivity;
import com.example.spanishtalk.SpanishTalkApplication;

public class CheckLogin{
	public CheckLogin(final Activity t) {
		new SpanishTalkAsyncTask<Void>() {
			@Override
			protected HttpResponse doPost() {
				HttpResponse response = HttpApi.testAndroidPage();
				return response;
			}
			
			@Override
			protected void onCancelled() {

			}
			
			@Override
			protected void loginRequired() {
				Intent intent = new Intent(SpanishTalkApplication.context, LoginActivity.class);
				t.startActivity(intent);
				t.finish();
			}
			
			@Override
			protected void showNoticeView() {
			}
			
			@Override
			protected void hideNoticeView() {
			}
		}.execute();
	}
}



