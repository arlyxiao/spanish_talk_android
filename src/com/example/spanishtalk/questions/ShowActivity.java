package com.example.spanishtalk.questions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datasource.AnswerBaseAdapter;
import com.example.lib.BaseDialog;
import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;
import com.example.tables.Answer;
import com.example.tables.Question;

public class ShowActivity extends BaseEventActivity {
	private TextView qTitle, qContent, qCreatedAt, qId;
	private LinearLayout answerBox, answerStatusBox;
	private EditText aContent;
	private Integer questionId;
	private ArrayList<Answer> latestAnswers;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// BaseAction.checkLogin(getApplicationContext());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_show);

		qId = (TextView) findViewById(R.id.q_id);
		qTitle = (TextView) findViewById(R.id.q_title);
		qContent = (TextView) findViewById(R.id.q_content);
		qCreatedAt = (TextView) findViewById(R.id.q_created_at);

		answerStatusBox = (LinearLayout) findViewById(R.id.answer_status_btn);
		answerBox = (LinearLayout) findViewById(R.id.answer_box);
		aContent = (EditText) findViewById(R.id.input_answer_content);
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

		new DoAnswerTask().execute(questionId);
		
		new GetAnswersTask().execute(questionId);
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
	public class DoAnswerTask extends AsyncTask<Integer, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Integer... questions) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("answer[content]", aContent.getText().toString());

			String url = BaseUrl.answerCreate + "/"
					+ Integer.toString(questions[0]) + "/answers.json";
			HttpResponse response = HttpPack.sendPost(getApplicationContext(),
					url, params);

			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpPack.getJsonByResponse(response);
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			answerBox.setVisibility(View.GONE);
			answerStatusBox.setVisibility(View.VISIBLE);

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(JSONObject question) {
			answerBox.setVisibility(View.GONE);
			answerStatusBox.setVisibility(View.INVISIBLE);

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
