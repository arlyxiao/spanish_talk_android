package com.example.spanishtalk;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.lib.BaseDialog;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.SpanishTalkBaseActivity;

public class LoginActivity extends SpanishTalkBaseActivity {
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
		BaseDialog.showSingleAlert("当前网络连接不可用", this);
	}

	public class LoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			edit_text_email = (EditText) findViewById(R.id.login_email);
			edit_text_password = (EditText) findViewById(R.id.login_password);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("user[email]", edit_text_email.getText().toString());
			params.put("user[password]", edit_text_password.getText()
					.toString());
			
			HttpResponse response = HttpPack.sendPost(getApplicationContext(), login_url, params);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				String cookie = HttpPack.getCookieByResponse(response);
				session.saveCookie(cookie);

				saveUserSessionByResponse(response);
			}

			return null;
		}
		
	
		
		@Override
	    protected void onPostExecute(Void result) {
			if (session.getUserId() == null) {
				showFormNotice("请填写正确的用户名或密码");
				return;
			}
			openActivity(QuestionNewActivity.class);
			finish();

	        super.onPostExecute(result);
	    }

	}
}
