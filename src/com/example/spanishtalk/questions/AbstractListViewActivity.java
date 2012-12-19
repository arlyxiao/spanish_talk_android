package com.example.spanishtalk.questions;

import java.io.IOException;
import java.util.List;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.datasource.CustomArrayAdapter;
import com.example.datasource.QuestionDataSource;
import com.example.spanishtalk.R;
import com.example.tables.Question;





public abstract class AbstractListViewActivity extends ListActivity
{

	protected QuestionDataSource datasource;

	protected static final int PAGESIZE = 10;

	protected TextView textViewDisplaying;

	protected View footerView;

	protected boolean loading = false;

	protected class LoadNextPage extends AsyncTask<String, Void, String>
	{
		private List<Question> newData = null;

		@Override
		protected String doInBackground(String... arg0)
		{
			try
			{
				Thread.sleep(1500);
				newData = datasource.getData();
			} catch (InterruptedException e) {
				Log.e("AbstractListActivity", e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			CustomArrayAdapter customArrayAdapter = ((CustomArrayAdapter) getListAdapter());
			for (Question qr : newData) {
				customArrayAdapter.add(qr);
			}
			customArrayAdapter.notifyDataSetChanged();

			getListView().removeFooterView(footerView);
  			loading = false;
		}

	}

	protected void updateDisplayingTextView()
	{
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		String text = getString(R.string.display);
		text = String.format(text, getListAdapter().getCount(), datasource.getSize());
		textViewDisplaying.setText(text);
	}

	
	
	protected boolean load(int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && getListView().getChildAt(visibleItemCount -1) != null && getListView().getChildAt(visibleItemCount-1).getBottom() <= getListView().getHeight();
		boolean moreRows = getListAdapter().getCount() < datasource.getSize();
		return moreRows && lastItem && !loading;
		
	}
}