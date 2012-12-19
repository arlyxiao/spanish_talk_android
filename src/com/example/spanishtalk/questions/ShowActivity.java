package com.example.spanishtalk.questions;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;

public class ShowActivity extends BaseEventActivity {
	private TextView qTitle, qContent, qCreatedAt, qId;
	private LinearLayout answerBox;
	private EditText aContent;
	private Integer questionId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// BaseAction.checkLogin(getApplicationContext());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_show);

		qId = (TextView) findViewById(R.id.q_id);
		qTitle = (TextView) findViewById(R.id.q_title);
		qContent = (TextView) findViewById(R.id.q_content);
		qCreatedAt = (TextView) findViewById(R.id.q_created_at);

		answerBox = (LinearLayout) findViewById(R.id.answer_box);
		aContent = (EditText) findViewById(R.id.answer_content);

		if (HttpPack.hasConnected(this)) {
			Intent myIntent = getIntent();
			Bundle b = myIntent.getExtras();
			questionId = b.getInt("question_id");

			new ShowQuestionTask().execute(questionId);
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

	public void doAnswer(View view) {
		String content = aContent.getText().toString();
		if (BaseUtils.is_str_blank(content)) {
			Context context = getApplicationContext();
			Toast.makeText(context, "请填写正确的内容", Toast.LENGTH_SHORT).show();
			return;
		}

		new AnswerTask().execute(questionId);
	}

	// 显示问题内容
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
			String username, time;
			try {
				qTitle.setText(question.getString("title"));
				qContent.setText(question.getString("content"));
				qId.setText(question.getString("id"));

				creator = question.getJSONObject("creator");
				username = creator.getString("username");
				time = question.getString("created_at").substring(0, 10);

				qCreatedAt.setText(username + ", " + time);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			super.onPostExecute(question);
		}
	}

	// 回复问题
	public class AnswerTask extends AsyncTask<Integer, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Integer... questions) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("answer[content]", qContent.getText().toString());
			
			String url = BaseUrl.answerCreate + "/"
					+ Integer.toString(questions[0]) + "/answers.json";
			HttpResponse response = HttpPack.sendPost(
					getApplicationContext(), url, params);

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
