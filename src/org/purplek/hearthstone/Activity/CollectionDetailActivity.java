package org.purplek.hearthstone.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.purplek.hearthstone.Constant;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.adapter.CardAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;
import org.purplek.hearthstone.support.BarView;
import org.purplek.heartstone.utils.PhoneUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CollectionDetailActivity extends BaseActivity implements OnItemClickListener {
	
	private ListView listView;
	private CardAdapter adapter;
	private List<Card> list;
	private int collId;
	private String collName;
	private AlertDialog dialog;
	private BarView barView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_display);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			collId = bundle.getInt(Constant.COLL_ID);
			collName = intent.getStringExtra(Constant.COLL_NAME);
		}
		
		setTitle(collName);
		
		initList();
		initDialog();
	}
	
	private void initList(){
		listView = (ListView) findViewById(R.id.card_list);
		list = new ArrayList<Card>();
		adapter = new CardAdapter(this, list);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.list_headview_card_stat,
				null);
		barView = (BarView) headerView.findViewById(R.id.barView);
		barView.setTextSize(20);
		listView.addHeaderView(headerView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		List<String> bottomStringList = Arrays.asList(Constant.COST_ARRAY);
		barView.setBottomTextList(bottomStringList);
		adapter.notifyDataSetChanged();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				queryData();
				handler.sendEmptyMessage(Constant.ADD_DATA);
			}
		}).start();
	}
	
	private void initDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.confirm_remove));
		builder.setPositiveButton(R.string.confirm, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						removeCollection();
					}
				}).start();
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = builder.create();
	}
	
	private void queryData(){
		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		List<Card> tmpList = helper.queryCollectedCards(collId);
		for(int i = 0 ; i < tmpList.size() ; i++){
			if(i < tmpList.size() - 1){
				if(tmpList.get(i).id == tmpList.get(i+1).id){
					adapter.getSelectedMap().put(tmpList.get(i).name, 2);
				} else {
					list.add(tmpList.get(i));
				}
			} else {
				if(tmpList.get(i-1).id == tmpList.get(i).id){
					list.add(tmpList.get(i));
				}
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
			case Constant.DELETE_SUCCESS:
				setResult(Activity.RESULT_OK);
				PhoneUtil.showToast(CollectionDetailActivity.this,
						R.string.remove_success);
				finish();
				break;
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_edit_collection:
			
			break;
		case R.id.action_remove_collection:
			dialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection_detail, menu);
		return true;
	}
	
	private void removeCollection(){
		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		helper.deleteCollection(collId);
		handler.sendEmptyMessage(Constant.DELETE_SUCCESS);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, CardDetailActivity.class);
		intent.putExtra(Constant.NUM, position);
		intent.putExtra(Constant.LIST, (Serializable)list);
		startActivity(intent);
	}
	
}
