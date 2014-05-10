package org.purplek.hearthstone.model;

import java.util.List;

/**
 * 用于保存收藏卡组的model类
 * @author purplekfung
 *
 */
public class Collection {
	public int id;
	public int clas;
	public List<Card> cards;
	public String name;
	public int orange;
	public int purple;
	public int blue;
}
