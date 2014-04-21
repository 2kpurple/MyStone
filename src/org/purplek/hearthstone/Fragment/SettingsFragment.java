package org.purplek.hearthstone.Fragment;

import org.purplek.hearthstone.R;
import org.purplek.hearthstone.Activity.SettingsActivity;

import android.R.anim;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	
	public final static String PRE_COLLECTABLE = "filter_collectable";
	public final static String PRE_FEEDBACK_EMAIL = "feedback_email";
	public final static String PRE_VERSION = "version";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Preference versionPref = getPreferenceScreen().findPreference(PRE_VERSION);
		
		// 获取版本号
		PackageManager manager = getActivity().getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = info.versionName;
		
		versionPref.setSummary(version);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if(PRE_COLLECTABLE.equals(key)){
			getActivity().setResult(SettingsActivity.RES_PREF_CHANGED);
		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if(preference.getKey().equals(PRE_FEEDBACK_EMAIL)){
			Intent intent = new Intent(Intent.ACTION_SENDTO);
			// 设置邮箱地址
			intent.setData(Uri.parse("mailto:2kpurple@gmail.com"));
			// 设置邮件标题
			intent.putExtra(Intent.EXTRA_SUBJECT, "炉石app意见反馈");
			startActivity(intent);
			return true;
		}
		return false;
	}

}
