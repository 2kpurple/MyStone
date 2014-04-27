package org.purplek.hearthstone;

import java.util.ArrayList;
import java.util.List;

import org.purplek.hearthstone.Fragment.DisplayFragment;
import org.purplek.hearthstone.Fragment.SettingsFragment;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;
import org.purplek.heartstone.utils.SharePrefUtil;

import android.content.Context;
import android.os.Handler;
import android.provider.SyncStateContract.Helpers;

public class CardListManager {
	private static CardListManager manager;
	private List<Card> cards;
	private List<Card> searchCards;
	private List<Card> queryCards;
	private boolean searchMode;
	
    /*查询卡牌库参数*/
    public int clas = -1;			//职业
    public int rarity = -1;			//稀有度
    public int cost = -1;			//费用
    public int race = -1;			//仆从种族
    public int type = -1;			//卡牌类型
    public String power = null;		//能力
    public int page = 0;				//页数
    public boolean haveDataToUpdate;	//是否还有数据进行更新
	
	public CardListManager(){
		cards = new ArrayList<Card>();
		queryCards = cards;
		searchMode = false;
	}
	
	public boolean isSearchMode() {
		return searchMode;
	}

	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

	public static CardListManager getInstance(){
		if(manager == null){
			manager = new CardListManager();
		}
		return manager;
	}
	
	public List<Card> getList(){
		return cards;
	}
	
	public void setListToQuery(){
		cards = queryCards;
	}
	
	public void setListToSearch(){
		if(searchCards == null){
			searchCards = new ArrayList<Card>();
		}
		cards = searchCards;
	}
	
	/**
	 * 查询数据函数
	 * @param context 
	 * @return 返回4种情况 0为新查询且已经查询完毕 1为新查询但未查询完毕 2为旧查询且查询完毕 3为旧查询且未查询完毕
	 */
	public boolean queryData(Context context){
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		if(page == 0){
			haveDataToUpdate = true;
			// 清除list中原本的数据
			cards.clear();
		}
		if (haveDataToUpdate) {
			boolean collectable = SharePrefUtil.getData(context,
					SettingsFragment.PRE_COLLECTABLE, false);
			List<Card> tempList = helper.queryCardInfo(rarity, cost,
					race, clas, type, power, page, null, collectable);
			if(tempList.size() < 20){		//返回的list的大小小于20，即数据已经被查询完毕
				haveDataToUpdate = false;
			}
			cards.addAll(tempList);
		}
		return haveDataToUpdate;
	}
	
	//搜索数据函数
	public void searchData(Context context, String query) {
		DatabaseHelper helper = DatabaseHelper.getInstance(context);
		setListToSearch();
		cards.clear();
		boolean collectable = SharePrefUtil.getData(context,
				SettingsFragment.PRE_COLLECTABLE, false);
		List<Card> tempList = helper.searchCardInfo(query, collectable);
		cards.addAll(tempList);
	}
}
