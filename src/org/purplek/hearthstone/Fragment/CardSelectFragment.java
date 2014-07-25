package org.purplek.hearthstone.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Activity.CollectionEditActivity;
import org.purplek.hearthstone.adapter.CardAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;
import org.purplek.hearthstone.utils.PhoneUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * 这个fragment用于选择卡牌
 * @author PurpleK
 *
 */
public class CardSelectFragment extends Fragment implements OnScrollListener,
		OnItemClickListener, OnItemLongClickListener {

	private List<Card> list;
	private CardAdapter cardAdapter;
	private ListView cardListView;
	private int page = 0;
	private int clas = 0;
	private boolean haveDataToUpdate = true;
	private int lastItem = 0;
	
	private final static int ADD_DATA = 101;
	private final static int QUERY_FINISH = 102;
	
	private CollectionEditActivity activity;
	private CardSelectReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		list = new ArrayList<Card>();
		
		Bundle bundle = getArguments();
		if(bundle != null){
			clas = bundle.getInt("class");
		}
		
		
		activity = (CollectionEditActivity) getActivity();
		HashMap<String, Integer> map = activity.selectedMap;
		if(map != null){
			cardAdapter = new CardAdapter(activity, list, map);
		} else {
			cardAdapter = new CardAdapter(activity, list);
		}
		
		receiver = new CardSelectReceiver();
		IntentFilter filter = new IntentFilter(getString(R.string.action_update_card_select));
		getActivity().registerReceiver(receiver, filter);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		cardListView = (ListView) view.findViewById(R.id.card_list);
		queryData();
		cardListView.setAdapter(cardAdapter);
		cardListView.setOnScrollListener(this);
		cardListView.setOnItemClickListener(this);
		cardListView.setOnItemLongClickListener(this);
		return view;
	}
	
	public void queryData(){
		final DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
		if(haveDataToUpdate){
			ArrayList<Card> tempList = helper.queryCardInfoForCollection(clas, page, DatabaseHelper.COLUMN_COST);
			if(tempList.size() < 19){
				handler.sendEmptyMessage(QUERY_FINISH);
			}
			list.addAll(tempList);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastItem > list.size() - 5){
			//开启新线程进行查询
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					page++;
					queryData();
					handler.sendEmptyMessage(ADD_DATA);
				}
			}).start();
		}
	}
	
	/**
	 * 点击选择卡牌
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(activity.cards.size() == 30){
			PhoneUtil.showToast(getActivity(), R.string.cannot_select_more_card);
			return;
		}
		addCardToList(position);
		Intent intent = new Intent(getString(R.string.action_update_card_stat));
		activity.sendBroadcast(intent);
		activity.setActivityTitle();
	}
	
	/**
	 * 长按取消选择
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		removeCardFromList(position);
		Intent intent = new Intent(getString(R.string.action_update_card_stat));
		activity.sendBroadcast(intent);
		activity.setActivityTitle();
		return true;
	}
	
	public void addCardToList(int position){
		Map<String, Integer> tempMap = cardAdapter.getSelectedMap();
		Card tempCard = list.get(position);
		Integer count = tempMap.get(tempCard.name);
		if(count == null){
			tempMap.put(tempCard.name, 1);
			activity.cards.add(tempCard);
		} else {
			if(count == 1){
				if(tempCard.rarity == 4){
					PhoneUtil.showToast(getActivity(), R.string.cannot_select_more_legend);
				} else {
					tempMap.put(tempCard.name, 2);
					activity.cards.add(tempCard);
				}
			} else {
				PhoneUtil.showToast(getActivity(), R.string.cannot_select_more);
			} 
		}
		cardAdapter.notifyDataSetChanged();
	}
	
	public void removeCardFromList(int position){
		if(activity.cards.size() == 0){
			return ;
		}
		Map<String, Integer> tempMap = cardAdapter.getSelectedMap();
		Card tempCard = list.get(position);
		Integer count = tempMap.get(tempCard.name);
		if(count == null){
			return;
		} else {
			if(count == 2){
				tempMap.put(tempCard.name, 1);
				activity.cards.remove(tempCard);
			}
			if(count == 1){
				tempMap.remove(tempCard.name);
				activity.cards.remove(tempCard);
			}
		}
		cardAdapter.notifyDataSetChanged();
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ADD_DATA:
				cardAdapter.notifyDataSetChanged();
				break;
			case QUERY_FINISH:
				haveDataToUpdate = false;
				break;
			}
		}
		
	};
	
	private class CardSelectReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(getString(R.string.action_update_card_select))){
				String name = intent.getStringExtra("name");
				HashMap<String, Integer> temp = cardAdapter.getSelectedMap();
				Integer count = temp.get(name);
				if(count != null){
					if(count == 2){
						temp.put(name, 1);
					} else {
						temp.remove(name);
					}
					cardAdapter.notifyDataSetChanged();
				}
			}
		}
	}
}
