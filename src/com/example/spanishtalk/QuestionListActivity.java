package com.example.spanishtalk;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.lib.BaseDialog;
import com.example.lib.CustomAdapter;
import com.example.lib.HttpPack;
import com.example.logic.QuestionRows;
import com.example.logic.SpanishTalkBaseActivity;

public class QuestionListActivity extends SpanishTalkBaseActivity {	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        new GetQuestionsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question_list, menu);
        return true;
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

				Log.d("title-------", json_data.getString("title"));
				Log.d("id-------", json_data.getString("id"));

				questionList.add(qr);
			}
			return questionList;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
   
    
    public class GetQuestionsTask extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			HttpResponse response = HttpPack.sendRequest(getApplicationContext(), questions_url);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpPack.getJsonArrayByResponse(response);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(JSONArray questionsJsonArray) {
			ArrayList<QuestionRows> questionList = getQuestions(questionsJsonArray);
			final ListView lv = (ListView) findViewById(R.id.questionListView);
		
			lv.setAdapter(new CustomAdapter(getApplicationContext(), questionList));
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Object o = lv.getItemAtPosition(position);
					QuestionRows fullObject = (QuestionRows) o;
					
					//BaseDialog.showSingleAlert(fullObject.getId(),
					//		QuestionListActivity.this);
					
					Intent intent = new Intent(getApplicationContext(), QuestionShowActivity.class);
					intent.putExtra("question_id", Integer.parseInt(fullObject.getId()));
					startActivity(intent);
				}
			});


		}

	}
}
