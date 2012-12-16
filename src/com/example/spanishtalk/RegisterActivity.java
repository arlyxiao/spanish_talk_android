package com.example.spanishtalk;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lib.BaseDialog;
import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.SpanishTalkBaseActivity;

public class RegisterActivity extends SpanishTalkBaseActivity {
	private EditText edit_text_email, edit_text_username, edit_text_password,
			edit_text_confirm_password;
	private TextView email_error, username_error, password_error,
			confirm_password_error, network_error;
	private LinearLayout error_list;
	String email, username, password, confirm_password;
	private SessionManagement session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		load_ui();
		
		session = new SessionManagement(getApplicationContext());
		session.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	private void load_ui() {
		edit_text_email = (EditText) findViewById(R.id.reg_email);
		edit_text_username = (EditText) findViewById(R.id.reg_username);
		edit_text_password = (EditText) findViewById(R.id.reg_password);
		edit_text_confirm_password = (EditText) findViewById(R.id.reg_confirm_password);

		error_list = (LinearLayout) findViewById(R.id.reg_error_show);
		email_error = (TextView) findViewById(R.id.reg_email_error);
		username_error = (TextView) findViewById(R.id.reg_username_error);
		password_error = (TextView) findViewById(R.id.reg_password_error);
		confirm_password_error = (TextView) findViewById(R.id.reg_confirm_password_error);
		network_error = (TextView) findViewById(R.id.reg_network_error);

	}

	public void doRegister(View view) {
		if (validateRegisterForm()) {

			clearErrorList();
			
			// 先判断网络是否连接正常
			if (HttpPack.hasConnected(this)) {
				new PostRegisterTask().execute();
				return;
			}
			BaseDialog.showSingleAlert("当前网络连接不可用", this);
		}

	}
	
	
	public void showLogin(View view) {
		openActivity(LoginActivity.class);
		finish();
	}

	public void clearErrorList() {
		error_list.setVisibility(View.GONE);
		email_error.setVisibility(View.GONE);
		username_error.setVisibility(View.GONE);
		password_error.setVisibility(View.GONE);
		network_error.setVisibility(View.GONE);
	}

	public boolean validateRegisterForm() {
		boolean checked = true;
		email = edit_text_email.getText().toString();
		username = edit_text_username.getText().toString();
		password = edit_text_password.getText().toString();
		confirm_password = edit_text_confirm_password.getText().toString();

		if (HttpPack.hasConnected(this)) {
			network_error.setVisibility(View.GONE);
		} else {
			network_error.setVisibility(View.VISIBLE);
			checked = false;
		}

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

	public class PostRegisterTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			Map<String, String> params = new HashMap<String, String>();
			params.put("user[username]", edit_text_username.getText()
					.toString().trim());
			params.put("user[email]", edit_text_email.getText().toString().trim());
			params.put("user[password]", edit_text_password.getText()
					.toString().trim());

			HttpResponse response = HttpPack.sendPost(getApplicationContext(), register_url, params);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				saveUserSessionByResponse(response);
			}
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			if (session.getUserId() == null) {
				BaseDialog.showSingleAlert("请填写正确的注册信息", RegisterActivity.this);
				return;
			}
			openActivity(QuestionNewActivity.class);
			finish();
	    }

	}
}
