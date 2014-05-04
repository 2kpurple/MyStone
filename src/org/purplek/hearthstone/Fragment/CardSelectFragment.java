package org.purplek.hearthstone.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Activity.CollectionEditActivity;
import org.purplek.hearthstone.adapter.CardAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;
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
public class CardSelectFragment extends Fragment implements OnScrollListener, OnItemClickListener, OnItemLongClickListener {
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		list = new ArrayList<Card>();
		
		Bundle bundle = getArguments();
		if(bundle != null){
			clas = bundle.getInt("class");
		}
		cardAdapter = new CardAdapter(getActivity(), list);
		activity = (CollectionEditActivity) getActivity();
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
		if(activity.count == 30){
			showToast(R.string.cannot_select_more_card);
			return;
		}
		Map<String, Integer> tempMap = cardAdapter.getSelectedMap();
		Card tempCard = list.get(position);
		Integer count = tempMap.get(tempCard.name);
		if(count == null){
			tempMap.put(tempCard.name, 1);
			activity.count++;
			activity.cards.add(tempCard);
		} else {
			if(count == 1){
				if(tempCard.rarity == 4){
					showToast(R.string.cannot_select_more_legend);
				} else {
					tempMap.put(tempCard.name, 2);
					activity.count++;
					activity.cards.add(tempCard);
				}
			} else {
				showToast(R.string.cannot_select_more);
			} 
		}
		cardAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 长按取消选择
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		if(activity.count == 0){
			return true;
		}
		Map<String, Integer> tempMap = cardAdapter.getSelectedMap();
		Card tempCard = list.get(position);
		Integer count = tempMap.get(tempCard.name);
		if(count == null){
			return true;
		} else {
			if(count == 2){
				tempMap.put(tempCard.name, 1);
				activity.count--;
				activity.cards.remove(tempCard);
			}
			if(count == 1){
				tempMap.remove(tempCard.name);
				activity.count--;
				activity.cards.remove(tempCard.id);
			}
		}
		cardAdapter.notifyDataSetChanged();
		return true;
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
	
	private void showToast(int string) {
		Toast.makeText(getActivity(), getString(string), Toast.LENGTH_SHORT)
				.show();
	}
}
