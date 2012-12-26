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
import com.example.spanishtalk.R;
import com.example.spanishtalk.LoginActivity.saveSessionTask;
import com.example.spanishtalk.questions.IndexActivity;
import com.example.spanishtalk.questions.NewActivity;
import com.example.spanishtalk.questions.ShowActivity;

public class BaseEventActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new CheckTask().execute();
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
	
	
	public class CheckTask extends AsyncTask<Void, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(Void... arg0) {
			Context context = getApplicationContext();

			HttpResponse response = HttpPack.sendRequest(context, BaseUrl.android);
			if (response == null) {
				cancel(true);
				return null;
			}
			return response;
		}
		
		
		@Override
		protected void onCancelled() {
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
		@Override
		protected void onPostExecute(HttpResponse response) {
			Context context = getApplicationContext();
			Integer statusCode = response.getStatusLine().getStatusCode();
			if ( (statusCode != 200) || isCancelled()) {
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
				
        		BaseAction.showFormNotice(context, context.getString(R.string.login_required));
			}
		}

	}
}
