package com.libs.androidlibs;



import com.libs.cutkumthai.Thai;
import com.libs.cutkumthai.breakWord;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class TextUntil  {

	public static void AdjustText(final TextView tv,final Context context){
		tv.post(new Runnable() {
		    @Override
		    public void run() {
		        int w = tv.getMeasuredWidth();
		        int h = tv.getMeasuredHeight();
			    breakWord bw = new breakWord(tv, context, w);
				String str_t = bw.getBreakedWord().toString();
				str_t = new Thai(str_t).target;
				//Log.e("trim",str_t);
				tv.setText(str_t.trim());
		    }
		});

	}
	  
}
