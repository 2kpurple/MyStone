package org.purplek.hearthstone.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Fragment.CardSelectFragment;
import org.purplek.hearthstone.Fragment.CardStatFragment;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;
import org.purplek.hearthstone.model.Collection;
import org.purplek.hearthstone.support.PriorityList;
import org.purplek.heartstone.utils.PhoneUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

public class CollectionEditActivity extends BaseActivity {
	
	private int clas;
	private ViewPager viewPager;
	private List<Fragment> list;
	
	public List<Card> cards;	// 保存卡牌的list
	
	private final static String COUNT_FORMAT = "(%d/30)";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_edit);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		clas = intent.getIntExtra("class", -1);
		
		cards = new PriorityList<Card>();
		
		// 如果是编辑 传入编辑参数 如果是新建，则不需要
		initViewPager();
		
		setActivityTitle();
	}
	
	public void setActivityTitle(){
		int position = viewPager.getCurrentItem();
		String count = String.format(COUNT_FORMAT, cards.size());
		switch (position) {
		case 0:
			setTitle(getString(R.string.select_class_card) + count);
			break;
		case 1:
			setTitle(getString(R.string.select_neutral_card) + count);
			break;
		case 2:
			setTitle(getString(R.string.card_stat) + count);
			break;
		}
	}
	
	private void initViewPager(){
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				setActivityTitle();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
		list = new ArrayList<Fragment>();
		
		CardSelectFragment fragment = new CardSelectFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("class", clas);
		fragment.setArguments(bundle);
		
		list.add(fragment);
		list.add(new CardSelectFragment());
		list.add(new CardStatFragment());
		viewPager.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_save_collection:
			saveCollection();
			PhoneUtil.showToast(this, R.string.insert_success);
			finish();
			break;
		case R.id.action_cancel_collection:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveCollection(){
		if(cards.size() < 30){
			System.out.println("no cards");
			return;
		}
		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		Collection coll = new Collection();
		coll.cards = cards;
		coll.clas = clas;
		coll.name = "test";
		helper.insertCollection(coll);
	}
	
	private class MyPagerAdapter extends FragmentPagerAdapter{
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub d
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			System.out.println(position);
			return list.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		
	}
}
