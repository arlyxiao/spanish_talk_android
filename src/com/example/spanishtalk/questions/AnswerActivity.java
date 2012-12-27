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
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.spanishtalk.questions.NewActivity.saveQuestionTask;

public class AnswerActivity extends BaseEventActivity {
	private EditText vContent;
	private Integer questionId;
	private Button sendBtn;
	private ProgressBar progressBar;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        
        vContent = (EditText) findViewById(R.id.input_answer_content);
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
		final String content = vContent.getText().toString();
		if (BaseUtils.is_str_blank(content)) {
			BaseAction.showTopNotice(SpanishTalkApplication.context.getString(R.string.answer_content_required));
			return;
		}
		
		new SpanishTalkAsyncTask(progressBar) {
			@Override
			protected HttpResponse doPost() {
				HttpResponse response = HttpApi.answerQuestion(questionId, content);
				return response;
			}
			
			@Override
			protected void onSuccess(HttpResponse response) {
				Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
				intent.putExtra("questionId", questionId);
				startActivity(intent);
			}
			
		}.execute();
		
	}
  
}
