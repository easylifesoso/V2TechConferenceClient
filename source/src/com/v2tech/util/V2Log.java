package com.v2tech.util;

import android.util.Log;

/**
 * TODO add file log output
 * @author 28851274
 *
 */
public class V2Log {
	
	public static final String TAG = "V2TECH";
	
	public static boolean isDebuggable = false;
	
	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}
	
	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}
	
	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}
	
	public static void d(String tag, String msg) {
		if (isDebuggable) {
			Log.d(tag, msg);
		}
	}

	
	
	public static void i(String msg) {
		Log.i(TAG, msg);
	}
	
	public static void e(String msg) {
		Log.e(TAG, msg);
	}
	
	public static void w(String msg) {
		Log.w(TAG, msg);
	}
	
	public static void d(String msg) {
		if (isDebuggable) {
			Log.d(TAG, msg);
		}
		
	}

	//TODO record log to disk
}
