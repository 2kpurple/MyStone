package org.purplek.hearthstone.Fragment;

import java.io.IOException;
import java.io.InputStream;

import org.purplek.hearthstone.CardListManager;
import org.purplek.hearthstone.R;
import org.purplek.hearthstone.R.drawable;
import org.purplek.hearthstone.Activity.CardDetialActivity;
import org.purplek.hearthstone.model.Card;

import android.support.v4.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CardDetailFragment extends Fragment {
	
	private int num;
	
	/**
	 * 获取fragment的实例
	 * @param num 
	 * @return
	 */
	public static CardDetailFragment newInstance(int num){
		CardDetailFragment fragment = new CardDetailFragment();
		
		// 传参数到fragment 
		Bundle args = new Bundle();
		args.putInt("num", num);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		num = getArguments() != null ? getArguments().getInt("num") : 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detail, container, false);
		
		// 获取控件
		ImageView cardImage = (ImageView) view.findViewById(R.id.card_img);
		TextView typeText = (TextView) view.findViewById(R.id.detail_type_text);
		TextView rarityText = (TextView) view.findViewById(R.id.detail_rarity_text);
		TextView classText = (TextView) view.findViewById(R.id.detail_class_text);
		TextView flavorText = (TextView) view.findViewById(R.id.detail_flavor_text);
		
		Card card = CardListManager.getInstance().getList().get(num);
		
		// 设置卡牌显示数据
		String type = ((CardDetialActivity)getActivity()).typeStrings[card.type + 1];
		String clas = ((CardDetialActivity)getActivity()).classStrings[card.clas + 1];
		String rarity = ((CardDetialActivity)getActivity()).rarityStrings[card.rarity + 1];
		typeText.setText(type);
		rarityText.setText(rarity);
		classText.setText(clas);
		flavorText.setText(card.flavorText);
		
		// 设置卡牌图片
		AssetManager manager = getActivity().getAssets();
		try {
			InputStream is = manager.open("img/" + card.img + ".jpg");
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			cardImage.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}
	
}
