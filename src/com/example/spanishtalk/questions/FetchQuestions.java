package com.example.spanishtalk.questions;

import java.util.List;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datasource.CustomArrayAdapter;
import com.example.datasource.QuestionDataSource;
import com.example.logic.CheckLogin;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.R;
import com.example.tables.Question;


public class FetchQuestions {
	public static List<Question> questionList;
	
	private ListView vQuestionList;
	private TextView vListNotice, textViewDisplaying;
	private ProgressBar progressBar;
	
	private CustomArrayAdapter currentAdapter;
	private QuestionListActivity currentActivity;
		
	public FetchQuestions(QuestionListActivity t, CustomArrayAdapter customAdapter, final String url) {
		currentActivity = t;
		currentAdapter = customAdapter;
		
		vQuestionList = (ListView) t.findViewById(android.R.id.list);
		vListNotice = (TextView) t.findViewById(R.id.list_notice);
		progressBar = (ProgressBar) t.findViewById(R.id.progressBar1);
		textViewDisplaying = (TextView) t.findViewById(R.id.displaying);
		
		new SpanishTalkAsyncTask<String>(progressBar) {
			@Override
			protected HttpResponse doPost() {
				HttpResponse response = HttpApi.getQuestions(url);
				return response;
			}
			
			@Override
			protected void onSuccess(HttpResponse response) {
				new GetTask().execute(response);
			}
			
			
			@Override
			protected void showNoticeView() {
				new CheckLogin(currentActivity);
								
				vListNotice.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
			}
					
		}.execute();
	}
	
	protected void updateDisplayingTextView() {
		
		textViewDisplaying = (TextView) currentActivity.findViewById(R.id.displaying);
		String text = currentActivity.getString(R.string.display);
		text = String.format(text, currentActivity.getListAdapter().getCount(), QuestionDataSource.getTotal());
		textViewDisplaying.setText(text);

		currentActivity.updateButtons();
		progressBar.setVisibility(View.INVISIBLE);
	}
	
	
	public class GetTask extends AsyncTask<HttpResponse, Void, Void> {
		@Override
		protected Void doInBackground(HttpResponse... responses) {
			questionList = QuestionDataSource.getQuestions(responses[0]);
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			currentAdapter.clear();
			
			if (questionList.size() == 0) {
				vListNotice.setVisibility(View.VISIBLE);
				vQuestionList.setVisibility(View.GONE);
				updateDisplayingTextView();
				return;
			}
			
			vListNotice.setVisibility(View.GONE);
			vQuestionList.setVisibility(View.VISIBLE);
			for (Question qr : questionList) {
				currentAdapter.add(qr);
			}

			currentAdapter.notifyDataSetChanged();

			updateDisplayingTextView();
			
			vQuestionList.setLongClickable(true);
			vQuestionList.setOnItemLongClickListener(currentActivity);
		}
		
	}
}