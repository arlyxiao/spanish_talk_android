package com.example.lib;

import java.util.ArrayList;

import com.example.logic.QuestionRows;
import com.example.spanishtalk.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CustomAdapter extends BaseAdapter {
    private static ArrayList<QuestionRows> questionRows;
    private LayoutInflater mInflater;
 
    public CustomAdapter(Context context, ArrayList<QuestionRows> result) {
        questionRows = result;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return questionRows.size();
    }
 
    public Object getItem(int position) {
        return questionRows.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.simple_custom_list, null);
            holder = new ViewHolder();
            holder.simpleTitle = (TextView) convertView.findViewById(R.id.simple_title);
            holder.simpleId = (TextView) convertView.findViewById(R.id.simple_id);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.simpleTitle.setText(questionRows.get(position).getTitle());
        holder.simpleId.setText(questionRows.get(position).getId());
 
        return convertView;
    }
 
    static class ViewHolder {
    	TextView simpleId;
        TextView simpleTitle;
    }

	
}