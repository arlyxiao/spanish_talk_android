package com.example.spanishtalk.questions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.lib.BaseDialog;
import com.example.lib.HttpPack;
import com.example.spanishtalk.R;
import com.example.tables.Question;


public class IndexActivity extends QuestionListActivity implements OnItemLongClickListener{

	private Integer questionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		questionId = ((Question) getListAdapter().getItem(position)).id;

		Intent intent = new Intent(context, ShowActivity.class);
		intent.putExtra("questionId", questionId);
		startActivity(intent);
	}
	
	
}
