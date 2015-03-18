package com.lilo.widget;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListViewAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,Object>> listItems;
	private LayoutInflater listContInflater;
	
	
	
	public ListViewAdapter(Context context,
			List<Map<String, Object>> listItems, LayoutInflater listContInflater) {
		super();
		this.context = context;
		this.listItems = listItems;
		this.listContInflater = listContInflater;
	}

	@Override
	public int getCount() {
		
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
