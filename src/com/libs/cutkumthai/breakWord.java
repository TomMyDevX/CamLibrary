package com.libs.cutkumthai;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;



import android.content.Context;
import android.widget.TextView;



public class breakWord {
	 Context   context;
	 private TextView textView;
	 private String  breakwordString;
	 private CharSequence breakedwordString;
	 String thaiword;

	 public breakWord(TextView textview, Context context,int width) {
	
	//  String breakwordString2 = breakwordString.replaceAll(" ", "");
	  String breakwordString2 = textview.getText().toString();
	//  Log.e(breakwordString,"original");
	  Locale thai = new Locale("th");
	  BreakIterator b = BreakIterator.getWordInstance(thai);
	  b.setText(breakwordString2);
	  ArrayList<String> token = new ArrayList<String>();
	  int start = b.first();
	  
	  for (int end = b.next(); end != BreakIterator.DONE; start = end, end = b.next()) {

	   token.add(breakwordString2.substring(start, end));
	  }

	  
	  
	  
	  
	  
	  this.context = context;

	  this.textView = textview;
	  

	 

	 

	  StringBuilder tes = new StringBuilder();



	  StringBuilder holdset = new StringBuilder();

	  for (int i = 0; i < token.size(); i++) {
	   tes.append(token.get(i));

	   float f = textview.getPaint().measureText(tes.toString());
	//   Log.e("textw", Integer.toString(textview.getWidth()));
	   
	   //right find width text method
	   
	   float df = width;
	   if (df > f) {

	   }

	   else {
//try{
	    String token_data = token.get(i-1);
	    token.remove(i-1);
	    token.add(i-1, token_data + "\n") ;
	    tes = new StringBuilder();
	    tes.append(token.get(i));
//}catch (Exception e) {
//	// TODO: handle exception
//}
	   }

	  }

	  for (int i = 0; i < token.size(); i++) {
	   holdset.append(token.get(i));
	  }

	  CharSequence ad = holdset.toString();
	  breakedwordString = ad;

	 }

	 public CharSequence getBreakedWord() {
	  return breakedwordString;

	 }

	}
