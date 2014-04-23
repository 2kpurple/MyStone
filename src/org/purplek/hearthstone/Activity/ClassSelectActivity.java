package org.purplek.hearthstone.Activity;

import javax.xml.datatype.Duration;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
