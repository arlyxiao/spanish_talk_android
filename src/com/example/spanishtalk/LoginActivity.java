package com.example.spanishtalk;


import org.apache.http.HttpResponse;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.lib.SessionManagement;
import com.example.logic.BaseAction;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.questions.IndexActivity;

public class LoginActivity extends Activity {
	private EditText vEmail, vPassword;
	private Button loginBtn;
	private ProgressBar progressBar;
	String user_id, email, username, password;
	public SessionManagement session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		loginBtn = (Button) findViewById(R.id.linkToLogin);

		session = new SessionManagement();
		session.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void doLogin(View view) {
		new SpanishTalkAsyncTask() {
			
			@Override
			protected HttpResponse doPost() {
				vEmail = (EditText) findViewById(R.id.login_email);
				vPassword = (EditText) findViewById(R.id.login_password);
				
				String email = vEmail.getText().toString().trim();
				String password = vPassword.getText().toString().trim();

				HttpResponse response = HttpApi.doLogin(email, password);
			
				return response;
			}
			
			@Override
			protected void showNoticeView() {
				progressBar.setVisibility(View.VISIBLE);
				loginBtn.setVisibility(View.GONE);
			}
			
			@Override
			protected void hideNoticeView() {
				progressBar.setVisibility(View.GONE);
				loginBtn.setVisibility(View.VISIBLE);
			}
			
			@Override
			protected void onSuccess(HttpResponse response) {
				new saveSessionTask().execute(response);
			}
			
		}.execute();
	}

	public void showRegister(View view) {
		Intent intent = new Intent(getApplicationContext(),
				RegisterActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	
	public class saveSessionTask extends AsyncTask<HttpResponse, Void, Void> {
		@Override
		protected Void doInBackground(HttpResponse... responses) {
			BaseAction.saveUserSessionByResponse(responses[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if ( (session.getUserId() != null) && (session.getCookie() != null) ) {
				Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
    			startActivity(intent);
    			finish();
			}
		}
	}
}
