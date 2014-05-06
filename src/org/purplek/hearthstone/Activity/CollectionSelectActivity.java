package org.purplek.hearthstone.Activity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.R.array;
import org.purplek.hearthstone.R.id;
import org.purplek.hearthstone.R.layout;
import org.purplek.hearthstone.R.menu;
import org.purplek.hearthstone.adapter.CollectionAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Collection;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionSelectActivity extends BaseActivity {
	
	private int clas;
	private List<Collection> list;
	private CollectionAdapter adapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_select);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		clas = intent.getIntExtra("class", -1);
		
		list = new ArrayList<Collection>();
		queryCollection();
		
		initTitle();
		initList();
		
	}
	
	private void initTitle(){
		String[] classArray = getResources().getStringArray(R.array.class_array);
		setTitle(classArray[clas + 1]);
	}
	
	private void initList(){
		listView = (ListView) findViewById(R.id.collection_list);
		TextView tv = (TextView) findViewById(R.id.collection_empty_text);
		listView.setEmptyView(tv);
		adapter = new CollectionAdapter(this, list);
		listView.setAdapter(adapter);
	}
	
	private void queryCollection(){
		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		List<Collection> tempList = helper.queryCollection(clas);
		list.addAll(tempList);
		System.out.println("size : " + list.size());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection_select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_new_collection:
			Intent intent = new Intent(this, CollectionEditActivity.class);
			intent.putExtra("class", clas);
			startActivity(intent);
			break;
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


}
