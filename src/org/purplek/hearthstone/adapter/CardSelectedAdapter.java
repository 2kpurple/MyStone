package org.purplek.hearthstone.adapter;

import java.util.List;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.adapter.CardAdapter.ViewHolder;
import org.purplek.hearthstone.model.Card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardSelectedAdapter extends CardAdapter {
	
	private List<Card> list;
	private LayoutInflater inflater;
	
	public CardSelectedAdapter(Context context, List<Card> list){
		super(context, list);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_card_selected, null);
			holder.nameText = (TextView) convertView.findViewById(R.id.card_name);
			holder.costText = (TextView) convertView.findViewById(R.id.card_cost);
			holder.hpText = (TextView) convertView.findViewById(R.id.card_hp);
			holder.atkText = (TextView) convertView.findViewById(R.id.card_atk);
			holder.duraText = (TextView) convertView.findViewById(R.id.card_durability);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Card card = list.get(position);
		
		/* 设置卡片名字 */
		holder.nameText.setText(card.name);
		
		/* 设置稀有度的颜色 */
		holder.nameText.setTextColor(getRarityColor(card.rarity));
		
		/* 设置卡牌费用 */
		holder.costText.setText(Integer.toString(card.cost));
		
		/* 
		 * 设置仆从生命或设置武器耐久度
		 * 设置仆从或武器攻击力
		 */
		if (card.type == 1) {
			holder.atkText.setVisibility(View.VISIBLE);
			holder.hpText.setVisibility(View.VISIBLE);
			holder.duraText.setVisibility(View.GONE);
			holder.atkText.setText(Integer.toString(card.attack));
			holder.hpText.setText(Integer.toString(card.health));
		} else if (card.type == 3) {
			holder.hpText.setVisibility(View.GONE);
			holder.atkText.setVisibility(View.VISIBLE);
			holder.duraText.setVisibility(View.VISIBLE);
			holder.atkText.setText(Integer.toString(card.attack));
			holder.duraText.setText(Integer.toString(card.health));
		} else {
			holder.atkText.setVisibility(View.GONE);
			holder.hpText.setVisibility(View.GONE);
			holder.duraText.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		TextView nameText;
		TextView costText;
		TextView atkText;
		TextView hpText;
		TextView duraText;
	}

}
