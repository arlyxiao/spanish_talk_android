package com.example.logic;


import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lib.HttpPack;
import com.example.spanishtalk.LoginActivity;
import com.example.spanishtalk.questions.IndexActivity;
import com.example.spanishtalk.questions.NewActivity;
import com.example.spanishtalk.questions.ShowActivity;

public class BaseEventActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new CheckTask().execute();
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
	
	
	public class CheckTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			Context context = getApplicationContext();

			HttpResponse response = HttpPack.sendRequest(context, BaseUrl.android);
			if (response == null) {
				
				cancel(true);
			}
			return null;
		}
		
		
		@Override
		protected void onCancelled() {
			//SessionManagement session = new SessionManagement(getApplicationContext());
			//session.clear();
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
		@Override
		protected void onPostExecute(Void result) {
		    if (isCancelled()) {
		    	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
 		    }
		}

	}
}
