package org.purplek.hearthstone.adapter;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.Fragment.CardDetailFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CardDetailAdapter extends FragmentStatePagerAdapter {
	
	public CardDetailAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return CardDetailFragment.newInstance(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CardListManager.getInstance().getList().size();
	}

}
