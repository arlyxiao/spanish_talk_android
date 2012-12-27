package com.example.spanishtalk.questions;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.BaseAction;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;
import com.example.spanishtalk.RegisterActivity.saveSessionTask;

public class NewActivity extends BaseEventActivity {

	private EditText edit_text_title, edit_text_content;
	private Button sendBtn;
	private ProgressBar progressBar;
	String title, content;
	Integer user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_new);

		loadUi();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_question, menu);
		return true;
	}

	private void loadUi() {
		edit_text_title = (EditText) findViewById(R.id.question_title);
		edit_text_content = (EditText) findViewById(R.id.question_content);
		
		sendBtn = (Button) findViewById(R.id.link_to_question);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		user_id = new SessionManagement().getUserId();
	}

	public boolean validateQuestionForm(String title, String content) {
		if (BaseUtils.is_str_blank(title) || BaseUtils.is_str_blank(content)) {
			Context context = getApplicationContext();
			Toast.makeText(context, R.string.new_question_required,
					Toast.LENGTH_SHORT).show();

			return false;
		}

		return true;
	}

	public void postQuestion(View view) {
		title = edit_text_title.getText().toString();
		content = edit_text_content.getText().toString();

		if (validateQuestionForm(title, content)) {
			new PostQuestionTask().execute();
		}
		
	}

	public class PostQuestionTask extends AsyncTask<Void, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(Void... arg0) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("question[title]", edit_text_title.getText().toString());
			params.put("question[content]", edit_text_content.getText()
					.toString());

			HttpResponse response = HttpPack.sendPost(BaseUrl.questionCreate, params);
	
			if (response == null) {
				cancel(true);
				return null;
			}
			
			return response;
		}
		
		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);

			if (!HttpPack.hasConnected(NewActivity.this)) {
				Context context = getApplicationContext();
				BaseAction.showFormNotice(context,
						context.getString(R.string.network_error));
				cancel(true);
				return;
			}

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			progressBar.setVisibility(View.GONE);

			Context context = getApplicationContext();
			BaseAction.showFormNotice(context, context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			
			Context context = getApplicationContext();

			Integer statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				progressBar.setVisibility(View.GONE);
				BaseAction.showFormNotice(context, context.getString(R.string.server_connection_error));
			} else {
				new saveQuestionTask().execute(response);
			}

			super.onPostExecute(response);
		}

	}
	
	
	
	public class saveQuestionTask extends AsyncTask<HttpResponse, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(HttpResponse... responses) {
			JSONObject q;
			
			q = HttpPack.getJsonByResponse(responses[0]);
			
			return q;
		}
		
		@Override
		protected void onPostExecute(JSONObject q) {
			try {
				int questionId = q.getInt("question_id");
				Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
				intent.putExtra("questionId", questionId);
				startActivity(intent);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
