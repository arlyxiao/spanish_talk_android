package com.example.spanishtalk.questions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

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
import com.example.spanishtalk.R;
import com.example.spanishtalk.RegisterActivity;
import com.example.spanishtalk.questions.ShowActivity.DeleteAnswerTask;
import com.example.tables.Answer;
import com.example.tables.Question;

public class IndexActivity extends AbstractListViewActivity {
	private Context context;

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
	private CustomArrayAdapter customArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions_index);

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
		if (!HttpPack.hasConnected(this)) {
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

	// 删除回答
	public class DeleteQuestionTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... questions) {
			Integer questionId = questions[0];
			Integer position = questions[1];
			String url = BaseUrl.questionDelete + "/"
					+ Integer.toString(questionId) + ".json";
			HttpResponse response = HttpPack.sendDelete(
					getApplicationContext(), url);

			if (response == null) {
				cancel(true);
				return null;
			}

			Integer statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return position;
			}

			cancel(true);
			return null;
		}

		@Override
		protected void onPreExecute() {
			if (!HttpPack.hasConnected(IndexActivity.this)) {
				Context context = getApplicationContext();
				BaseAction.showFormNotice(context,
						context.getString(R.string.network_error));
				cancel(true);
				return;
			}

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			Context context = getApplicationContext();
			BaseAction.showFormNotice(context,
					context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(Integer position) {
			Question question = ((Question) getListAdapter().getItem(position));
			customArrayAdapter.remove(question);
			customArrayAdapter.notifyDataSetChanged();
			
//			Log.d("start list", "-----");
//			for (Question q: questionList) {
//				Log.d("list aaa ---", q.getContent());
//			}
//			Log.d("end list", "-----");
//			
//			questionList.remove((int)position);
//			
//			Log.d("start222 list", "-----");
//			for (Question q: questionList) {
//				Log.d("list aaa ---", q.getContent());
//			}
//			Log.d("end 2222list", "-----");
//			
//			
//			adapter.notifyDataSetChanged();
			super.onPostExecute(position);
		}
	}

	private class LoadNextPage extends AsyncTask<String, Void, String> {
		private List<Question> questionList = null;

		@Override
		protected String doInBackground(String... urls) {
			datasource = new QuestionDataSource(getApplicationContext(),
					urls[0]);

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
		protected void onPreExecute() {
			loading = true;

			context = getApplicationContext();
			progressBar.setVisibility(View.VISIBLE);

			if (!HttpPack.hasConnected(IndexActivity.this)) {
				BaseAction.showFormNotice(context,
						context.getString(R.string.network_error));
				cancel(true);
				return;
			}

			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			context = getApplicationContext();
			progressBar.setVisibility(View.INVISIBLE);

			BaseAction.showFormNotice(context,
					context.getString(R.string.server_connection_error));
		}

		@Override
		protected void onPostExecute(String result) {
			customArrayAdapter = ((CustomArrayAdapter) getListAdapter());
			customArrayAdapter.clear();

			if (questionList.size() == 0) {
				// BaseAction.showFormNotice(context,
				// context.getString(R.string.server_connection_error));
				updateDisplayingTextView();
				return;
			} else {
				for (Question qr : questionList) {
					customArrayAdapter.add(qr);
				}
			}

			customArrayAdapter.notifyDataSetChanged();

			updateDisplayingTextView();

			loading = false;
			
			
			
			
			IndexActivity.this.getListView().setLongClickable(true);
			IndexActivity.this.getListView().setOnItemLongClickListener(
					new OnItemLongClickListener() {
						public boolean onItemLongClick(AdapterView<?> parent,
								View v, final int position, long id) {
							questionId = ((Question) getListAdapter().getItem(
									position)).getID();
							creatorId = ((Question) getListAdapter().getItem(
									position)).getCreatorId();
							
							SessionManagement session = new SessionManagement(
									getApplicationContext());
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

													new DeleteQuestionTask()
															.execute(questionId, position);

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
					});
		}

	}

	@Override
	protected void updateDisplayingTextView() {
		textViewDisplaying = (TextView) findViewById(R.id.displaying);
		String text = getString(R.string.display);
		text = String.format(text, getListAdapter().getCount(),
				datasource.getSize());
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
		return (int) (Math.ceil((float) datasource.getSize() / PAGESIZE));
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
