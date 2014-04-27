package org.purplek.hearthstone.adapter;

import java.util.List;

import org.purplek.hearthstone.model.Collection;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CollectionAdapter extends BaseAdapter {
	
	private Context context;
	private List<Collection> list;
	
	public CollectionAdapter(Context context, List<Collection> list){
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		return convertView;
	}

	
}
