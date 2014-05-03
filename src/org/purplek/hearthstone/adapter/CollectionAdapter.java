package org.purplek.hearthstone.adapter;

import java.util.List;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.model.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CollectionAdapter extends BaseAdapter {
	
	private List<Collection> list;
	private LayoutInflater inflater;
	
	public CollectionAdapter(Context context, List<Collection> list){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.listitem_collection_info, null);
			holder = new ViewHolder();
			holder.nameText = (TextView) convertView.findViewById(R.id.collection_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.nameText.setText(list.get(position).name);
		
		return convertView;
	}
	
	public class ViewHolder{
		TextView nameText;
	}
	
}
