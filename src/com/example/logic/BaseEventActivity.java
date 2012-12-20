package com.example.logic;


import org.apache.http.HttpResponse;

import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.spanishtalk.LoginActivity;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class BaseEventActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new CheckTask().execute();
	}
	
	
	public void clickGoBack(View view) {
		this.finish();
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
			SessionManagement session = new SessionManagement(getApplicationContext());
			session.clear();
			
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			finish();
		}

	}
}
