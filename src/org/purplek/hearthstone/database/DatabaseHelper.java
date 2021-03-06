package org.purplek.hearthstone.database;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.purplek.hearthstone.model.Card;
import org.purplek.hearthstone.model.Collection;
import org.purplek.hearthstone.utils.SharePrefUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by purplekfung on 14-1-4.
 * 查询数据库
 */
public class DatabaseHelper {

    public static String LOG_DATABASE = "DATABSE";

    public static DatabaseHelper mInstance = null;
    /** DB对象 **/
    private static SQLiteDatabase mDatabase;

    private Context context;

    private static final String DATABASE_NAME = "cards.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_VERSION_KEY = "database_version";

    private static final int ITEM_PRE_PAGE = 20;

    //表名
    private static String TABLE_INFO = "card_info";

    //列名
    public static String COLUMN_ID = "card_id";
    public static String COLUMN_NAME = "card_name";
    public static String COLUMN_CLASS = "card_class";
    public static String COLUMN_ATTACK = "card_atk";
    public static String COLUMN_TYPE = "card_type";
    public static String COLUMN_HEALTH = "card_health";
    public static String COLUMN_COST = "card_cost";
    public static String COLUMN_RARITY = "card_rarity";
    public static String COLUMN_RACE = "card_race";
    public static String COLUMN_IMG = "card_img";
    public static String COLUMN_FLAVOR_Text = "card_flavor_text";
    public static String COLUMN_TEXT_IN_HAND = "card_text_in_hand";
    public static String COLUMN_COLLECTIABLE = "card_collectiable";

    //卡牌能力
    private static String BATTLECRY = "battlecry";			//战吼
    private static String CHARGE = "charge";				//冲锋
    private static String COMBO = "combo";					//连击
    private static String DEATHRATTLE = "deathrattle";		//亡语
    private static String DIVINE_SHIELD = "divine_shield";	//圣盾
    private static String ENRAGE = "enrage";				//激怒
    private static String FREEZE = "freeze";				//冻结
    private static String SECRET = "secret";				//奥秘
    private static String SILENCE = "silence";				//沉默
    private static String STEALTH = "stealth";				//潜行
    private static String TAUNT = "taunt";					//嘲讽
    private static String WINDFURY = "windfury";			//风怒
    
    
    //卡组表相关
	private static final String TABLE_COLL = "collection";
	private static final String COL_COLLID = "coll_id";
	private static final String COL_CLASS = "class";
	private static final String COL_CARDS = "cards";
	private static final String COL_NAME = "name";
	private static final String COL_ORANGE = "orange";
	private static final String COL_PURPLE = "purple";
	private static final String COL_BLUE = "blue";
	
	private static final String TABLE_CARDS = "cards";
	private static final String COL_CARDSID = "cards_id";

    public DatabaseHelper(Context context){
        this.context = context;
        mDatabase = getDatabase();
    }

    public synchronized SQLiteDatabase getDatabase(){
        String path = context.getFilesDir().getAbsolutePath() + "/" + DATABASE_NAME;
        File file = new File(path);
        int version = SharePrefUtil.getData(context, DATABASE_VERSION_KEY, -1);
        if(!file.exists() || version != DATABASE_VERSION){
            copyAssetsToPhone();
        }
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    /**
     * 根据以下条件进行筛选。当参数为-1时，不对此条件筛选。
     * @param rarity 卡牌稀有度
     * @param cost 卡牌费用
     * @param race 仆从种族
     * @param clas 仆从职业
     * @param power 仆从能力
     * @return 返回查询结果cardinfo数组
     */
	public ArrayList<Card> queryCardInfo(int rarity, int cost, int race,
			int clas, int type, String power, int page, String order,
			boolean collectable) {
        StringBuilder argsBuilder = new StringBuilder();       //用于保存需要查找的列名
        StringBuilder selectBuilder = new StringBuilder();     //用于保存条件
        boolean flag = false;
        if(rarity != -1){
            argsBuilder.append(rarity + ",");
            selectBuilder.append(COLUMN_RARITY + " = ? and ");
            flag = true;
        }
        if(cost != -1){
        	argsBuilder.append(cost + ",");
        	if(cost != 7){
                selectBuilder.append(COLUMN_COST + " = ? and ");
        	} else {
        		selectBuilder.append(COLUMN_COST + " >= ? and ");
        	}
        	flag = true;
        }
        if(race != -1){
        	System.out.println("race= " + race);
            argsBuilder.append(race + ",");
            selectBuilder.append(COLUMN_RACE + " = ? and ");
            flag = true;
        }
        if(clas != -1){
            argsBuilder.append(clas + ",");
            selectBuilder.append(COLUMN_CLASS + " = ? and ");
            flag = true;
        }
        if(type != -1){
            argsBuilder.append(type + ",");
            selectBuilder.append(COLUMN_TYPE + " = ? and ");
            flag = true;
        }
        if(power != null){
            selectBuilder.append(power + " = 1 and ");
            flag = true;
        }

        String[] selectArgs = null;
        String select = null;
        if(flag){
			if (collectable) {
				selectBuilder.append(COLUMN_COLLECTIABLE + " = 1");
				select = selectBuilder.toString();
			} else {
				String temp = selectBuilder.toString();
				select = selectBuilder.toString().substring(0,
						temp.length() - 5);
			}
			if (argsBuilder.length() != 0) {
				selectArgs = argsBuilder.toString().split(",");
			}
			System.out.println(select);
        } else {
        	if(collectable){
				select = COLUMN_COLLECTIABLE + " = 1";
        	}
        }
//        String limit = "0,1";
        String limit = ITEM_PRE_PAGE * page + "," + ITEM_PRE_PAGE;
        System.out.println(limit);
        Cursor cursor = mDatabase.query(TABLE_INFO,null,select,selectArgs,null,null,order,limit);
        ArrayList<Card> list = new ArrayList<Card>();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(getCard(cursor));
                cursor.moveToNext();
            }
            cursor.close();
            Log.i(LOG_DATABASE, "" + list.size());
        }

        return list;
    }
    
    public ArrayList<Card> queryCardInfoForCollection(int clas, int page, String order){
    	ArrayList<Card> list = new ArrayList<Card>();
    	String selection = COLUMN_CLASS + " = ? and " + COLUMN_COLLECTIABLE + " = 1";
    	String limit = ITEM_PRE_PAGE * page + "," + ITEM_PRE_PAGE;
		Cursor cursor = mDatabase
				.query(TABLE_INFO, null, selection,
						new String[] { String.valueOf(clas) }, null, null,
						order, limit);
		if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
            	// 将不是英雄的牌加入list
            	if(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE)) != 0){
            		list.add(getCard(cursor));
            	}
                cursor.moveToNext();
            }
            cursor.close();
        }
    	return list;
    }
    
    /**
     * 以卡牌名字搜索卡牌
     * @param query 卡牌名字关键字
     * @return
     */
    public ArrayList<Card> searchCardInfo(String query, boolean collectable){
    	ArrayList<Card> list = new ArrayList<Card>();
    	String selection = null;
    	if(collectable){
    		selection = COLUMN_NAME + " like ? and " + COLUMN_COLLECTIABLE + " = 1";
    	} else {
    		selection = COLUMN_NAME + " like ?";
    	}
    	Cursor cursor = mDatabase.query(TABLE_INFO, null, selection,
				new String[] { "%" + query + "%" }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(getCard(cursor));
                cursor.moveToNext();
            }
            cursor.close();
		}
    	return list;
    }
    
    /**
	 * 于数据库中添加收藏卡组
	 * @param context
	 * @param coll
	 */
	public void insertCollection(Collection coll){
		
		int orange = 0, blue = 0, purple = 0;
		
		//生成所选卡牌字符串s
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < coll.cards.size() ; i++){
			Card tmp = coll.cards.get(i);
			buffer.append(tmp.id + ",");
			switch (tmp.rarity) {
			case 4:
				orange++;
				break;
			case 3:
				purple++;
				break;
			case 2:
				blue++;
				break;
			}
		}
		buffer.deleteCharAt(buffer.length() - 1);
		
		//插入数据到collection表
		ContentValues cvColl = new ContentValues();
		cvColl.put(COL_CLASS, coll.clas);
		cvColl.put(COL_NAME, coll.name);
		cvColl.put(COL_ORANGE, orange);
		cvColl.put(COL_PURPLE, purple);
		cvColl.put(COL_BLUE, blue);
		
		//插入数据到cards表
		ContentValues cvCard = new ContentValues();
		cvCard.put(COL_CARDS, buffer.toString());
		
		//获取最后一次添加的collid
//		Cursor cursor = mDatabase.rawQuery("select last_insert_rowid()",null);
		
		//开启事务
		mDatabase.beginTransaction();
		// 插入到collection
		int id = (int) mDatabase.insert(TABLE_COLL, null, cvColl);
		cvCard.put(COL_COLLID, id);
		int result = (int) mDatabase.insert(TABLE_CARDS, null, cvCard);
		if(id != -1 && result != -1){
			mDatabase.setTransactionSuccessful();
		}
		mDatabase.endTransaction();
	}
	
	/**
	 * 以职业来查找卡牌组
	 * @return
	 */
	public List<Collection> queryCollection(int clas){
		String selection = COL_CLASS + " = ?";
		
		List<Collection> list = new ArrayList<Collection>();
		
		Cursor cursor = mDatabase.query(TABLE_COLL, null, selection,
				new String[] { String.valueOf(clas) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				Collection collection = new Collection();
				collection.name = cursor.getString(cursor.getColumnIndex(COL_NAME));
				collection.id = cursor.getInt(cursor.getColumnIndex(COL_COLLID));
				collection.clas = cursor.getInt(cursor.getColumnIndex(COL_CLASS));
				collection.orange = cursor.getInt(cursor.getColumnIndex(COL_ORANGE));
				collection.blue = cursor.getInt(cursor.getColumnIndex(COL_BLUE));
				collection.purple = cursor.getInt(cursor.getColumnIndex(COL_PURPLE));
				list.add(collection);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 获取某个卡组中的卡牌
	 * @return
	 */
	public Collection queryCollectedCards(int id){
		Collection collection = new Collection();
		List<Card> list = new ArrayList<Card>();
		int cardsId = 0;
		
		// 获取卡组卡牌ID字符串
		Cursor cursor = mDatabase.query(TABLE_CARDS,
				new String[] { COL_CARDSID, COL_CARDS }, COL_COLLID + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
		
		String cards = null;
		if(cursor != null && cursor.moveToFirst()){
			cardsId = cursor.getInt(cursor.getColumnIndex(COL_CARDSID));
			cards = cursor.getString(cursor.getColumnIndex(COL_CARDS));
		}
		
		// 获取30张卡牌并插入到list中
		String[] cardId = cards.split(",");
		Cursor cardCursor = null;
		for(int i = 0 ; i < cardId.length ; i++){
			cardCursor = mDatabase.query(TABLE_INFO, null, COLUMN_ID + " = ?", new String[]{cardId[i]}, null, null, null);
			if(cardCursor != null && cardCursor.moveToFirst()){
                list.add(getCard(cardCursor));
			}
		}
		
		collection.cards = list;
		collection.cardsId = cardsId;
		return collection;
	}
	
	/**
	 * 更新卡牌组
	 * @param collection
	 */
	public void updateCollection(Collection coll){
		
		int orange = 0, blue = 0, purple = 0;
		
		//生成所选卡牌字符串s
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < coll.cards.size() ; i++){
			Card tmp = coll.cards.get(i);
			buffer.append(tmp.id + ",");
			switch (tmp.rarity) {
			case 4:
				orange++;
				break;
			case 3:
				purple++;
				break;
			case 2:
				blue++;
				break;
			}
		}
		buffer.deleteCharAt(buffer.length() - 1);
		
		ContentValues cvColl = new ContentValues();
		cvColl.put(COL_CLASS, coll.clas);
		cvColl.put(COL_NAME, coll.name);
		cvColl.put(COL_ORANGE, orange);
		cvColl.put(COL_PURPLE, purple);
		cvColl.put(COL_BLUE, blue);
		
		ContentValues cvCard = new ContentValues();
		cvCard.put(COL_CARDS, buffer.toString());
		
		try {
			mDatabase.beginTransaction();
			mDatabase.update(TABLE_COLL, cvColl, COL_COLLID + " = ?", new String[]{String.valueOf(coll.id)});
			mDatabase.update(TABLE_CARDS, cvCard, COL_CARDSID + " = ?", new String[]{String.valueOf(coll.cardsId)});
			mDatabase.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			mDatabase.endTransaction();
		}
	}
	
	/**
	 * 删除卡牌组
	 * @param id
	 */
	public void deleteCollection(int id){
		mDatabase.beginTransaction();
		String whereClause = COL_COLLID + " = ?";
		String[] whereArgs = {String.valueOf(id)};
		int flag1 = mDatabase.delete(TABLE_COLL, COL_COLLID + " = ?", whereArgs);
		int flag2 = mDatabase.delete(TABLE_CARDS, whereClause, whereArgs);
		if(flag1 != 0 && flag2 != 0){
			mDatabase.setTransactionSuccessful();
		}
		mDatabase.endTransaction();
	}
	
	private Card getCard(Cursor cursor){
		Card card = new Card();
        card.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        card.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        card.clas = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS));
        card.rarity = cursor.getInt(cursor.getColumnIndex(COLUMN_RARITY));
        card.attack = cursor.getInt(cursor.getColumnIndex(COLUMN_ATTACK));
        card.health = cursor.getInt(cursor.getColumnIndex(COLUMN_HEALTH));
        card.cost = cursor.getInt(cursor.getColumnIndex(COLUMN_COST));
        card.race = cursor.getInt(cursor.getColumnIndex(COLUMN_RACE));
        card.type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
        card.img = cursor.getString(cursor.getColumnIndex(COLUMN_IMG));
        card.flavorText = cursor.getString(cursor.getColumnIndex(COLUMN_FLAVOR_Text));
        card.textInHand = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT_IN_HAND));
        card.collectable = cursor.getInt(cursor.getColumnIndex(COLUMN_COLLECTIABLE));
        card.battlecry = cursor.getInt(cursor.getColumnIndex(BATTLECRY));
        card.charge = cursor.getInt(cursor.getColumnIndex(CHARGE));
        card.combo = cursor.getInt(cursor.getColumnIndex(COMBO));
        card.deathrattle = cursor.getInt(cursor.getColumnIndex(DEATHRATTLE));
        card.divineShield = cursor.getInt(cursor.getColumnIndex(DIVINE_SHIELD));
        card.enrage = cursor.getInt(cursor.getColumnIndex(ENRAGE));
        card.freeze = cursor.getInt(cursor.getColumnIndex(FREEZE));
        card.secret = cursor.getInt(cursor.getColumnIndex(SECRET));
        card.silence = cursor.getInt(cursor.getColumnIndex(SILENCE));
        card.stealth = cursor.getInt(cursor.getColumnIndex(STEALTH));
        card.taunt = cursor.getInt(cursor.getColumnIndex(TAUNT));
        card.windfury = cursor.getInt(cursor.getColumnIndex(WINDFURY));
        return card;
	}

    private void copyAssetsToPhone(){
        try {
            InputStream is = context.getAssets().open(DATABASE_NAME);

            String path = context.getFilesDir().getAbsolutePath() + "/" + DATABASE_NAME;
            OutputStream os = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1){
                os.write(buffer, 0, length);
            }
            
            SharePrefUtil.setData(context, DATABASE_VERSION_KEY, DATABASE_VERSION);
            
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
