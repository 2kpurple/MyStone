package org.purplek.hearthstone.model;

/**
 * Created by purplekfung on 14-1-3.
 * CardInfo保存卡片的基本信息
 */

public class Card implements Comparable<Card>{
    public int id;
    public String name;
    public int clas;
    public int rarity;
    public int type;
    public int attack;
    public int health;
    public int cost;
    public int race;
    public String img;
    public String flavorText;
    public String textInHand;
    public int collectable;
    public int battlecry;
    public int charge;
    public int combo;
    public int deathrattle;
    public int divineShield;
    public int enrage;
    public int freeze;
    public int secret;
    public int silence;
    public int stealth;
    public int taunt;
    public int windfury;
    
	@Override
	public int compareTo(Card another) {
		// TODO Auto-generated method stub
		if(this.cost > another.cost){
			return 1;
		} else if(this.cost < another.cost) {
			return -1;
		} else {
			// 如果cost相等则判断id大小
			if(this.id > another.id){
				return 1;
			} else if(this.id < another.id){
				return -1;
			} else {
				return 0;
			}
		}
	}
    
    

}