package com.example.spanishtalk.questions;

import org.apache.http.HttpResponse;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.datasource.QuestionDataSource;
import com.example.lib.HttpPack;
import com.example.logic.BaseUrl;
import com.example.logic.HttpApi;
import com.example.logic.SpanishTalkAsyncTask;
import com.example.spanishtalk.LoginActivity;



public class AbstractListViewActivity extends ListActivity
{

	protected QuestionDataSource datasource;

	protected static final int PAGESIZE = 10;

	protected TextView textViewDisplaying;

	protected View footerView;

	protected boolean loading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	
	protected class LoadNextPage extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{			
			
		}

	}

	protected void updateDisplayingTextView()
	{
	}

	
	
	protected boolean load(int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		return false;
		
	}


}