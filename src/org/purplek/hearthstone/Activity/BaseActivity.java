package org.purplek.hearthstone.Activity;

import org.purplek.hearthstone.R;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right,
				R.anim.leave_to_left);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.enter_from_right,
				R.anim.leave_to_left);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.enter_from_left,
				R.anim.leave_to_right);
	}
}
