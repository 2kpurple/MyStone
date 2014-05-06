package org.purplek.hearthstone.Activity;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Fragment.SettingsFragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends BaseActivity {
	
	public static final int RES_PREF_CHANGED = 201;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content,
	            new SettingsFragment()).commit();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

}
