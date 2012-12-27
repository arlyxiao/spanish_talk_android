package com.example.spanishtalk.questions;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datasource.AnswerBaseAdapter;
import com.example.lib.BaseDialog;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.BaseAction;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.ContactActivity;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.tables.Answer;

public class ShowActivity extends BaseEventActivity {
	private TextView qTitle, qContent, qCreatedAt, qUsername, qCount;
	private LinearLayout answerBox;
	private ProgressBar progressBar;
	private Integer questionId;
	private ArrayList<Answer> answerList;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_show);

		qTitle = (TextView) findViewById(R.id.q_title);
		qUsername = (TextView) findViewById(R.id.q_username);
		qContent = (TextView) findViewById(R.id.q_content);
		qCount = (TextView) findViewById(R.id.q_count);
		qCreatedAt = (TextView) findViewById(R.id.q_created_at);

 		
		lv = (ListView) findViewById(R.id.answer_list_view);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		Intent myIntent = getIntent();
		Bundle b = myIntent.getExtras();
		questionId = b.getInt("questionId");

		new ShowQuestionTask().execute(questionId);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_question_show, menu);
		return true;
	}
	
	public void showContacts(View view) {
		String title = qTitle.getText().toString();
		String content = qContent.getText().toString();
		
		
		Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("content", content);
		startActivity(intent);
	}

	public void showAnswer(View view) {
		Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
		intent.putExtra("questionId", questionId);
		startActivity(intent);
	}

	public void cancelAnswer(View view) {
		answerBox.setVisibility(View.GONE);
	}

	public void getAnswers(JSONArray answers) {
		answerList = new ArrayList<Answer>();

		try {
			Integer size = answers.length();
			
			for (int i = 0; i < size; i++) {
				Answer answer = new Answer();
				JSONObject a = answers.getJSONObject(i);
				JSONObject creator = a.getJSONObject("creator");
				String username = creator.getString("username");
				
				Log.d("answers -----", username);
				
				answer.setID(Integer.parseInt(a.getString("id")));
				answer.setCreatorId(a.getInt("creator_id"));
				answer.setContent(a.getString("content"));
				answer.setUsername(username);
				answer.setCreatedAt(a.getString("created_at").substring(0, 10));
				answerList.add(answer);
			}
			
			lv.setAdapter(new AnswerBaseAdapter(getApplicationContext(), answerList));				
			
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
			
			
			
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
	                    final int position, long id) {
	            	final Object o = lv.getItemAtPosition(position);
	            	final Answer currentAnswer = (Answer) o;
	            	
	            	SessionManagement session = new SessionManagement();
	        		if (session.getUserId() != currentAnswer.getCreatorId()) {
	        			return true;
 	        		}
	            	
	            	AlertDialog.Builder clearConfirmDialog = new AlertDialog.Builder(ShowActivity.this);
                    clearConfirmDialog.setMessage(getApplicationContext().getString(R.string.confirm_delete)).setCancelable(false)
                    .setPositiveButton(getApplicationContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        	
                        	new DeleteAnswerTask().execute(currentAnswer.getID(), position);
                        	
                        }
                    })
                    .setNegativeButton(getApplicationContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = clearConfirmDialog.create();
                    alert.show();

	                return true;
	            }
	        }); 
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 显示问题内容
	public class ShowQuestionTask extends AsyncTask<Integer, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Integer... questions) {
			String url = BaseUrl.questionShow + "/"
					+ Integer.toString(questions[0]) + ".json";
			HttpResponse response = HttpPack.sendRequest(url);


			if (response == null) {
				cancel(true);
				return null;
			}
			
			Integer statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return HttpPack.getJsonByResponse(response);
			}
			
			cancel(true);
			return null;
		}
		
		
		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
			
			if (!HttpPack.hasConnected()) {
				BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.network_error));
				cancel(true);
				return;
			}

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			progressBar.setVisibility(View.GONE);
			
			Context context = getApplicationContext();
			BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(JSONObject question) {
			progressBar.setVisibility(View.GONE);
			
			
			JSONObject creator;
			String username, time;
			JSONArray answers;
			try {
				qTitle.setText(question.getString("title"));
				qContent.setText(question.getString("content"));

				creator = question.getJSONObject("creator");
				username = creator.getString("username");
				qUsername.setText(username);
				
				
				time = question.getString("created_at").substring(0, 10);
				qCreatedAt.setText(time);
				
				
				// 显示回复列表
				answers = question.getJSONArray("answers");
				qCount.setText(Integer.toString(answers.length()) + getApplicationContext().getString(R.string.answers_for_count));
				getAnswers(answers);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			super.onPostExecute(question);
		}
	}
	
	
	
	
	
	
	// 删除回答
	public class DeleteAnswerTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... answers) {
			Integer answerId = answers[0];
			Integer position = answers[1];
			String url = BaseUrl.answerDelete + "/"
					+ Integer.toString(answerId) + ".json";
			HttpResponse response = HttpPack.sendDelete(url);


			if (response == null) {
				cancel(true);
				return null;
			}
			
			Integer statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return position;
			}
			
			cancel(true);
			return null;
		}
		
		
		@Override
		protected void onPreExecute() {				
			if (!HttpPack.hasConnected()) {
				Context context = getApplicationContext();
				BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.network_error));
				cancel(true);
				return;
			}

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {				
			BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(Integer position) {
			answerList.remove((int)position);
        	
        	lv.setAdapter(new AnswerBaseAdapter(getApplicationContext(), answerList));
			super.onPostExecute(position);
		}
	}

	
}
