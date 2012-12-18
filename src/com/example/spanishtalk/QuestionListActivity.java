package com.example.spanishtalk;

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

import com.example.lib.BaseDialog;
import com.example.lib.CustomAdapter;
import com.example.lib.HttpPack;
import com.example.logic.QuestionRows;
import com.example.logic.SpanishTalkBaseActivity;
import com.example.spanishtalk.questions.NewActivity;
import com.example.spanishtalk.questions.ShowActivity;


public class QuestionListActivity extends SpanishTalkBaseActivity {
	private LinearLayout loadingNotice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        
        refreshQuestions(questions_url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question_list, menu);
        return true;
    }
    
    public void refreshQuestions(String url) {
    	if (HttpPack.hasConnected(this)) {
    		loadingNotice = (LinearLayout) findViewById(R.id.loading_view);
    		loadingNotice.setVisibility(View.VISIBLE);
    		
			new GetQuestionsTask().execute(url);
			return;
		}
		BaseDialog.showSingleAlert("", this);
    }
    
    public void refreshQuestions(View view) {
    	refreshQuestions(questions_url);
    }
    
    public void clickAskQuestion(View view) {
    	openActivity(NewActivity.class);
	}
    
    public void clickMyQuestions(View view) {
    	refreshQuestions(my_questions_url);
	}
    
	public ArrayList<QuestionRows> getQuestions(JSONArray questions) {
		ArrayList<QuestionRows> questionList = new ArrayList<QuestionRows>();
		JSONObject json_data;

		try {
			for (int i = 0; i < questions.length(); i++) {
				QuestionRows qr = new QuestionRows();

				json_data = questions.getJSONObject(i);

				qr.setId(json_data.getString("id"));
				qr.setTitle(json_data.getString("title"));
				qr.setCreatedAt(json_data.getString("created_at"));
				// Log.d("title-------", json_data.getString("title"));
				// Log.d("id-------", json_data.getString("id"));

				questionList.add(qr);
			}
			return questionList;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
   
    
    public class GetQuestionsTask extends AsyncTask<String, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(String... urls) {
			HttpResponse response = HttpPack.sendRequest(getApplicationContext(), urls[0]);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpPack.getJsonArrayByResponse(response);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(JSONArray questionsJsonArray) {
    		loadingNotice.setVisibility(View.GONE);
     		
			ArrayList<QuestionRows> questionList = getQuestions(questionsJsonArray);
			final ListView lv = (ListView) findViewById(R.id.questionListView);
		
			lv.setAdapter(new CustomAdapter(getApplicationContext(), questionList));
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Object o = lv.getItemAtPosition(position);
					QuestionRows row = (QuestionRows) o;
					
					//BaseDialog.showSingleAlert(row.getId(),
					//		QuestionListActivity.this);
					
					Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
					intent.putExtra("question_id", Integer.parseInt(row.getId()));
					startActivity(intent);
				}
			});


		}

	}
}
