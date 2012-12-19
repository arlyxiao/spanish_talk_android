package com.example.spanishtalk.questions;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.datasource.AnswerBaseAdapter;
import com.example.lib.BaseDialog;
import com.example.lib.HttpPack;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;
import com.example.tables.Answer;

public class ShowActivity extends BaseEventActivity {
	private TextView qTitle, qContent, qCreatedAt;
	private LinearLayout answerBox;
	
	private Integer questionId;
	private ArrayList<Answer> latestAnswers;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// BaseAction.checkLogin(getApplicationContext());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_show);

		qTitle = (TextView) findViewById(R.id.q_title);
		qContent = (TextView) findViewById(R.id.q_content);
		qCreatedAt = (TextView) findViewById(R.id.q_created_at);

 		
		lv = (ListView) findViewById(R.id.answer_list_view);
		
		Intent myIntent = getIntent();
		Bundle b = myIntent.getExtras();
		questionId = b.getInt("question_id");


		if (HttpPack.hasConnected(this)) {
			
			new ShowQuestionTask().execute(questionId);
			
			new GetAnswersTask().execute(questionId);
		
			return;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_question_show, menu);
		return true;
	}

	public void showAnswer(View view) {
		Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
		intent.putExtra("question_id", questionId);
		startActivity(intent);
	}

	public void cancelAnswer(View view) {
		answerBox.setVisibility(View.GONE);
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

	

	// 回复列表
	public class GetAnswersTask extends AsyncTask<Integer, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Integer... questions) {
			String url = BaseUrl.answers + "/"
					+ Integer.toString(questions[0]) + "/answers.json";
			HttpResponse response = HttpPack.sendRequest(
					getApplicationContext(), url);

			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpPack.getJsonArrayByResponse(response);
			}

			return null;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(JSONArray answers) {
			latestAnswers = new ArrayList<Answer>();

			try {
				Integer size = answers.length();
				
				for (int i = 0; i < size; i++) {
					Answer answer = new Answer();
					JSONObject a = answers.getJSONObject(i);
					JSONObject creator = a.getJSONObject("creator");
					String username = creator.getString("username");
					
					answer.setID(Integer.parseInt(a.getString("id")));
					answer.setContent(a.getString("content"));
					answer.setCreatedAt(username + ", " + a.getString("created_at").substring(0, 10));
					latestAnswers.add(answer);
				}
				
				lv.setAdapter(new AnswerBaseAdapter(getApplicationContext(), latestAnswers));				
				
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> a, View v, int position,
							long id) {
						Object o = lv.getItemAtPosition(position);
						Answer fullObject = (Answer) o;
						BaseDialog.showSingleAlert(fullObject.getContent(),
								ShowActivity.this);
					}
				});
				
			} catch (JSONException e) {
				e.printStackTrace();
			}

			super.onPostExecute(answers);
		}
	}

}
