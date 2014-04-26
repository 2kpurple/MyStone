package org.purplek.hearthstone.Fragment;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Activity.CardDetialActivity;
import org.purplek.hearthstone.Activity.MainActivity;
import org.purplek.hearthstone.adapter.CardAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;
import org.purplek.heartstone.utils.SharePrefUtil;

import android.support.v4.app.Fragment;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.List;

/**
 * Created by purplekfung on 14-1-2.¸
 * 用于显示卡牌列表
 */

public class DisplayFragment extends Fragment implements OnScrollListener, OnItemClickListener {

    private ListView cardListView;
    private CardAdapter cardAdapter;
    private View footerView;
    private int lastItem;
    
    public static final int ADD_DATA = 101;
    public static final int NEW_QUERY = 102;
    public static final int QUERY_FINISH = 103;
    public static final int SEARCH_FINISH = 104;
    
    /*查询卡牌库参数*/
    public int clas = -1;			//职业
    public int rarity = -1;			//稀有度
    public int cost = -1;			//费用
    public int race = -1;			//仆从种族
    public int type = -1;			//卡牌类型
    public String power = null;		//能力
    public int page = 0;				//页数
    public boolean haveDataToUpdate;	//是否还有数据进行更新
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardAdapter = new CardAdapter(getActivity(), CardListManager.getInstance().getList());
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
        cardListView = (ListView) rootView.findViewById(R.id.card_list);
        initFootView();
		queryData();
        cardListView.setAdapter(cardAdapter);
        cardListView.setOnScrollListener(this);
        cardListView.setOnItemClickListener(this);
		return rootView;
	}
    
    
    //查询数据函数
	public void queryData() {
//		if(page == 0){
//			haveDataToUpdate = true;
//			// 清除list中原本的数据
//			CardListManager.getInstance().getList().clear();
//			handler.sendEmptyMessage(NEW_QUERY);
//		}
//		if (haveDataToUpdate) {
//			boolean collectable = SharePrefUtil.getData(getActivity(),
//					SettingsFragment.PRE_COLLECTABLE, false);
//			List<Card> tempList = databaseHelper.queryCardInfo(rarity, cost,
//					race, clas, type, power, page, collectable);
//			if(tempList.size() < 20){		//返回的list的大小小于20，即数据已经被查询完毕
//				haveDataToUpdate = false;
//				handler.sendEmptyMessage(QUERY_FINISH);
//			}
//			cardAdapter.addDataToList(tempList);
//		}
		if(CardListManager.getInstance().page == 0){
			handler.sendEmptyMessage(NEW_QUERY);
		}
		boolean flag = CardListManager.getInstance().queryData(getActivity());
		
		// 当还有数据要更新的时候 flag为true 反之为false
		if(!flag){
			handler.sendEmptyMessage(QUERY_FINISH);
		}
	}
	
    //搜索数据函数
	public void searchData(String query) {
//		CardListManager.getInstance().setListToSearch();
//		CardListManager.getInstance().getList().clear();
//		handler.sendEmptyMessage(NEW_QUERY);
//		boolean collectable = SharePrefUtil.getData(getActivity(),
//				SettingsFragment.PRE_COLLECTABLE, false);
//		List<Card> tempList = databaseHelper.searchCardInfo(query, collectable);
//		handler.sendEmptyMessage(QUERY_FINISH);
//		cardAdapter.addDataToList(tempList);
		CardListManager.getInstance().searchData(getActivity(), query);
		handler.sendEmptyMessage(SEARCH_FINISH);
	}
	
	private void initFootView(){
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = inflater.inflate(R.layout.list_footview, null);
		cardListView.addFooterView(footerView);
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ADD_DATA:
				cardAdapter.notifyDataSetChanged();
				break;
			case NEW_QUERY:
				cardListView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						cardListView.setSelection(0);
					}
				});
				footerView.setVisibility(View.GONE);
				break;
			case QUERY_FINISH:
				footerView.setVisibility(View.VISIBLE);
				break;
			case SEARCH_FINISH:
				cardListView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						cardListView.setSelection(0);
					}
				});
				footerView.setVisibility(View.VISIBLE);
				break;
			}
			
		}
		
	};
	
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
				&& lastItem == CardListManager.getInstance().getList().size() + 1){
			//开启新线程进行查询
			if(CardListManager.getInstance().isSearchMode()){
				return;
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					CardListManager.getInstance().page++;
					queryData();
					handler.sendEmptyMessage(ADD_DATA);
				}
			}).start();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), CardDetialActivity.class);
		intent.putExtra("num", position);
		getActivity().startActivityForResult(intent, MainActivity.REQ_DETIAL);
	}

	public CardAdapter getCardAdapter() {
		return cardAdapter;
	}
}
