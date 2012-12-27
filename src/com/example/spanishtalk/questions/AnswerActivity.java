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
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.logic.BaseAction;
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.spanishtalk.questions.NewActivity.saveQuestionTask;

public class AnswerActivity extends BaseEventActivity {
	private EditText aContent;
	private Integer questionId;
	private Button sendBtn;
	private ProgressBar progressBar;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        
        aContent = (EditText) findViewById(R.id.input_answer_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		sendBtn = (Button) findViewById(R.id.send_btn);
        
        Intent myIntent = getIntent();
		Bundle b = myIntent.getExtras();
		questionId = b.getInt("questionId");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_answer, menu);
        return true;
    }
    
    
    public void doAnswer(View view) {
		String content = aContent.getText().toString();
		if (BaseUtils.is_str_blank(content)) {
			Context context = getApplicationContext();
			
			Toast toast = Toast.makeText(context, R.string.answer_content_required, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 0);
			toast.show();
			return;
		}

		new DoAnswerTask().execute(questionId);
		
	}
    
    
 // 回复问题
 	public class DoAnswerTask extends AsyncTask<Integer, Void, HttpResponse> {

 		@Override
 		protected HttpResponse doInBackground(Integer... questions) {
 			Map<String, String> params = new HashMap<String, String>();
 			params.put("answer[content]", aContent.getText().toString());

 			String url = BaseUrl.answerCreate + "/"
 					+ Integer.toString(questions[0]) + "/answers.json";
 			HttpResponse response = HttpPack.sendPost(url, params);
 			
 			if (response == null) {
 				cancel(true);
				return null;
			}
 			
 			return response;
 		}

 		
 		
 		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);

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
			progressBar.setVisibility(View.INVISIBLE);

			Context context = getApplicationContext();
			BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			
			Context context = getApplicationContext();

			Integer statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				progressBar.setVisibility(View.GONE);
				BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.server_connection_error));
			} else {
				Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
				intent.putExtra("questionId", questionId);
				startActivity(intent);
			}

			super.onPostExecute(response);
		}
 	}
 	
}
