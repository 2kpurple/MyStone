package org.purplek.hearthstone.utils;

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
	
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
