package org.purplek.heartstone.utils;

import android.content.Context;
import android.widget.Toast;

public class PhoneUtil {
	public static void showToast(Context context, int string) {
		Toast.makeText(context, context.getString(string), Toast.LENGTH_SHORT)
				.show();
	}
	
	public static void showToast(Context context, String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
}
