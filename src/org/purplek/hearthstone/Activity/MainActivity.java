package org.purplek.hearthstone.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Fragment.DisplayFragment;
import org.purplek.hearthstone.adapter.FilterAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity implements
		SearchView.OnQueryTextListener {
	
	public final static int REQ_SETTINGS = 101;
	public final static int REQ_DETIAL = 102;
	
	// 保存按下back时的时间
	private long exitTime = 0;	

	// 控件
	private DisplayFragment displayFragment;
	private DrawerLayout drawerLayout;
	private ExpandableListView filterListView;
	private RelativeLayout filterListContent;
	private SearchView searchView;
	
	private FilterAdapter filterAdapter;
	private ActionBarDrawerToggle drawerToggle;
	private List<String> filterList;
	private List<List<String>> filterChildList;

	private String[] powerEnStrings;
	
	private CardListManager listManager;
	
	// 筛选器的字符数组
	private String[] filterStrings;
	private String[] typeStrings;
	private String[] classStrings;
	private String[] rarityStrings;
	private String[] raceStrings;
	private String[] costStrings;
	private String[] powerStrings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		displayFragment = new DisplayFragment();

		powerEnStrings = getResources().getStringArray(R.array.power_en_array);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, displayFragment).commit();
		}
		
		setTitle(getString(R.string.card_list));

		findViews();
		initToggle();
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		// drawerLayout.openDrawer(filterListContent);
		filterAdapter = initFilterData(MainActivity.this);
		filterListView.setAdapter(filterAdapter);

		// 初始化单例manager
		listManager = CardListManager.getInstance();
	}

	private void findViews() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		filterListView = (ExpandableListView) findViewById(R.id.filter_list);
		filterListContent = (RelativeLayout) findViewById(R.id.filter_list_content);
		filterListView.setOnChildClickListener(onChildClickListener);
		filterListView.setOnGroupClickListener(onGroupClickListener);
		filterListView.setGroupIndicator(null);
	}

	private void initToggle() {
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				setTitle(getString(R.string.card_list));
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
				setTitle(getString(R.string.app_name));
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
	}

	private FilterAdapter initFilterData(Context context) {
		filterStrings = getResources().getStringArray(
				R.array.filters_array);
		typeStrings = getResources()
				.getStringArray(R.array.type_array);
		classStrings = getResources().getStringArray(
				R.array.class_array);
		rarityStrings = getResources().getStringArray(
				R.array.rarity_array);
		raceStrings = getResources()
				.getStringArray(R.array.race_array);
		costStrings = getResources()
				.getStringArray(R.array.cost_array);
		powerStrings = getResources().getStringArray(
				R.array.power_array);

		filterList = Arrays.asList(filterStrings);
		List<String> typeList = Arrays.asList(typeStrings);
		List<String> classList = Arrays.asList(classStrings);
		List<String> rarityList = Arrays.asList(rarityStrings);
		List<String> raceList = Arrays.asList(raceStrings);
		List<String> costList = Arrays.asList(costStrings);
		List<String> powerList = Arrays.asList(powerStrings);

		filterChildList = new ArrayList<List<String>>();
		filterChildList.add(classList);
		filterChildList.add(typeList);
		filterChildList.add(raceList);
		filterChildList.add(rarityList);
		filterChildList.add(costList);
		filterChildList.add(powerList);

		return new FilterAdapter(context, filterList, filterChildList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
		
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(System.currentTimeMillis() - exitTime > 1000){
			Toast.makeText(this, getString(R.string.back_press_again_to_exit),
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
			return;
		}
		if(searchView.isActivated()){
			System.out.println("aaaaa");
		}
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, REQ_SETTINGS);
			return true;
		}
		if (id == R.id.action_collection) {
			Intent intent = new Intent(this, ClassSelectActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean drawerOpen = drawerLayout.isDrawerOpen(filterListContent);
		menu.findItem(R.id.action_search).collapseActionView();
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private ExpandableListView.OnChildClickListener onChildClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			
			/*
			 * groupPosition 0,1,2,3,4,5,6 分别表示全部、职业、卡牌类型、种族、稀有度、费用、特性
			 */

			/* 先判断groupPosition */
			switch (groupPosition) {
			case 1:
				if (listManager.clas == childPosition - 1) {
					drawerLayout.closeDrawers();
					filterListView.collapseGroup(groupPosition);
					return false;
				}
				listManager.clas = childPosition - 1;
				if(childPosition == 0){
					filterList.set(groupPosition, getString(R.string._class));
					break;
				}
				filterList.set(groupPosition, getString(R.string._class) + "  " + 
						filterChildList.get(groupPosition - 1).get(childPosition));
				break;
			case 2:
				if (listManager.type == childPosition - 1) {
					drawerLayout.closeDrawers();
					filterListView.collapseGroup(groupPosition);
					return false;
				}
				listManager.type = childPosition - 1;
				if(childPosition == 0){
					filterList.set(groupPosition, getString(R.string.type));
					break;
				}
				filterList.set(groupPosition, getString(R.string.type) + "  " + 
						filterChildList.get(groupPosition - 1).get(childPosition));
				break;
			case 3:
				if (listManager.race == childPosition) {
					drawerLayout.closeDrawers();
					filterListView.collapseGroup(groupPosition);
					return false;
				}
				listManager.race = childPosition;
				if(childPosition == 0){
					filterList.set(groupPosition, getString(R.string.race));
					break;
				}
				filterList.set(groupPosition, getString(R.string.race) + "  " + 
						filterChildList.get(groupPosition - 1).get(childPosition));
				break;
			case 4:
				if (listManager.rarity == childPosition - 1) {
					drawerLayout.closeDrawers();
					filterListView.collapseGroup(groupPosition);
					return false;
				}
				listManager.rarity = childPosition - 1;
				if(childPosition == 0){
					filterList.set(groupPosition, getString(R.string.rarity));
					break;
				}
				filterList.set(groupPosition, getString(R.string.rarity) + "  " + 
						filterChildList.get(groupPosition - 1).get(childPosition));
				break;
			case 5:
				if (listManager.cost == childPosition - 1) {
					drawerLayout.closeDrawers();
					filterListView.collapseGroup(groupPosition);
					return false;
				}
				listManager.cost = childPosition - 1;
				if(childPosition == 0){
					filterList.set(groupPosition, getString(R.string.cost));
					break;
				}
				filterList.set(groupPosition, getString(R.string.cost) + "  " + 
						filterChildList.get(groupPosition - 1).get(childPosition));
				break;
			case 6:
				if (childPosition != 0) {
					if (listManager.power != null) {
						if (listManager.power
								.equals(powerEnStrings[childPosition - 1])) {
							drawerLayout.closeDrawers();
							filterListView.collapseGroup(groupPosition);
							return false;
						}
					}
					listManager.power = powerEnStrings[childPosition - 1];
					filterList.set(groupPosition, getString(R.string.power) + "  " + 
							filterChildList.get(groupPosition - 1).get(childPosition));
				} else {
					filterList.set(groupPosition, getString(R.string.power));
					System.out.println("我点下了" + groupPosition);
					listManager.power = null;
				}
				break;
			}
			listManager.page = 0;
			
			filterListView.collapseGroup(groupPosition);
			drawerLayout.closeDrawer(filterListContent);
			
			filterAdapter.notifyDataSetChanged();

			/* 这里开始查询 */
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					displayFragment.queryData();
					displayFragment.handler
							.sendEmptyMessage(DisplayFragment.ADD_DATA);
				}
			}).start();
			return false;
		}
	};

	private ExpandableListView.OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			// TODO Auto-generated method stub
			if (groupPosition == 0) {
				// 判断现在是否在“全部”标签下
				if (isTableAll()) {
					drawerLayout.closeDrawer(filterListContent);
					return false;
				}
				resetFilter();
			}
			return false;
		}
	};
	
	/**
	 * 判断现在是否在“全部”标签下
	 * @return
	 */
	public boolean isTableAll() {
		return listManager.clas == -1 && listManager.rarity == -1
				&& listManager.race == -1 && listManager.type == -1
				&& listManager.cost == -1 && listManager.power == null;
	}
	
	/**
	 * 重置筛选器
	 */
	private void resetFilter(){
		listManager.clas = -1;
		listManager.cost = -1;
		listManager.rarity = -1;
		listManager.race = -1;
		listManager.type = -1;
		listManager.power = null;
		listManager.page = 0;
		drawerLayout.closeDrawer(filterListContent);
		filterAdapter.notifyDataSetChanged();
		for(int i = 1 ; i <= 6 ; i++){
			filterListView.collapseGroup(i);
		}
		
		//重置每个筛选器的名字
		filterList.set(1, getString(R.string._class));
		filterList.set(2, getString(R.string.type));
		filterList.set(3, getString(R.string.race));
		filterList.set(4, getString(R.string.rarity));
		filterList.set(5, getString(R.string.cost));
		filterList.set(6, getString(R.string.power));
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				displayFragment.queryData();
				displayFragment.handler
						.sendEmptyMessage(DisplayFragment.ADD_DATA);
			}
		}).start();
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		if(newText.isEmpty()){
			CardListManager.getInstance().setSearchMode(false);
			CardListManager.getInstance().setListToQuery();
		} else {
			CardListManager.getInstance().setSearchMode(true);
			displayFragment.searchData(newText);
		}
		displayFragment.handler.sendEmptyMessage(DisplayFragment.ADD_DATA);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("activity result");
		switch (requestCode) {
		case REQ_SETTINGS:
			if(resultCode == SettingsActivity.RES_PREF_CHANGED){
				System.out.println("改了显示方式");
				resetFilter();
			}
			break;
		case REQ_DETIAL:
			displayFragment.getCardAdapter().notifyDataSetChanged();
			break;
		}
	}
}
