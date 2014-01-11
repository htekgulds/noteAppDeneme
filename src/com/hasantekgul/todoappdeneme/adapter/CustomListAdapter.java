package com.hasantekgul.todoappdeneme.adapter;

import java.util.List;

import com.hasantekgul.todoappdeneme.R;
import com.hasantekgul.todoappdeneme.model.CustomList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	
	private final Context context;
	private final List<CustomList> rows;
	

	public CustomListAdapter(Context context, List<CustomList> rows) {
		this.context = context;
		this.rows = rows;
	}


	@Override
	public int getCount() {
		return rows.size();
	}


	@Override
	public Object getItem(int position) {
		return rows.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.custom_list_row, null);
		}
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView text = (TextView) convertView.findViewById(R.id.text);
		
		title.setText(rows.get(position).getTitle());
		text.setText(rows.get(position).getText());
		
		return convertView;
	}
	
	

}
