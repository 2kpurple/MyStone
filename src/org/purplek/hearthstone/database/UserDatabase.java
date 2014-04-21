package org.purplek.hearthstone.database;

import org.purplek.hearthstone.model.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase {
	
	private static final String DATA_BASE = "user.db";
	private static final int VERSION = 1;
	private static SQLiteDatabase db = null;
	private static DatabaseHelper dbHelper = null;
	
	
	private static final String TABLE_COLL = "collection";
	private static final String COL_COLLID = "coll_id";
	private static final String COL_CLASS = "class";
	private static final String COL_CARDS = "cards";
	private static final String COL_NAME = "name";
	
	private static final String TABLE_CARDS = "cards";
	private static final String COL_CARDSID = "cards_id";
	
	public static void init(Context context){
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 于数据库中添加收藏卡组
	 * @param context
	 * @param coll
	 */
	public static void addCollection(Context context, Collection coll){
		if(dbHelper == null){
			init(context);
		}
		
		//生成所选卡牌字符串s
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < coll.cards.size() ; i++){
			buffer.append(coll.cards.get(i));
		}
		buffer.deleteCharAt(buffer.length() - 1);
		
		//插入数据到collection表
		ContentValues cv = new ContentValues();
		cv.put(COL_CLASS, coll.clas);
		cv.put(COL_NAME, coll.name);
		
		//插入数据到cards表
		cv = new ContentValues();
		cv.put(COL_CARDSID, coll.id);
		
		//获取最后一次添加的collid
		Cursor cursor = db.rawQuery("select last_insert_rowid() from person",null);
		//开启事务
		try {
			db.beginTransaction();
			db.insert(TABLE_COLL, null, cv);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		
	}
	
	public static Collection queryCollection(Context context){
		return null;
	}

	public static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATA_BASE, null, VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String sql1 = "CREATE TABLE IF NOT EXISTS " + TABLE_COLL 
					+ " (" + COL_COLLID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COL_NAME + " TEXT NOT NULL,"
					+ COL_CLASS + " INTEGER NOT NULL)";
			String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_CARDS
					+ " (" + COL_CARDSID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ COL_COLLID + " INTEGER NOT NULL,"
					+ COL_CARDS + " TEXT NOT NULL)";
			try {
				db.execSQL(sql1);
				db.execSQL(sql2);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
