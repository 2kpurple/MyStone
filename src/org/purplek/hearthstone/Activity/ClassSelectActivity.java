package org.purplek.hearthstone.Activity;


import org.purplek.hearthstone.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

public class ClassSelectActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_select);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
	
	/**
	 * 九宫格按钮响应函数
	 * @param v
	 */
	public void onClick(View v){
		Intent intent = new Intent(this, CollectionSelectActivity.class);
		switch (v.getId()) {
		case R.id.class_select_druid:
			intent.putExtra("class", 1);
			break;
		case R.id.class_select_hunter:
			intent.putExtra("class", 2);
			break;
		case R.id.class_select_mage:
			intent.putExtra("class", 3);
			break;
		case R.id.class_select_paladin:
			intent.putExtra("class", 4);
			break;
		case R.id.class_select_priest:
			intent.putExtra("class", 5);
			break;
		case R.id.class_select_rogue:
			intent.putExtra("class", 6);
			break;
		case R.id.class_select_shaman:
			intent.putExtra("class", 7);
			break;
		case R.id.class_select_warlock:
			intent.putExtra("class", 8);
			break;
		case R.id.class_select_warrior:
			intent.putExtra("class", 9);
			break;
		}
		startActivity(intent);
	}
}
