package com.example.datasource;

import java.util.List;

import com.example.spanishtalk.R;
import com.example.spanishtalk.SpanishTalkApplication;
import com.example.tables.Question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomArrayAdapter extends ArrayAdapter<Question>
{
	
	public CustomArrayAdapter(Context context, int textViewResourceId, List<Question> objects)
	{
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// holder pattern
		Holder holder = null;
		if (convertView == null)
		{
			holder = new Holder();

			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.listview, null);
			holder.setID((TextView) convertView.findViewById(R.id.lvQuestionID));
			holder.setTitle((TextView) convertView.findViewById(R.id.lv_question_title));
			holder.setCreatedAt((TextView) convertView.findViewById(R.id.lv_question_created_at));
			holder.setUsername((TextView) convertView.findViewById(R.id.lv_question_username));
			holder.setAnswerCount((TextView) convertView.findViewById(R.id.lv_answer_count));
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}

		
		holder.getID().setText(Integer.toString(getItem(position).id));
		holder.getTitle().setText(getItem(position).title);
		holder.getUsername().setText(getItem(position).creator.username);
		holder.getCreatedAt().setText(getItem(position).created_at.substring(0, 10));
		
		String answersCount = Integer.toString(getItem(position).answers.size());
		holder.getAnswerCount().setText(answersCount +
				SpanishTalkApplication.context.getString(R.string.answers_for_count));
		
		
		return convertView;
	}
	
	

}