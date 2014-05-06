package org.purplek.hearthstone.adapter;

import java.util.HashMap;
import java.util.List;
import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.model.Card;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter {
	
	protected Context context;
	protected LayoutInflater inflater;
	private HashMap<String, Integer> selectedMap;
	protected List<Card> list;
	
	public CardAdapter(Context context, List<Card> list){
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		selectedMap = new HashMap<String, Integer>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_card_info, null);
			holder.nameText = (TextView) convertView.findViewById(R.id.card_name);
			holder.typeText = (TextView) convertView.findViewById(R.id.card_type);
			holder.inHandText = (TextView) convertView.findViewById(R.id.text_in_hand);
			holder.costText = (TextView) convertView.findViewById(R.id.card_cost);
			holder.hpText = (TextView) convertView.findViewById(R.id.card_hp);
			holder.atkText = (TextView) convertView.findViewById(R.id.card_atk);
			holder.duraText = (TextView) convertView.findViewById(R.id.card_durability);
			holder.cardContent = (RelativeLayout) convertView.findViewById(R.id.item_content);
			holder.countText = (TextView) convertView.findViewById(R.id.card_select_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Card card = list.get(position);
		
		/* 设置卡片名字 */
		holder.nameText.setText(card.name);
		
		/* 设置稀有度的颜色 */
		holder.nameText.setTextColor(getRarityColor(card.rarity));
		
		/* 设置手牌显示文字 */
		holder.inHandText.setText(card.textInHand);
		
		/* 设置类型字符串 */
		holder.typeText.setText(getTypeString(card));
		
		/* 设置卡牌费用 */
		holder.costText.setText(Integer.toString(card.cost));
		
		/* 判断是否有选择该卡牌 */
		if(selectedMap.get(card.name) == null){
			holder.countText.setVisibility(View.GONE);
		} else {
			holder.countText.setVisibility(View.VISIBLE);
			holder.countText.setText(String.valueOf(selectedMap.get(card.name)));
		}
		
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
		public TextView nameText;
		public TextView typeText;
		public TextView inHandText;
		public TextView costText;
		public TextView hpText;
		public TextView atkText;
		public TextView duraText;
		public RelativeLayout cardContent;
		public TextView countText;
	}

	public void addDataToList(List<Card> data){
		CardListManager.getInstance().getList().addAll(data);
	}

	/**
	 * 稀有度 0为白卡 1为基础卡 2为蓝卡 3为紫卡 4为橙卡
	 *  2，3，4时分辨返回蓝色紫色和橙色的ID
	 * @param rarity
	 * @return
	 */
	public int getRarityColor(int rarity){
		int color = -1;
		switch (rarity) {
		case 1:
			color = context.getResources().getColor(R.color.green);
			break;
		case 2:
			color = context.getResources().getColor(R.color.blue);
			break;
		case 3:
			color = context.getResources().getColor(R.color.purple);
			break;
		case 4:
			color = context.getResources().getColor(R.color.orange);
			break;
		default:
			color = context.getResources().getColor(R.color.primary_text_color);
			break;
		}
		return color;
	}
	
	/**
	 * 生成卡牌类型字符串
	 * @param card
	 * @return
	 */
	public String getTypeString(Card card){
		StringBuffer buffer = new StringBuffer();
		//生成职业
		String[] classArray = context.getResources().getStringArray(R.array.class_array);
		buffer.append(classArray[card.clas + 1]);
		
		//生成类型
		String[] typeArray = context.getResources().getStringArray(R.array.type_array);
		buffer.append(" " + typeArray[card.type + 1]);
		
		//生成种族 先判断是否有种族
		if(card.race != 0){
			String[] raceArray = context.getResources().getStringArray(R.array.race_array);
			buffer.append(" " + raceArray[card.race]);
		}
		
		return buffer.toString();
	}

	public HashMap<String, Integer> getSelectedMap() {
		return selectedMap;
	}
	
	public List<Card> getList(){
		return list;
	}
	
	public void setList(List<Card> list){
		this.list = list;
	}
	
}
