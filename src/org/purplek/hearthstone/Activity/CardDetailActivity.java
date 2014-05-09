package org.purplek.hearthstone.Activity;

import java.util.List;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.Constant;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.adapter.CardDetailAdapter;
import org.purplek.hearthstone.model.Card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;

public class CardDetailActivity extends BaseActivity implements
		OnPageChangeListener {

	private ViewPager viewPager;
	private CardDetailAdapter adapter;
	public List<Card> cards;
	
	public String[] typeStrings;
	public String[] rarityStrings;
	public String[] classStrings;
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		Intent intent = getIntent();
		Object object = intent.getSerializableExtra(Constant.LIST);
		if(object == null){
			cards = CardListManager.getInstance().getList();
			flag = true;
		} else {
			cards = (List<Card>) object;
			flag = false;
		}
		adapter = new CardDetailAdapter(getSupportFragmentManager(), cards);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		getStrings();

		int num = intent.getIntExtra("num", -1);
		viewPager.setCurrentItem(num);

		
		setTitle(cards.get(num).name);
		
	}
	
	private void getStrings(){
		classStrings = getResources().getStringArray(R.array.class_array);
		typeStrings = getResources().getStringArray(R.array.type_array);
		rarityStrings = getResources().getStringArray(R.array.rarity_array);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		if (state == ViewPager.SCROLL_STATE_IDLE
				&& viewPager.getCurrentItem() + 1 == cards.size()
				&& !CardListManager.getInstance().isSearchMode() && flag) {
			CardListManager.getInstance().page++;
			CardListManager.getInstance().queryData(this);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		setTitle(cards.get(position).name);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
