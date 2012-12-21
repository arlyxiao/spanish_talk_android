package com.example.datasource;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.spanishtalk.R;
import com.example.tables.Answer;



public class AnswerBaseAdapter extends BaseAdapter {
    private static ArrayList<Answer> answerList;
 
    private LayoutInflater mInflater;
 
    public AnswerBaseAdapter(Context context, ArrayList<Answer> results) {
        answerList = results;
        
        Log.d(" -------- Answer Header start --------", "----");
		for (Answer h : answerList) {
			Log.d(h.getContent(), "sssssss");
		}
		Log.d(" -------- Answer Header end --------", "----");
		
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return answerList.size();
    }
 
    public Object getItem(int position) {
        return answerList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.answerId = (TextView) convertView.findViewById(R.id.answer_id);
            holder.content = (TextView) convertView.findViewById(R.id.answer_content);
            holder.username = (TextView) convertView.findViewById(R.id.answer_username);
            holder.createdAt = (TextView) convertView.findViewById(R.id.answer_created_at);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        String answerId = Integer.toString(answerList.get(position).getID());
        holder.answerId.setText(answerId);
        holder.content.setText(answerList.get(position).getContent());
        holder.username.setText(answerList.get(position).getUsername());
        holder.createdAt.setText(answerList.get(position).getCreatedAt());
        
 
        return convertView;
    }
 
    static class ViewHolder {
    	TextView answerId;
        TextView content;
        TextView username;
        TextView createdAt;
    }
}