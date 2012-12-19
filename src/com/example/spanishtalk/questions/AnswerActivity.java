package com.example.spanishtalk.questions;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
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
import com.example.logic.BaseEventActivity;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;

public class AnswerActivity extends BaseEventActivity {
	private EditText aContent;
	private Integer questionId;
	private Button confirmBtn;
	private ProgressBar progressBar;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        
        aContent = (EditText) findViewById(R.id.input_answer_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		confirmBtn = (Button) findViewById(R.id.link_to_answer);
        
        Intent myIntent = getIntent();
		Bundle b = myIntent.getExtras();
		questionId = b.getInt("question_id");
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
			
			Toast toast = Toast.makeText(context, "请填写正确的内容", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}

		new DoAnswerTask().execute(questionId);
		
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

 			progressBar.setVisibility(View.VISIBLE);
			confirmBtn.setVisibility(View.INVISIBLE);
 			super.onPreExecute();
 		}

 		@Override
 		protected void onPostExecute(JSONObject question) {
 			progressBar.setVisibility(View.GONE);
 			confirmBtn.setVisibility(View.VISIBLE);

 			Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
			intent.putExtra("question_id", questionId);
			startActivity(intent);
			
 			super.onPostExecute(question);
 		}
 	}
}
