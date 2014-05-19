package org.purplek.hearthstone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * 使用Sharepreference保存某些简单的数据
 * 
 */
public class SharePrefUtil {
	
	public static String getData(Context context, String key,
			String defaultValue) {
		final SharedPreferences prefs = getSharedPreferences(context);
		String value = prefs.getString(key, defaultValue);
		return value;
	}
	
	public static boolean setData(Context context, String key, String value) {
		final SharedPreferences prefs = getSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public static int getData(Context context, String key,
			int defaultValue) {
		final SharedPreferences prefs = getSharedPreferences(context);
		int value = prefs.getInt(key, defaultValue);
		return value;
	}
	
	public static boolean getData(Context context, String key, boolean defaultValue){
		final SharedPreferences prefs = getSharedPreferences(context);
		boolean value = prefs.getBoolean(key, defaultValue);
		return value;
	}
	
	public static boolean setData(Context context, String key, int value) {
		final SharedPreferences prefs = getSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
