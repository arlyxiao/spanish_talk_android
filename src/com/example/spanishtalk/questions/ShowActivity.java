package com.example.spanishtalk.questions;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lib.HttpPack;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;

public class ShowActivity extends BaseEventActivity {
	private TextView qTitle, qContent, qCreatedAt;
	private LinearLayout answerBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// BaseAction.checkLogin(getApplicationContext());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_show);

		qTitle = (TextView) findViewById(R.id.q_title);
		qContent = (TextView) findViewById(R.id.q_content);
		qCreatedAt = (TextView) findViewById(R.id.q_created_at);
		answerBox = (LinearLayout) findViewById(R.id.answer_box);

		if (HttpPack.hasConnected(this)) {
			Intent myIntent = getIntent();
			Bundle b = myIntent.getExtras();
			Integer question_id = b.getInt("question_id");

			new ShowQuestionTask().execute(question_id);
			return;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_question_show, menu);
		return true;
	}
	
	public void showAnswer(View view) {
		answerBox.setVisibility(View.VISIBLE);
	}
	
	public void cancelAnswer(View view) {
		answerBox.setVisibility(View.GONE);
	}
	
	

	public class ShowQuestionTask extends AsyncTask<Integer, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Integer... questions) {
			String url = BaseUrl.questionShow + "/"
					+ Integer.toString(questions[0]) + ".json";
			HttpResponse response = HttpPack.sendRequest(
					getApplicationContext(), url);

			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpPack.getJsonByResponse(response);
			}

			return null;
		}

		@Override
		protected void onPostExecute(JSONObject question) {
			JSONObject creator;
			String username;
			try {
				qTitle.setText(question.getString("title"));
				qContent.setText(question.getString("content"));
				
				creator = question.getJSONObject("creator");
				username = creator.getString("username");
				
				String time = question.getString("created_at").substring(0, 10);
				
				qCreatedAt.setText(username + ", " + time);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			super.onPostExecute(question);
		}
	}

}
