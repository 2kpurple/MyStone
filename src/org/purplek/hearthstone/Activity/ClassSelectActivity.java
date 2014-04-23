package org.purplek.hearthstone.Activity;

import javax.xml.datatype.Duration;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ClassSelectActivity extends Activity {
	
	// 九宫格按钮
	private Button druidButton;
	private Button hunterButton;
	private Button mageButton;
	private Button paladinButton;
	private Button priestButton;
	private Button rogueButton;
	private Button shamanButton;
	private Button warlockButton;
	private Button warriorButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_select);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		initButton();
	}
	
	private void initButton(){
		druidButton = (Button) findViewById(R.id.class_select_druid);
		hunterButton = (Button) findViewById(R.id.class_select_hunter);
		mageButton = (Button) findViewById(R.id.class_select_mage);
		paladinButton = (Button) findViewById(R.id.class_select_paladin);
		priestButton = (Button) findViewById(R.id.class_select_priest);
		rogueButton = (Button) findViewById(R.id.class_select_rogue);
		shamanButton = (Button) findViewById(R.id.class_select_shaman);
		warlockButton = (Button) findViewById(R.id.class_select_warlock);
		warlockButton = (Button) findViewById(R.id.class_select_warrior);
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
	public void OnClick(View v){
		Intent intent = null;
		switch (v.getId()) {
		case R.id.class_select_druid:
			intent.putExtra("class", 1);
			break;
		case R.id.class_select_hunter:
			break;
		case R.id.class_select_mage:
			break;
		case R.id.class_select_paladin:
			break;
		case R.id.class_select_priest:
			break;
		case R.id.class_select_rogue:
			break;
		case R.id.class_select_shaman:
			break;
		case R.id.class_select_warlock:
			break;
		case R.id.class_select_warrior:
			break;
		}
		startActivity(intent);
	}
}
