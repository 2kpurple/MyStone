package org.purplek.hearthstone.Fragment;

import java.util.ArrayList;
import java.util.List;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.adapter.CardAdapter;
import org.purplek.hearthstone.model.Card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 这个fragment用于选择卡牌
 * @author PurpleK
 *
 */
public class CardSelectFragment extends Fragment {
	
	private List<Card> list;
	private CardAdapter cardAdapter;
	private ListView cardListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		list = new ArrayList<Card>();
		cardAdapter = new CardAdapter(getActivity(), list);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		cardListView = (ListView) view.findViewById(R.id.card_list);
		queryData();
		return view;
	}
	
	public void queryData(){
		
	}
	
	
}
