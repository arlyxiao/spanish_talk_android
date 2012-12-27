package com.example.spanishtalk.questions;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datasource.AnswerBaseAdapter;
import com.example.datasource.CustomArrayAdapter;
import com.example.datasource.QuestionDataSource;
import com.example.lib.BaseDialog;
import com.example.lib.HttpPack;
import com.example.lib.SessionManagement;
import com.example.logic.BaseAction;
import com.example.logic.BaseUrl;
import com.example.logic.CheckLogin;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.LoginActivity;
import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.tables.Question;

public class IndexActivity extends AbstractListViewActivity implements OnItemLongClickListener{

	private int offset = 0;

	private ProgressBar progressBar;

	private Button first;

	private Button last;

	private Button prev;

	private Button next;

	public QuestionDataSource datasource;

	private String url = BaseUrl.questions;

	private Integer questionId, creatorId;
	private ArrayList<Question> questionList;
	private ListView vQuestionList;
	private TextView vListNotice;
	private CustomArrayAdapter customArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions_index);
	
		
		vQuestionList = (ListView) findViewById(android.R.id.list);
		vListNotice = (TextView) findViewById(R.id.list_notice);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		first = (Button) findViewById(R.id.buttonfirst);
		prev = (Button) findViewById(R.id.buttonprev);
		next = (Button) findViewById(R.id.buttonnext);
		last = (Button) findViewById(R.id.buttonlast);
		
		setListAdapter(new CustomArrayAdapter(this, R.layout.listview,
				new ArrayList<Question>()));
		
		
		String param_url = getUrlFromOtherActivity();
		if (param_url == null) {
			(new LoadNextPage()).execute(url + "?page=1");
			return;
		}
		(new LoadNextPage()).execute(param_url + "?page=1");
		
	}

	public String getUrlFromOtherActivity() {
		Intent myIntent = getIntent();
		Bundle b = myIntent.getExtras();
		if (b == null) {
			return null;
		}
		url = b.getString("url");

		return url;
	}

	public void clickMyQuestions(View view) {
		offset = 0;
		url = BaseUrl.questionsMy;
		(new LoadNextPage()).execute(url + "?page=1");
	}

	public void clickAllQuestions(View view) {
		url = BaseUrl.questions;
		(new LoadNextPage()).execute(url + "?page=1");
	}

	public void clickAskQuestion(View view) {
		Intent intent = new Intent(getApplicationContext(), NewActivity.class);
		startActivity(intent);
	}
	
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Context context = getApplicationContext();
		if (!HttpPack.hasConnected()) {
			BaseDialog.showSingleAlert(
					context.getString(R.string.network_error),
					IndexActivity.this);
			return;
		}
		questionId = ((Question) getListAdapter().getItem(position)).getID();

		Intent intent = new Intent(context, ShowActivity.class);
		intent.putExtra("questionId", questionId);
		startActivity(intent);
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent,
			View v, final int position, long id) {
		questionId = ((Question) getListAdapter().getItem(
				position)).getID();
		creatorId = ((Question) getListAdapter().getItem(
				position)).getCreatorId();
		
		SessionManagement session = new SessionManagement();
		if (session.getUserId() != creatorId) {
			return true;
		}

		AlertDialog.Builder clearConfirmDialog = new AlertDialog.Builder(
				IndexActivity.this);
		clearConfirmDialog
				.setMessage(
						getApplicationContext().getString(
								R.string.confirm_delete))
				.setCancelable(false)
				.setPositiveButton(
						getApplicationContext().getString(
								R.string.confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int id) {

							
								
								new SpanishTalkAsyncTask<Void>() {
	                        		@Override
	                    			protected HttpResponse doPost() {
	                    				HttpResponse response = HttpApi.deleteQuestion(questionId);
	                    				return response;
	                    			}
	                    			
	                    			@Override
	                    			protected void onSuccess(HttpResponse response) {
	                    				Question question = ((Question) getListAdapter().getItem(position));
	                    				customArrayAdapter.remove(question);
	                    				customArrayAdapter.notifyDataSetChanged();
	                    			}
	                    			
	                    			protected void showNoticeView() {
	                     			}
	                    			
	                    			protected void hideNoticeView() {
	                     			}
	                        	}.execute();

							}
						})
				.setNegativeButton(
						getApplicationContext().getString(
								R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = clearConfirmDialog.create();
		alert.show();

		return true;
	}



	private class LoadNextPage extends AsyncTask<String, Void, HttpResponse> {
		private List<Question> questionList = null;

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpResponse response = QuestionDataSource.sendRequest(urls[0]);

			Integer statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == 200) {
				try {
    				Thread.sleep(1000);
    				questionList = QuestionDataSource.getQuestions(response);
    				
				} catch (InterruptedException e) {
    				Log.e("PagingButtons", e.getMessage());
    			}
			}

			return response;
		}

		@Override
		protected void onPreExecute() {
			new CheckLogin(IndexActivity.this);
			
			loading = true;
			
			vListNotice.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);

			if (!HttpPack.hasConnected()) {
				BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.network_error));
				cancel(true);
				return;
			}

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			progressBar.setVisibility(View.INVISIBLE);

			BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			Integer statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
            	case 200:
            		customArrayAdapter = ((CustomArrayAdapter) getListAdapter());
        			customArrayAdapter.clear();
        			
        			if (questionList.size() == 0) {
        				vListNotice.setVisibility(View.VISIBLE);
        				vQuestionList.setVisibility(View.GONE);
        				updateDisplayingTextView();
        				return;
        			}
        			
        			vListNotice.setVisibility(View.GONE);
        			vQuestionList.setVisibility(View.VISIBLE);
        			for (Question qr : questionList) {
        				customArrayAdapter.add(qr);
        			}
 
        			customArrayAdapter.notifyDataSetChanged();

        			updateDisplayingTextView();

        			loading = false;
        			
        			vQuestionList.setLongClickable(true);
        			vQuestionList.setOnItemLongClickListener(IndexActivity.this);
        			
            		break;
            	case 401:
            		BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.login_required));
            		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    				startActivity(intent);
    				finish();
    				break;
            	default:
            		BaseAction.showFormNotice(SpanishTalkApplication.context.getString(R.string.server_connection_error));
            		break;
			}
		}	

	}

	@Override
	protected void updateDisplayingTextView() {
		
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		String text = getString(R.string.display);
		text = String.format(text, getListAdapter().getCount(), QuestionDataSource.getTotal());
		textViewDisplaying.setText(text);

		updateButtons();
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void updateButtons() {
		if (getCurrentPage() > 1) {
			first.setEnabled(true);
			prev.setEnabled(true);
		} else {
			first.setEnabled(false);
			prev.setEnabled(false);
		}
		if (getCurrentPage() < getLastPage()) {
			next.setEnabled(true);
			last.setEnabled(true);
		} else {
			next.setEnabled(false);
			last.setEnabled(false);
		}

	}

	private int getLastPage() {
		return (int) (Math.ceil((float) QuestionDataSource.getTotal() / PAGESIZE));
	}

	private int getCurrentPage() {
		return (int) (Math.ceil((float) (offset + 1) / PAGESIZE));
	}

	// 导航按钮
	public void first(View v) {
		if (!loading) {
			offset = 0;
			(new LoadNextPage()).execute(url + "?page=1");
		}
	}

	public void next(View v) {
		if (!loading) {
			Integer page = getCurrentPage();
			offset = page * PAGESIZE;

			(new LoadNextPage()).execute(url + "?page="
					+ Integer.toString(page + 1));
		}
	}

	public void previous(View v) {
		if (!loading) {
			Integer page = getCurrentPage();
			offset = (page - 2) * PAGESIZE;

			(new LoadNextPage()).execute(url + "?page="
					+ Integer.toString(page - 1));
		}
	}

	public void last(View v) {
		if (!loading) {
			Integer page = getLastPage();
			offset = (page - 1) * PAGESIZE;

			(new LoadNextPage()).execute(url + "?page="
					+ Integer.toString(page));
		}
	}


	

}
