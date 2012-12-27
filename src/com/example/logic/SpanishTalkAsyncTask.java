package com.example.logic;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.os.AsyncTask;

import com.example.lib.HttpPack;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;




public class SpanishTalkAsyncTask extends AsyncTask<Void, Void, HttpResponse> {
	private Context context = SpanishTalkApplication.context;
	
	

	@Override
	protected HttpResponse doInBackground(Void... arg0) {
		HttpResponse response = doPost();
		
		if (response == null) {
			cancel(true);
			return null;
		}
		
		return response;
	}
	
	@Override
	protected void onPreExecute() {
		showNoticeView();
		
		if (!HttpPack.hasConnected()) {
			BaseAction.showFormNotice(context.getString(R.string.network_error));
			cancel(true);
			return;
		}

		super.onPreExecute();
	}

	@Override
	protected void onCancelled() {
		hideNoticeView();
		BaseAction.showFormNotice(context.getString(R.string.server_connection_error));
	}

	@Override
	protected void onPostExecute(HttpResponse response) {
		hideNoticeView();
		
		Integer statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
        	case 200:  
        		onSuccess(response);
        		break;
        	default:
        		BaseAction.showFormNotice(context.getString(R.string.login_form_error));
        		break;
		}
		
		super.onPostExecute(response);
	}
	
	protected HttpResponse doPost() {
		HttpResponse response = null;
		return response;
	}
	
	protected void showNoticeView() {
		
	}
	
	protected void hideNoticeView() {
		
	}
	
	protected void onSuccess(HttpResponse response) {
		
	}

}