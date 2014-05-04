package org.purplek.hearthstone.model;

import java.util.Set;

/**
 * 用于保存收藏卡组的model类
 * @author purplekfung
 *
 */
public class Collection {
	public int id;
	public int clas;
	public Set<Card> cards;
	public String name;
}
