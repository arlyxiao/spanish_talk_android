package com.example.spanishtalk.questions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datasource.CustomArrayAdapter;
import com.example.datasource.QuestionDataSource;
import com.example.logic.BaseAction;
import com.example.logic.BaseUrl;
import com.example.spanishtalk.R;
import com.example.tables.Question;


public class IndexActivity extends AbstractListViewActivity
{

	private int offset = 0;

	private ProgressBar progressBar;

	private Button first;

	private Button last;

	private Button prev;

	private Button next;
	
	public QuestionDataSource datasource;
	
	private String url = BaseUrl.questions;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		BaseAction.checkLogin(getApplicationContext());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions_index);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		first = (Button) findViewById(R.id.buttonfirst);
		prev = (Button) findViewById(R.id.buttonprev);
		next = (Button) findViewById(R.id.buttonnext);
		last = (Button) findViewById(R.id.buttonlast);
		

		setListAdapter(new CustomArrayAdapter(this, R.layout.listview, new ArrayList<Question>()));
		
		(new LoadNextPage()).execute(url + "?page=1");
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
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Integer question_id = ((Question) getListAdapter().getItem(position)).getID();
		
		Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
		intent.putExtra("question_id", question_id);
		startActivity(intent);
	}

	private class LoadNextPage extends AsyncTask<String, Void, String>
	{
		private List<Question> questionList = null;

		@Override
		protected void onPreExecute()
		{
			progressBar.setVisibility(View.VISIBLE);
			loading = true;
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls)
		{
			datasource = new QuestionDataSource(getApplicationContext(), urls[0]);
			try {
				Thread.sleep(1000);
				questionList = datasource.getData();
			} catch (InterruptedException e) {
				Log.e("PagingButtons", e.getMessage());
			} catch (IOException e) {
				Log.d("Data source error", e.getMessage());
				e.printStackTrace();
			}
			
			
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			CustomArrayAdapter customArrayAdapter = ((CustomArrayAdapter) getListAdapter());
			customArrayAdapter.clear();
			for (Question qr : questionList) {
				customArrayAdapter.add(qr);
			}
			customArrayAdapter.notifyDataSetChanged();

			updateDisplayingTextView();

			loading = false;
		}

	}

	@Override
	protected void updateDisplayingTextView()
	{
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		String text = getString(R.string.display);
		text = String.format(text, getListAdapter().getCount(), datasource.getSize());
		textViewDisplaying.setText(text);
		
		updateButtons();
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void updateButtons()
	{
		if (getCurrentPage() > 1)
		{
			first.setEnabled(true);
			prev.setEnabled(true);
		}
		else
		{
			first.setEnabled(false);
			prev.setEnabled(false);
		}
		if (getCurrentPage() < getLastPage())
		{
			next.setEnabled(true);
			last.setEnabled(true);
		}
		else
		{
			next.setEnabled(false);
			last.setEnabled(false);
		}

	}

	private int getLastPage()
	{
		return (int) (Math.ceil((float) datasource.getSize() / PAGESIZE));
	}

	private int getCurrentPage()
	{
		return (int) (Math.ceil((float) (offset + 1) / PAGESIZE));
	}

	
	// 导航按钮
	public void first(View v)
	{
		if (!loading)
		{
			offset = 0;
			(new LoadNextPage()).execute(url + "?page=1");
		}
	}

	public void next(View v)
	{
		if (!loading)
		{
			Integer page = getCurrentPage();
			offset = page * PAGESIZE;
						
			(new LoadNextPage()).execute(url + "?page=" + Integer.toString(page + 1));
		}
	}

	public void previous(View v)
	{
		if (!loading)
		{
			Integer page = getCurrentPage();
			offset = (page - 2) * PAGESIZE;
									
			(new LoadNextPage()).execute(url + "?page=" + Integer.toString(page - 1));
		}
	}

	public void last(View v)
	{
		if (!loading)
		{
			Integer page = getLastPage();
			offset = (page - 1) * PAGESIZE;
			 
			(new LoadNextPage()).execute(url + "?page=" + Integer.toString(page));
		}
	}

}
