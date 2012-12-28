package com.example.spanishtalk;


import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lib.BaseUtils;
import com.example.lib.SessionManagement;
import com.example.logic.BaseAction;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.questions.IndexActivity;

public class RegisterActivity extends Activity {
	private EditText vEmail, vUsername, vPassword,
			vConfirmPassword;
	private TextView email_error, username_error, password_error,
			confirm_password_error;
	private LinearLayout error_list;
	private Button registerBtn;
	private ProgressBar progressBar;
	String email, username, password, confirm_password;
	private SessionManagement session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		load_ui();
				
		session = new SessionManagement();
		session.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	private void load_ui() {
		vEmail = (EditText) findViewById(R.id.reg_email);
		vUsername = (EditText) findViewById(R.id.reg_username);
		vPassword = (EditText) findViewById(R.id.reg_password);
		vConfirmPassword = (EditText) findViewById(R.id.reg_confirm_password);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		registerBtn = (Button) findViewById(R.id.linkToRegister);

		error_list = (LinearLayout) findViewById(R.id.reg_error_show);
		email_error = (TextView) findViewById(R.id.reg_email_error);
		username_error = (TextView) findViewById(R.id.reg_username_error);
		password_error = (TextView) findViewById(R.id.reg_password_error);
		confirm_password_error = (TextView) findViewById(R.id.reg_confirm_password_error);

	}

	public void doRegister(View view) {
		if (!validateRegisterForm()) {
			return;
		}
			
		clearErrorList();
		
		new SpanishTalkAsyncTask<Void>() {
			
			@Override
			protected HttpResponse doPost() {
				String email = vEmail.getText().toString().trim();
				String username = vUsername.getText().toString().trim();
				String password = vPassword.getText().toString().trim();

				HttpResponse response = HttpApi.doRegister(email, username, password);
			
				return response;
			}
			
			@Override
			protected void showNoticeView() {
				progressBar.setVisibility(View.VISIBLE);
				registerBtn.setVisibility(View.GONE);
			}
			
			@Override
			protected void hideNoticeView() {
				progressBar.setVisibility(View.GONE);
				registerBtn.setVisibility(View.VISIBLE);
			}
			
			@Override
			protected void onSuccess(HttpResponse response) {
				new saveSessionTask().execute(response);
			}
			
		}.execute();
	}
	
	
	public void showLogin(View view) {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void clearErrorList() {
		error_list.setVisibility(View.GONE);
		email_error.setVisibility(View.GONE);
		username_error.setVisibility(View.GONE);
		password_error.setVisibility(View.GONE);
	}

	public boolean validateRegisterForm() {
		boolean checked = true;
		email = vEmail.getText().toString();
		username = vUsername.getText().toString();
		password = vPassword.getText().toString();
		confirm_password = vConfirmPassword.getText().toString();


		if (BaseUtils.is_str_blank(email)) {
			email_error.setVisibility(View.VISIBLE);
			checked = false;
		} else {
			email_error.setVisibility(View.GONE);
		}
		if (BaseUtils.is_str_blank(username)) {
			username_error.setVisibility(View.VISIBLE);
			checked = false;
		} else {
			username_error.setVisibility(View.GONE);
		}
		if (BaseUtils.is_str_blank(password)) {
			password_error.setVisibility(View.VISIBLE);
			checked = false;
		} else {
			password_error.setVisibility(View.GONE);
		}
		if (BaseUtils.is_str_blank(confirm_password)) {
			confirm_password_error.setVisibility(View.VISIBLE);
			checked = false;
		} else {
			confirm_password_error.setVisibility(View.GONE);
		}
		if (!checked) {
			error_list.setVisibility(View.VISIBLE);
		}
		return checked;
	}
	
	
	public class saveSessionTask extends AsyncTask<HttpResponse, Void, Void> {
		@Override
		protected Void doInBackground(HttpResponse... responses) {
			BaseAction.saveUserSessionByResponse(responses[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if ( (session.getUserId() != 0) && (session.getCookie() != null) ) {
				Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
    			startActivity(intent);
    			finish();
			}
		}
	}
}
