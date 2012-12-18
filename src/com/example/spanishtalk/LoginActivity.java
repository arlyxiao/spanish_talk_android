package com.example.spanishtalk;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.BaseAction;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.questions.IndexActivity;

public class LoginActivity extends Activity {
	private EditText edit_text_email, edit_text_password;
	String user_id, email, username, password;
	public SessionManagement session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		session = new SessionManagement(getApplicationContext());
		session.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}


	public void doLogin(View view) {
		if (HttpPack.hasConnected(this)) {
			new LoginTask().execute();
			return;
		}
		BaseAction.showFormNotice(getApplicationContext(), "网络连接超时");
	}
	
	public void showRegister(View view) {
		Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
		startActivity(intent);
		finish();
	}

	public class LoginTask extends AsyncTask<Void, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(Void... arg0) {
			
			edit_text_email = (EditText) findViewById(R.id.login_email);
			edit_text_password = (EditText) findViewById(R.id.login_password);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("user[email]", edit_text_email.getText().toString().trim());
			params.put("user[password]", edit_text_password.getText().toString().trim());
			
			HttpResponse response = HttpPack.sendPost(getApplicationContext(), BaseUrl.login, params);
			
			if (response == null) {
				return null;
			}
			if ( response.getStatusLine().getStatusCode() == 200) {
				BaseAction.saveUserSessionByResponse(getApplicationContext(), response);
			}
			return response;
		
		}
	
		
		@Override
	    protected void onPostExecute(HttpResponse response) {
			if (response == null) {
				BaseAction.showFormNotice(getApplicationContext(), "网络连接超时");
				return;
			}
			
			if (session.getUserId() == null) {
				BaseAction.showFormNotice(getApplicationContext(), "");
				return;
			}
			Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
			startActivity(intent);
			finish();

	        // super.onPostExecute(result);
	    }

	}
}
