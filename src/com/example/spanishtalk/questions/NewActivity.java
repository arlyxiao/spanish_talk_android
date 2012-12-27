package com.example.spanishtalk.questions;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;


public class NewActivity extends BaseEventActivity {
	
	private EditText vTitle, vContent;
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
		vTitle = (EditText) findViewById(R.id.question_title);
		vContent = (EditText) findViewById(R.id.question_content);
		
		sendBtn = (Button) findViewById(R.id.link_to_question);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		user_id = new SessionManagement().getUserId();
	}

	public boolean validateQuestionForm(String title, String content) {
		if (BaseUtils.is_str_blank(title) || BaseUtils.is_str_blank(content)) {
			BaseAction.showTopNotice(SpanishTalkApplication.context.getString(R.string.new_question_required));
			return false;
		}

		return true;
	}

	public void postQuestion(View view) {
		title = vTitle.getText().toString();
		content = vContent.getText().toString();

		if (!validateQuestionForm(title, content)) {
			return;
		}
		new SpanishTalkAsyncTask(progressBar) {
			@Override
			protected HttpResponse doPost() {
				HttpResponse response = HttpApi.createQuestion(title, content);
				return response;
			}
			
			@Override
			protected void onSuccess(HttpResponse response) {
				new saveQuestionTask().execute(response);
			}
			
		}.execute();
	}

	
	public class saveQuestionTask extends AsyncTask<HttpResponse, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(HttpResponse... responses) {
			return HttpPack.getJsonByResponse(responses[0]);
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
