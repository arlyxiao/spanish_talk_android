package com.example.spanishtalk.questions;


import org.apache.http.HttpResponse;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.lib.BaseUtils;
import com.example.logic.BaseAction;
import com.example.logic.BaseEventActivity;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;


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

    
    
    public void doAnswer(View view) {
		final String content = vContent.getText().toString();
		if (BaseUtils.is_str_blank(content)) {
			BaseAction.showTopNotice(SpanishTalkApplication.context.getString(R.string.answer_content_required));
			return;
		}
		
		new SpanishTalkAsyncTask<Void>(progressBar) {
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
