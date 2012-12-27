package com.example.logic;


import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.lib.HttpPack;
import com.example.spanishtalk.LoginActivity;
import com.example.spanishtalk.R;
import com.example.spanishtalk.LoginActivity.saveSessionTask;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.spanishtalk.questions.IndexActivity;
import com.example.spanishtalk.questions.NewActivity;
import com.example.spanishtalk.questions.ShowActivity;

public class BaseEventActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new CheckLogin(this);
	}
	
	public void returnPreviousPage(View view) {
		this.finish();
	}
	
	
	public void clickGoBack(View view) {
		Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
		intent.putExtra("url", BaseUrl.questions);
		startActivity(intent);
	}
	
	public void clickMyQuestions(View view) {
		Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
		intent.putExtra("url", BaseUrl.questionsMy);
		startActivity(intent);
	}

	public void clickAllQuestions(View view) {
		Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
		intent.putExtra("url", BaseUrl.questions);
		startActivity(intent);
	}

	public void clickAskQuestion(View view) {
		Intent intent = new Intent(getApplicationContext(), NewActivity.class);
		startActivity(intent);
	}
	
	
}
