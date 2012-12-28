package com.example.spanishtalk.questions;

import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;

import com.example.datasource.CustomArrayAdapter;
import com.example.datasource.QuestionDataSource;
import com.example.lib.SessionManagement;
import com.example.logic.BaseUrl;
import com.example.logic.CheckLogin;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.R;
import com.example.tables.Question;


public class QuestionListActivity extends ListActivity implements OnItemLongClickListener
{
	private int offset = 0;
	
	protected static final int PAGESIZE = 10;
		
	private Button first;

	private Button last;

	private Button prev;

	private Button next;


	protected View footerView;

	protected boolean loading = false;
	
	private String url = BaseUrl.questions;
	
	private Integer questionId, creatorId;
	
	private CustomArrayAdapter customArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		new CheckLogin(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions_index);
	
		first = (Button) findViewById(R.id.buttonfirst);
		prev = (Button) findViewById(R.id.buttonprev);
		next = (Button) findViewById(R.id.buttonnext);
		last = (Button) findViewById(R.id.buttonlast);
		
		setListAdapter(new CustomArrayAdapter(this, R.layout.listview,
				new ArrayList<Question>()));
		
		
		customArrayAdapter = ((CustomArrayAdapter) getListAdapter());
		customArrayAdapter.clear();
		
		String param_url = getUrlFromOtherActivity();
		if (param_url == null) {
			new FetchQuestions(this, customArrayAdapter, url + "?page=1");
			return;
		}
		new FetchQuestions(this, customArrayAdapter, url + "?page=1");
		
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
		new FetchQuestions(this, customArrayAdapter, url + "?page=1");
	}

	public void clickAllQuestions(View view) {
		url = BaseUrl.questions;
		new FetchQuestions(this, customArrayAdapter, url + "?page=1");
	}

	public void clickAskQuestion(View view) {
		Intent intent = new Intent(getApplicationContext(), NewActivity.class);
		startActivity(intent);
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent,
			View v, final int position, long id) {
		questionId = ((Question) getListAdapter().getItem(
				position)).id;
		creatorId = ((Question) getListAdapter().getItem(
				position)).creator_id;
		
		SessionManagement session = new SessionManagement();
		if (session.getUserId() != creatorId) {
			return true;
		}

		AlertDialog.Builder clearConfirmDialog = new AlertDialog.Builder(
				this);
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
	
	public void updateButtons() {
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

	public void first(View v) {
		if (!loading) {
			offset = 0;
			new FetchQuestions(this, customArrayAdapter, url + "?page=1");
		}
	}

	public void next(View v) {
		if (!loading) {
			Integer page = getCurrentPage();
			offset = page * PAGESIZE;

			String a = url + "?page=" + Integer.toString(page + 1);
			new FetchQuestions(this, customArrayAdapter, a);
		}
	}

	public void previous(View v) {
		if (!loading) {
			Integer page = getCurrentPage();
			offset = (page - 2) * PAGESIZE;
			
			String a = url + "?page=" + Integer.toString(page - 1);
			new FetchQuestions(this, customArrayAdapter, a);
		}
	}

	public void last(View v) {
		if (!loading) {
			Integer page = getLastPage();
			offset = (page - 1) * PAGESIZE;
			
			String a = url + "?page=" + Integer.toString(page);
			new FetchQuestions(this, customArrayAdapter, a);
		}
	}

}