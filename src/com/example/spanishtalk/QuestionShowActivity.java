package com.example.spanishtalk;


import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.lib.HttpPack;
import com.example.logic.SpanishTalkBaseActivity;

public class QuestionShowActivity extends SpanishTalkBaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		checkLogin();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_show);
		
		Intent myIntent = getIntent();
		Bundle b = myIntent.getExtras();
		Integer question_id = b.getInt("question_id");
		
		new ShowQuestionTask().execute(question_id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_question_show, menu);
		return true;
	}
	
	
	public class ShowQuestionTask extends AsyncTask<Integer, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Integer... questions) {
			question_show_url = question_show_url + "/" + Integer.toString(questions[0]);
			HttpResponse response = HttpPack.sendRequest(getApplicationContext(), question_show_url);

			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpPack.getJsonByResponse(response);
			}

			return null;
		}
		
		
		@Override
	    protected void onPostExecute(JSONObject question) {			
			try {
				TextView q_title = (TextView) findViewById(R.id.question_title);
				TextView q_content = (TextView) findViewById(R.id.question_content);
				
				q_title.setText(question.getString("title"));
				q_content.setText(question.getString("content"));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
	        super.onPostExecute(question);
	    }
	}


}
