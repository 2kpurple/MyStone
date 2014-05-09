package org.purplek.hearthstone.Fragment;

import java.util.List;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Activity.CollectionEditActivity;
import org.purplek.hearthstone.adapter.CardSelectedAdapter;
import org.purplek.hearthstone.model.Card;
import org.purplek.hearthstone.support.BarView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CardStatFragment extends Fragment implements OnItemClickListener {
	
	private List<Card> list;
	private ListView listView;
	private CardSelectedAdapter adapter;
	private CollectionEditActivity activity;
	private CardStatReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (CollectionEditActivity) getActivity();
		list = activity.cards;
		adapter = new CardSelectedAdapter(activity, list);
		receiver = new CardStatReceiver();
		IntentFilter filter = new IntentFilter(getActivity().getString(R.string.action_update_card_stat));
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card_stat, container, false);
		listView = (ListView) view.findViewById(R.id.selected_listview);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 点击在listview中删除
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getString(R.string.action_update_card_select));
		intent.putExtra("name", list.get(position).name);
		getActivity().sendBroadcast(intent);
		list.remove(position);
		adapter.notifyDataSetChanged();
		activity.setActivityTitle();
	}
	
	public CardSelectedAdapter getAdapter(){
		return this.adapter;
	}
	
	private class CardStatReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(getString(R.string.action_update_card_stat))){
				adapter.notifyDataSetChanged();
			}
		}
		
	}

}
