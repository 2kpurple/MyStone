package org.purplek.hearthstone.Activity;

import java.util.ArrayList;
import java.util.List;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Fragment.CardSelectFragment;
import org.purplek.hearthstone.Fragment.DisplayFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class CollectionEditActivity extends FragmentActivity {
	
	private int clas;
	private ViewPager viewPager;
	private List<Fragment> list;
	private int count;   //	保存选择的卡牌数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_edit);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		clas = intent.getIntExtra("class", -1);
		
		// 如果是编辑 传入编辑参数 如果是新建，则不需要
		initViewPager();
	}
	
	private void initViewPager(){
		viewPager = (ViewPager) findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
		list = new ArrayList<Fragment>();
		
		CardSelectFragment fragment1 = new CardSelectFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("class", clas);
		fragment1.setArguments(bundle);
		
		list.add(fragment1);
		list.add(new CardSelectFragment());
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
