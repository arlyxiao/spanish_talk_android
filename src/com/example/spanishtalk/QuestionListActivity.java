package com.example.spanishtalk;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lib.BaseDialog;
import com.example.lib.HttpPack;
import com.example.logic.SpanishTalkBaseActivity;

public class QuestionListActivity extends SpanishTalkBaseActivity {
	private ListView list_view_questions;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetQuestionsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question_list, menu);
        return true;
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
		protected void onPostExecute(JSONArray questions) {
			List<String> data = new ArrayList<String>();
			JSONObject json_data;
			
			for (int i = 0; i < questions.length(); i++) {
				try {
					json_data = questions.getJSONObject(i);
					data.add(json_data.getString("title"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			
			list_view_questions = new ListView(QuestionListActivity.this);
			list_view_questions.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        	BaseDialog.showSingleAlert(view.toString(), QuestionListActivity.this);
		        }
		    });


			
	        list_view_questions.setAdapter(new ArrayAdapter<String>(QuestionListActivity.this, android.R.layout.simple_list_item_1, data));
	        
	        setContentView(list_view_questions);
		}

	}
}
