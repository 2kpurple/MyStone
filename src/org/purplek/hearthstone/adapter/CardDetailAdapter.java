package org.purplek.hearthstone.adapter;

import java.util.List;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.Fragment.CardDetailFragment;
import org.purplek.hearthstone.model.Card;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CardDetailAdapter extends FragmentStatePagerAdapter {
	
	private List<Card> list;
	
	public CardDetailAdapter(FragmentManager fm, List<Card> list) {
		super(fm);
		this.list = list;
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
		return list.size();
	}

}
