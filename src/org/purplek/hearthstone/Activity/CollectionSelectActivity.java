package org.purplek.hearthstone.Activity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

import org.purplek.hearthstone.Constant;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.R.array;
import org.purplek.hearthstone.R.id;
import org.purplek.hearthstone.R.layout;
import org.purplek.hearthstone.R.menu;
import org.purplek.hearthstone.adapter.CollectionAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Collection;
import org.purplek.heartstone.utils.PhoneUtil;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CollectionSelectActivity extends BaseActivity implements OnItemClickListener {
	
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
		
		
		
		initTitle();
		initList();
		
	}
	
	private void initTitle(){
		String[] classArray = getResources().getStringArray(R.array.class_array);
		setTitle(classArray[clas + 1]);
	}
	
	private void initList(){
		list = new ArrayList<Collection>();
		listView = (ListView) findViewById(R.id.collection_list);
		TextView tv = (TextView) findViewById(R.id.collection_empty_text);
		listView.setEmptyView(tv);
		adapter = new CollectionAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		queryCollection();
	}
	
	private void queryCollection(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				list.clear();
				DatabaseHelper helper = DatabaseHelper.getInstance(CollectionSelectActivity.this);
				List<Collection> tempList = helper.queryCollection(clas);
				list.addAll(tempList);
				handler.sendEmptyMessage(Constant.ADD_DATA);
			}
		}).start();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, CollectionDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.COLL_ID, list.get(position).id);
		bundle.putString(Constant.COLL_NAME, list.get(position).name);
		intent.putExtras(bundle);
		startActivityForResult(intent, Constant.DETAIL_FINISH);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == Constant.DETAIL_FINISH) {
			if (resultCode == Activity.RESULT_OK) {
				queryCollection();
			}
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constant.ADD_DATA:
				adapter.notifyDataSetChanged();
				break;
			}
		}
		
	};

}
