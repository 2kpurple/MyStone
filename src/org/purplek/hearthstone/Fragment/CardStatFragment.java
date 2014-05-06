package org.purplek.hearthstone.Fragment;

import java.util.List;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Activity.CollectionEditActivity;
import org.purplek.hearthstone.adapter.CardSelectedAdapter;
import org.purplek.hearthstone.model.Card;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = (CollectionEditActivity) getActivity();
		list = activity.cards;
		adapter = new CardSelectedAdapter(activity, list);
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
		
	}

}
