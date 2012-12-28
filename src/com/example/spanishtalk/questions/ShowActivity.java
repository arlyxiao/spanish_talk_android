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
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
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
		
		
		new SpanishTalkAsyncTask<Integer>(progressBar) {
			@Override
			protected HttpResponse doPost() {
				HttpResponse response = HttpApi.getQuestion(questionId);
				return response;
			}
			
			@Override
			protected void onSuccess(HttpResponse response) {
				new showQuestionTask().execute(response);
			}
			
		}.execute(questionId);

	}
	
	
	
	public class showQuestionTask extends AsyncTask<HttpResponse, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(HttpResponse... responses) {
			return HttpPack.getJsonByResponse(responses[0]);
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
				qUsername.setText(username);
				
				
				time = question.getString("created_at").substring(0, 10);
				qCreatedAt.setText(time);
				
				
				// 显示回复列表
				String answers_json = question.getString("answers");
				answerList = Answer.build_by_json(answers_json);
				qCount.setText(Integer.toString(answerList.size()) + getApplicationContext().getString(R.string.answers_for_count));

				getAnswers(answerList);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
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

	public void getAnswers(final ArrayList<Answer> answerList) {
		

		lv.setAdapter(new AnswerBaseAdapter(getApplicationContext(), answerList));				
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				
			}
		});
		
		
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

		    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		            final int position, long id) {
		    	final Object o = lv.getItemAtPosition(position);
		    	final Answer currentAnswer = (Answer) o;
		    	
		    	SessionManagement session = new SessionManagement();
				if (session.getUserId() != currentAnswer.creator_id) {
					return true;
				}
		    	
		    	AlertDialog.Builder clearConfirmDialog = new AlertDialog.Builder(ShowActivity.this);
		        clearConfirmDialog.setMessage(getApplicationContext().getString(R.string.confirm_delete)).setCancelable(false)
		        .setPositiveButton(getApplicationContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	
		            	
		            	new SpanishTalkAsyncTask<Void>() {
		            		@Override
		        			protected HttpResponse doPost() {
		        				HttpResponse response = HttpApi.deleteAnswer(currentAnswer.id);
		        				return response;
		        			}
		        			
		        			@Override
		        			protected void onSuccess(HttpResponse response) {
		        				answerList.remove((int)position);
		        	        	
		        	        	lv.setAdapter(new AnswerBaseAdapter(getApplicationContext(), answerList));
		        			}
		        			
		        			protected void showNoticeView() {
		         			}
		        			
		        			protected void hideNoticeView() {
		         			}
		            	}.execute();
		            	
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
	}


	
}
