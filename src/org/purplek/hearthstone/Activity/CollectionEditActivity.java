package org.purplek.hearthstone.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.purplek.hearthstone.Constant;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Fragment.CardSelectFragment;
import org.purplek.hearthstone.Fragment.CardStatFragment;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;
import org.purplek.hearthstone.model.Collection;
import org.purplek.hearthstone.support.PriorityList;
import org.purplek.hearthstone.utils.PhoneUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CollectionEditActivity extends BaseActivity {
	
	private int clas;
	private ViewPager viewPager;
	private List<Fragment> list;
	
	public List<Card> cards;	// 保存卡牌的list
	public HashMap<String, Integer> selectedMap;
	private Dialog cancelDialog;
	private Dialog editDialog;
	
	private Collection collection;
	
	private boolean isEditMode;
	
	private final static String COUNT_FORMAT = "(%d/30)";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_edit);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		cards = new PriorityList<Card>();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
//		if(bundle == null){
//			clas = bundle.getInt(Constant.CLASS_KEY);
//			isEditMode = false;
//		} else {
//			clas = bundle.getInt(Constant.CLASS_KEY);
//			collection = (Collection) bundle.getSerializable(Constant.COLLECTION);
//			List<Card> tmpList = collection.cards;
//			if(tmpList != null){
//				cards.addAll(tmpList);
//				initSelectedMap(cards);
//			}
//			isEditMode = true;
//		}
		
		if(bundle != null){
			clas = intent.getIntExtra(Constant.CLASS_KEY, -1);
			collection = (Collection) bundle.getSerializable(Constant.COLLECTION);
			if(collection != null){
				clas = collection.clas;
				List<Card> tmpList = collection.cards;
				if(tmpList != null){
					cards.addAll(tmpList);
					initSelectedMap(cards);
				}
				isEditMode = true;
			} else {
				isEditMode = false;
			}
		}
		
		
		// 如果是编辑 传入编辑参数 如果是新建，则不需要
		initViewPager();
		initDialog();
		initEditDialog();
		setActivityTitle();
		if(isEditMode){
			viewPager.setCurrentItem(2);
		}
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
	
	private void initSelectedMap(List<Card> list){
		selectedMap = new HashMap<String, Integer>();
		if(list == null || list.size() == 0){
			return;
		}
		for(Card card : cards){
			if(selectedMap.get(card.name) == null){
				selectedMap.put(card.name, 1);
			} else {
				selectedMap.put(card.name, 2);
			}
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
	
	private void initDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.confirm_cancel));
		builder.setPositiveButton(R.string.confirm, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface cancelDialog, int which) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						finish();
					}
				}).start();
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface cancelDialog, int which) {
				// TODO Auto-generated method stub
				cancelDialog.dismiss();
			}
		});
		cancelDialog = builder.create();
	}
	
	private void initEditDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setHint(R.string.enter_collection_name_hint);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		builder.setView(input);
		builder.setTitle(R.string.enter_collection_name);
		if(isEditMode){
			input.setText(collection.name);
		}
		builder.setPositiveButton(R.string.confirm, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String name = input.getText().toString();
				if(TextUtils.isEmpty(name)){
					PhoneUtil.showToast(CollectionEditActivity.this, R.string.collection_name_cannot_empty);
					return;
				}
				if(isEditMode){
					updateCollection(name);
				} else {
					saveCollection(name);
				}
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				editDialog.dismiss();
			}
		});
		editDialog = builder.create();
	}
	
	private void showCancelDialog(){
		cancelDialog.show();
	}
	
	private void showEditDialog(){
		editDialog.show();
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
			showCancelDialog();
			break;
		case R.id.action_save_collection:
			if(cards.size() < 30){
				handler.sendEmptyMessage(Constant.CARDS_NOT_ENOUGH);
			} else {
				showEditDialog();
			}
			break;
		case R.id.action_cancel_collection:
			showCancelDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showCancelDialog();
	}

	private void saveCollection(final String name){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DatabaseHelper helper = DatabaseHelper.getInstance(CollectionEditActivity.this);
				Collection coll = new Collection();
				coll.cards = cards;
				coll.clas = clas;
				coll.name = name;
				helper.insertCollection(coll);
				handler.sendEmptyMessage(Constant.INSERT_SUCCESS);
			}
		}).start();
	}
	
	private void updateCollection(final String name){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DatabaseHelper helper = DatabaseHelper.getInstance(CollectionEditActivity.this);
				collection.cards = cards;
				collection.name = name;
				helper.updateCollection(collection);
				handler.sendEmptyMessage(Constant.UPDATE_SUCCESS);
			}
		}).start();
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constant.CARDS_NOT_ENOUGH:
				PhoneUtil.showToast(CollectionEditActivity.this, R.string.cards_not_enought);
				break;
			case Constant.INSERT_SUCCESS:
				PhoneUtil.showToast(CollectionEditActivity.this, R.string.insert_success);
				setResult(RESULT_OK);
				if(editDialog.isShowing()){
					editDialog.dismiss();
				}
				finish();
				break;
			case Constant.UPDATE_SUCCESS:
				PhoneUtil.showToast(CollectionEditActivity.this, R.string.update_succes);
				setResult(RESULT_OK);
				if(editDialog.isShowing()){
					editDialog.dismiss();
				}
				finish();
				break;
			}
		}
		
	};
	
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
