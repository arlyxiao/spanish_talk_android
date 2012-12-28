package com.example.logic;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.spanishtalk.questions.IndexActivity;
import com.example.spanishtalk.questions.NewActivity;

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
