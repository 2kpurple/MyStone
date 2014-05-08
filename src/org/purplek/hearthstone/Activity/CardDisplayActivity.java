package org.purplek.hearthstone.Activity;

import java.util.ArrayList;
import java.util.List;

import org.purplek.hearthstone.Constant;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.adapter.CardAdapter;
import org.purplek.hearthstone.database.DatabaseHelper;
import org.purplek.hearthstone.model.Card;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class CardDisplayActivity extends Activity {
	
	private ListView listView;
	private CardAdapter adapter;
	private List<Card> list;
	private int collId;
	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_display);
		
		Intent intent = getIntent();
		collId = intent.getIntExtra("coll_id", 0);
		
		initList();
		initDialog();
	}
	
	private void initList(){
		listView = (ListView) findViewById(R.id.card_list);
		list = new ArrayList<Card>();
		adapter = new CardAdapter(this, list);
		listView.setAdapter(adapter);
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
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.collection_querying));
		dialog.show();
	}
	
	private void queryData(){
		DatabaseHelper helper = DatabaseHelper.getInstance(this);
		List<Card> tmpList = helper.queryCollectedCards(collId);
		if(tmpList != null){
			list.addAll(tmpList);
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constant.ADD_DATA:
				adapter.notifyDataSetChanged();
				dialog.dismiss();
				break;
			}
		}
	};
	
	
}
