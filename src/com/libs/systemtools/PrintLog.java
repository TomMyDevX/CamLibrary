package com.libs.systemtools;

import android.content.Context;
import android.util.Log;

public class PrintLog {
	public static void print(Context con,String msg){
		Log.e(con.getApplicationInfo().packageName, msg);
	}
}
