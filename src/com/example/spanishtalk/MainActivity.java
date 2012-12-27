package com.example.spanishtalk;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

import com.example.lib.SessionManagement;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SessionManagement session = new SessionManagement();
		TextView lblName = (TextView) findViewById(R.id.lblName);
		TextView lblEmail = (TextView) findViewById(R.id.lblEmail);

		HashMap<String, String> user = session.getUserDetails();

		String username = user.get(SessionManagement.KEY_USERNAME);
		String user_id = user.get(SessionManagement.KEY_USER_ID);

		// displaying user data
		lblName.setText(Html.fromHtml("Name: <b>" + username + "</b>"));
		lblEmail.setText(Html.fromHtml("ID: <b>" + user_id + "</b>"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
