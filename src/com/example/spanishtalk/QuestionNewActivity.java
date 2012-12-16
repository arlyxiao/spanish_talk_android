package com.example.spanishtalk;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lib.BaseUtils;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.lib.SqliteLog;
import com.example.logic.SpanishTalkBaseActivity;
import com.example.tables.Question;
import com.example.tables.QuestionsHandler;

public class QuestionNewActivity extends SpanishTalkBaseActivity {

	private EditText edit_text_title, edit_text_content;
	String title, content;
	Integer user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_new);

		loadUi();

		// new PostQuestionTask().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_question, menu);
		return true;
	}

	private void loadUi() {
		edit_text_title = (EditText) findViewById(R.id.question_title);
		edit_text_content = (EditText) findViewById(R.id.question_content);

		user_id = new SessionManagement(getApplicationContext()).getUserId();
	}

	public boolean validateQuestionForm(String title, String content) {
		if (BaseUtils.is_str_blank(title) || BaseUtils.is_str_blank(content)) {
			Context context = getApplicationContext();
			Toast.makeText(context, "请填写正确的标题或者内容", Toast.LENGTH_SHORT).show();

			return false;
		}

		return true;
	}

	public void postQuestion(View view) {
		title = edit_text_title.getText().toString();
		content = edit_text_content.getText().toString();

		if (validateQuestionForm(title, content)) {
			//new QuestionsHandler(this).addQuestion(new Question(user_id, title,
			//		content));
			new PostQuestionTask().execute();
		}
	}

	public class PostQuestionTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("question[title]", edit_text_title.getText().toString());
			params.put("question[content]", edit_text_content.getText().toString());
			
			HttpResponse response = HttpPack.sendPost(getApplicationContext(), question_create_url, params);
			
			if (response.getStatusLine().getStatusCode() == 200) {

				JSONObject question = HttpPack.getJsonByResponse(response);
				
				try {
					String question_id = question.getString("question_id");
					return Integer.parseInt(question_id);
 				} catch (JSONException e) {
					e.printStackTrace();
				}
 			}

			return null;

//			new Timer().schedule(new TimerTask() {
//				@Override
//				public void run() {
//
//					QuestionsHandler db = new QuestionsHandler(
//							QuestionNewActivity.this);
//					List<Question> questions = db.getAllQuestions();
//
//					if (HttpPack.hasConnected(QuestionNewActivity.this)) {
//
//						for (Question cn : questions) {
//							Map<String, String> params = new HashMap<String, String>();
//							params.put("question[title]", cn.getTitle());
//							params.put("question[content]", cn.getContent());
//							
//							HttpResponse response = HttpPack.sendPost(getApplicationContext(), question_create_url, params);
//
//							if (response.getStatusLine().getStatusCode() == 200) {
//								Question question = db.getQuestion(cn.getID());
//								db.deleteQuestion(question);
//							}
//						}
//
//					}
//
//					// 输出目前本地数据库所有记录
//					SqliteLog.showAllQuestions(QuestionNewActivity.this);
//				}
//
//			}, 0, 5000);
//			return null;
		}
		
		@Override
	    protected void onPostExecute(final Integer question_id) {
		
			AlertDialog.Builder builder = new AlertDialog.Builder(QuestionNewActivity.this);
			builder.setMessage("发送成功")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									
									Intent intent = new Intent(getApplicationContext(), QuestionShowActivity.class);
									intent.putExtra("question_id", question_id);
									startActivity(intent);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

	        super.onPostExecute(question_id);
	    }
		
		
	}

}
