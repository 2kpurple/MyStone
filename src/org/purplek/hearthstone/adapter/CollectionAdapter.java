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
	
	private final static String ORANGE = "橙 ";
	private final static String PURPLE = "紫 ";
	private final static String BLUE = "蓝 ";
	
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
			holder.orangeCount = (TextView) convertView.findViewById(R.id.orange_card_count);
			holder.purpleCount = (TextView) convertView.findViewById(R.id.purple_card_count);
			holder.blueCount = (TextView) convertView.findViewById(R.id.blue_card_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Collection collection = list.get(position);
		
		holder.nameText.setText(collection.name);
		
		if (collection.orange != 0) {
			holder.orangeCount.setVisibility(View.VISIBLE);
			holder.orangeCount.setText(ORANGE + collection.orange);
		} else {
			holder.orangeCount.setVisibility(View.GONE);
		}
		if (collection.purple != 0) {
			holder.purpleCount.setVisibility(View.VISIBLE);
			holder.purpleCount.setText(PURPLE + collection.purple);
		} else {
			holder.purpleCount.setVisibility(View.GONE);
		}
		if (collection.blue != 0) {
			holder.blueCount.setVisibility(View.VISIBLE);
			holder.blueCount.setText(BLUE + collection.blue);
		} else {
			holder.blueCount.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		TextView nameText;
		TextView orangeCount;
		TextView purpleCount;
		TextView blueCount;
	}
	
}
