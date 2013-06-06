/*
* 
* + Thai.as (Sleepy Type Tool) version 1.3.3
* 	- online service : http://services.sleepydesign.com/typetool/
*	- source : http://code.google.com/p/sleepytypetool/
* 
*/
 
/*
* Copyright (C) 2005-2007 Todsaporn Banjerdkit (katopz), sleepydesign.com.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this 
* software and associated documentation files (the "Software"), to deal in the Software 
* without restriction, including without limitation the rights to use, copy, modify, 
* merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
* permit persons to whom the Software is furnished to do so, subject to the following 
* conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies 
* or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
* PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
* OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
* 
*/
 
/*
* 
* + feature
*   - จัดตำ�?หน่งตำ�?หน่งสระ/วรรณยุ�?ต์ ที่ลอย หรือทับหาง 
*   - �?�?้ไขปั�?หา�?าร�?สดงผล �? ในโปร�?�?รม Adobe Photoshop 7
*   - �?�?้ไขปั�?หาภาษาไทยพิมพ์ไม่ได้ในโปร�?�?รม Adobe CS,CS2
*   - สร้าง embed font สำหรับ Flash
*   - ตัวปัดบรรทัด(0x0D0A)ตัด Carriage Return(0x0D)+ Line Feed(0x0A) เหลือ�?ค่ (0x0A)
*   - ย�?เลิ�?�?าร�?�?้ไขจา�?ตัวที่�?�?้ (fix)ไป�?ล้ว (beta)
*    
* + to do
* 	- clean stupid/ugly/encap code
*	- loop/bitwise optimize
* 	- convert to as3
* 
* + target
*   - อ่อ้อ๊อ๋อ์อ�?อ็ป่ป้ป๊ป๋ป์ป�?ป็อั่อั้อั๊อั๋ปั่ปั้ปั๊ปั๋อิ่อี้อึ๊อื๋ปิ่ปี้ปึ๊ปื๋ปิ์อำอ่ำอ้ำอ๊ำอ๋ำปำป่ำป้ำป๊ำป๋ำปุ่ปุ้ปุ๊ปุ๋อุอูอฺ�?�?ุ�?ู�?ฺฤุฤูฤฺฎุฎูฎฺ�?ุ�?ู�?ฺ�?�?ุ�?ู�?ฺ
* 
* + thaiStcom.jimmysoftware.hw.datarue, isFixTail:true, isFixAum:true, isFixLowTail:true, isFixYoying:false, isFixYoyingTail:true}
* 
* + fontFamily
*   - psl_ad = PSL-AD,DB-,JS-,Courier
*   - psl_sp = PSL,PSL-SP,NP,UPC,DSE,Tahoma
*   - ds = DS-
* 
* + outputType
*   - multibyte
*   - multibyte_cs
*   - ascii_ps7
* 
* + example #1
*   - var target:Thai = new Thai(inputString:String, inputThaiStyle:Object) 
* 
* + example #2
*   - var target:Thai = new Thai(inputString:String) 
*   - target.setThaiStyle(inputThaiStyle:Object) 
*   - target.applyThaiStyle()
*   - target.removeThaiStyle()
* 
* + example #3
*   - var target:Thai = new Thai().getEmbedString();
* 
* */

package com.libs.cutkumthai;

import android.util.Log;

public class Thai{
	//----------style
	public class ThaiStyle{
		public Boolean isFixFloat=false, isFixTail=false, isFixAum=false, isFixLowTail=false, isFixYoying=false, isFixYoyingTail=false, isFixLineFeed=false;
		public String fontFamily="", outputType="";
	}
	ThaiStyle thaiStyle = new ThaiStyle();
	//----------static
	private String tone = "";
	private String vowel = "";
	private String bottomVowel = "";
	private String aum = "";
	private String longTail = "";
	private String lowTail = "";
	private String yoying = "";
	private String yoyingPS7 = "";
	private String yoyingNoTail = "";
	private String thothan = "";
	private String thothanNoTail = "";
	//----------dynamic
	private String highTone = "";
	private String lowTone = "";
	private String lowerleftTone = "";
	private String lowerleftVowel = "";
	private String lowBottomVowel = "";
	private String aumCombo = "";
	private String escapeChar = "";
	
	public String target = "";
	char newline=0x0a;
	//------------------------------get/set
	void set(String inputString){
		this.target = inputString;
	}
	String get(){
		return target;
	}
	//------------------------------function
	private String fixYoYing(String target) {
		//default
		if (target == null) {
			target = "";
		}
		String resultString = "";
		if (thaiStyle.isFixYoying || thaiStyle.isFixYoyingTail) {
			for (int i = 0; i<target.length(); i++) {
				//char by char
				char c = target.charAt(i);
				
				//skip some
				Boolean isEscape = (escapeChar != null) && (escapeChar.indexOf(c)>-1);
				//char code
				int charCode = Number(target.charAt(i));
				//skip newline
				Boolean isNewline = (c == newline);
				if (!isNewline && ((yoying+thothan).indexOf(c)>-1)) {
					//next is bottomVowel?
					Boolean isBeforeBottomVowel = false;
					if(i+1<target.length()){
						isBeforeBottomVowel = (bottomVowel.indexOf(target.charAt(i+1))>-1);
					}
					//-----------------�?
					if (charCode == Number(yoying.charAt(0))) {
						//isFixYoying
						if (thaiStyle.isFixYoying) {
							c = yoyingPS7.charAt(0);
						}
						//isFixYoyingTail                
						if (thaiStyle.isFixYoyingTail && isBeforeBottomVowel) {
							c = yoyingNoTail.charAt(0);
						}
					}
					//-----------------�?                
					if (charCode == Number(thothan.charAt(0))) {
						//isFixYoyingTail
						if (thaiStyle.isFixYoyingTail && isBeforeBottomVowel) {
							c = thothanNoTail.charAt(0);
						}
					}
					resultString += c;
				} else {
					resultString += target.charAt(i);
				}
			}
		} else {
			return target;
		}
		//return
		return resultString;
	}
	
//	public String getEmbedString() {
//		String resultString = "";
//		//normal
//		for (int i = 20; i<255; i++) {
//			resultString += ""+i;
//		}
//		//fix
//		resultString += highTone+lowTone+lowerleftTone+lowerleftVowel+lowBottomVowel+aumCombo+yoyingNoTail+thothanNoTail;
//		//return
//		return resultString;
//	}
	
	public String asciiToUnicode(String target){
		//default
		if (target == null) {
			target = "";
		}
		//result                
		String resultString = "";
		for (int i = 0; i<target.length(); i++) {
			//char by char
			char c = target.charAt(i);
			//only thai and not fixed
			int charCode = Number(target.charAt(i));
			//skip newline
			Boolean isNewline = ((c == newline) || (charCode == 13));
			if (!isNewline) {
				if ((charCode>127) && (charCode<3584) && (charCode<0xF000)) {
					//ascii -> unicode
					charCode = charCode+3585-161;
				}
				resultString += (char)(charCode);
			} else {
				resultString += c;
			}
		}
		//return
		return resultString;
	}
	
//	public String unicodeToAscii(String target){
//		//default
//		if (target == null) {
//			target = "";
//		}
//		//result                
//		String resultString = "";
//		for (var i = 0; i<target.length; i++) {
//			//char by char
//			var char = target.charAt(i);
//			//only thai and not fixed
//			var charCode = Number(target.charCodeAt(i));
//			//skip newline
//			var isNewline:Boolean = ((char == newline) || (charCode == 13));
//			if (!isNewline) {
//				if ((charCode>3584) && (charCode<0xF000)) {
//					//unicode ->ascii
//					charCode = charCode-3585+161;
//				}
//				if (charCode<128) {
//					resultString += char;
//				} else {
//					resultString += chr(charCode);
//				}
//			} else {
//				resultString += char;
//			}
//		}
//		//return
//		return resultString;
//	}
	private String ps7Chr(String target) {
//		//default
//		if (target == undefined) {
//			target = new String(_value);
//		}
		String resultString = "";
		for (int i = 0; i<target.length(); i++) {
			//char by char
			char c = target.charAt(i);
			//is that bottomVowel?
			Boolean isLowBottomVowel = (lowBottomVowel.indexOf(c)>-1);
			//skip some
			Boolean isEscape = (escapeChar != null) && (escapeChar.indexOf(c)>-1);
			//char code
			int charCode = Number(target.charAt(i));
			//skip newline
			Boolean isNewline = (c == newline);
			//only thai and not fixed
			if (!isNewline && !isLowBottomVowel && (charCode>3584) && !isEscape) {
				//unicode ->ascii
				charCode = charCode-3585+161;
				resultString += (char)(charCode);
			} else {
				resultString += target.charAt(i);
			}
		}
		//return
		return resultString;
	}
	private String csChr(String target) {
//		//default
//		if (target == undefined) {
//			target = new String(_value);
//		}
		//result                
		String resultString = "";
		for (int i = 0; i<target.length(); i++) {
			//char by char
			char c = target.charAt(i);
			//only thai and not fixed
			int charCode = Number(target.charAt(i));
			//skip newline
			Boolean isNewline = ((c == newline) || (charCode == 13));
			if (!isNewline) {
				if ((charCode>3584) && (charCode<0xF000)) {
					//unicode ->ascii
					charCode = charCode-3585+161;
				}
				if (charCode<126) {
					resultString += c;
				} else {
					resultString += (char)(0xF0+charCode);
				}
			} else {
				resultString += c;
			}
		}
		//return
		return resultString;
	}
//	private function removeCSChr(target:String):String {
//		//default
//		if (target == undefined) {
//			target = new String(_value);
//		}
//		//result                
//		var resultString = "";
//		for (var i = 0; i<target.length; i++) {
//			//char by char
//			var char = target.charAt(i);
//			//only thai and not fixed
//			var charCode = Number(target.charCodeAt(i));
//			//skip newline
//			var isNewline:Boolean = ((char == newline) || (charCode == 13));
//			if ((!isNewline) && (charCode>128)) {
//				if ((charCode>3584) && (charCode<0xF000)) {
//					//unicode ->ascii
//					charCode = -charCode+3585-161;
//				}
//				//resultString += (char)(charCode-0xF000+3585-161);          
//				if (charCode<126) {
//					resultString += char;
//				} else {
//					resultString += (char)(charCode-0xF000+3585-161);
//					//resultString += (char)("0xF0"+charCode.toString(16));
//				}
//			} else {
//				resultString += char;
//			}
//			//trace(charCode);
//		}
//		//return
//		return resultString;
//	}
//	private function removePS7Chr(target:String):String {
//		//default
//		if (target == undefined) {
//			target = new String(_value);
//		}
//		var resultString = "";
//		for (var i = 0; i<target.length; i++) {
//			//char by char
//			var char = target.charAt(i);
//			//is that bottomVowel?
//			var isLowBottomVowel = (lowBottomVowel.indexOf(char)>-1);
//			//skip some
//			var isEscape = (escapeChar != undefined) && (escapeChar.indexOf(char)>-1);
//			//char code
//			var charCode = Number(target.charCodeAt(i));
//			//skip newline
//			var isNewline:Boolean = (char == newline);
//			//only thai and not fixed
//			//if (!isNewline && !isLowBottomVowel && (charCode>3584) && !isEscape) {
//			//trace("*"+charCode);
//			if (!isNewline && !isLowBottomVowel && (charCode<3584-21) && !isEscape) {
//				//trace("*"+charCode+"<"+(3584-21));
//				//unicode ->ascii
//				charCode = charCode+3585-161;
//				//resultString += chr(charCode);
//				resultString += (char)(charCode);
//				//trace("**"+charCode);
//			} else {
//				//resultString += target.charAt(i);
//				charCode = charCode-3585+161;
//				resultString += (char)(charCode);
//				//trace("***"+target.charAt(i));
//			}
//		}
//		//return
//		return resultString;
//	}
//	public function removeThaiStyle(target:String, inputThaiStyle:Object):String {
//		//default
//		if (target == undefined) {
//			target = new String(_value);
//		}
//		if (inputThaiStyle == undefined) {
//			inputThaiStyle = thaiStyle
//		}
//		//trace("setFontFamily : "+inputThaiStyle.fontFamily);
//		//trace(inputThaiStyle.fontFamily == undefined)
//		if (inputThaiStyle.fontFamily == undefined) {
//			inputThaiStyle.fontFamily = "psl_ad";
//		}
//		//result                
//		//setFontFamily
//		
//		//trace("fontFamily : "+inputThaiStyle.fontFamily);
//		//trace("outputType : "+inputThaiStyle.outputType);
//		
//		setFontFamily(inputThaiStyle.fontFamily);
//		//prepare asciiToUnicode
//		if (inputThaiStyle.fontFamily == "psl_ad") {
//			switch (inputThaiStyle.outputType) {
//			case "multibyte_cs" :
//				target = removeCSChr(target);
//				//trace("asciiToUnicode : "+inputThaiStyle.fontFamily);
//				yoyingPS7 = asciiToUnicode(yoyingPS7);
//				yoyingNoTail = asciiToUnicode(yoyingNoTail);
//				thothanNoTail = asciiToUnicode(thothanNoTail);
//				//is that fixed? highTone,lowTone,lowerleftTone,lowerleftVowel,lowBottomVowel,aumCombo
//				highTone = asciiToUnicode(highTone);
//				lowTone = asciiToUnicode(lowTone);
//				lowerleftTone = asciiToUnicode(lowerleftTone);
//				lowerleftVowel = asciiToUnicode(lowerleftVowel);
//				lowBottomVowel = asciiToUnicode(lowBottomVowel);
//				aumCombo[0] = asciiToUnicode(aumCombo[0]);
//				aumCombo[1] = asciiToUnicode(aumCombo[1]);
//				break;
//			case "ascii_ps7" :
//				target = removePS7Chr(target);
//				//trace("removePS7Chr : "+target);
//				break;
//			}
//		}
//		//result       
//		var resultString = "";
//		for (var i = 0; i<target.length; i++) {
//			//char by char
//			var char = target.charAt(i);
//			//trace("char : "+char);
//			//trace("charCodeAt : "+Number(target.charCodeAt(i)));
//			//is that fixed? yoying,thothan
//			var yoyingPS7Pos = yoyingPS7.indexOf(char);
//			var yoyingNoTailPos = yoyingNoTail.indexOf(char);
//			var thothanNoTailPos = thothanNoTail.indexOf(char);
//			//is that fixed? highTone,lowTone,lowerleftTone,lowerleftVowel,lowBottomVowel,aumCombo
//			var highTonePos = highTone.indexOf(char);
//			var lowTonePos = lowTone.indexOf(char);
//			//trace("lowTone : "+lowTone);
//			//trace("lowTonePos : "+lowTonePos);
//			var lowerleftTonePos = lowerleftTone.indexOf(char);
//			var lowerleftVowelPos = lowerleftVowel.indexOf(char);
//			var lowBottomVowelPos = lowBottomVowel.indexOf(char);
//			var aumComboPos_0 = aumCombo[0].indexOf(char);
//			var aumComboPos_1 = aumCombo[1].indexOf(char);
//			//unfix : yoying,thothan
//			if (yoyingPS7Pos>-1) {
//				char = yoying.charAt(0);
//			}
//			if (yoyingNoTailPos>-1) {
//				char = yoying.charAt(0);
//			}
//			if (thothanNoTailPos>-1) {
//				char = thothan.charAt(0);
//			}
//			//unfix : tone,vowel,bottomVowel,aum                
//			if (highTonePos>-1) {
//				char = tone.charAt(highTonePos);
//			}
//			if (lowTonePos>-1) {
//				char = tone.charAt(lowTonePos);
//			}
//			if (lowerleftTonePos>-1) {
//				char = tone.charAt(lowerleftTonePos);
//			}
//			if (lowerleftVowelPos>-1) {
//				char = vowel.charAt(lowerleftVowelPos);
//			}
//			if (lowBottomVowelPos>-1) {
//				char = bottomVowel.charAt(lowBottomVowelPos);
//			}
//			if ((aumComboPos_0>-1) && (aumComboPos_1>-1) && ((aumComboPos_1-aumComboPos_0) == 1)) {
//				char = aum.charAt(aumComboPos_0);
//			}
//			//grab                
//			resultString += char;
//		}
//		if (inputThaiStyle.fontFamily != "psl_ad") {
//			switch (inputThaiStyle.outputType) {
//			case "multibyte_cs" :
//				resultString = removeCSChr(resultString);
//				break;
//			case "ascii_ps7" :
//				//need fix
//				//trace("removePS7Chr : "+resultString);
//				resultString = removePS7Chr(resultString);
//				
//				//trace("removePS7Chr : "+resultString);
//				break;
//			}
//		}
//		//return     
//		return resultString;
//	}
	private String fixFloat(String target) {
//		//default
//		if (target == undefined) {
//			var target = new String(_value);
//		}
		//result                
		String resultString = "";

		for (int i = 0; i<target.length(); i++) {
			//char by char
			char c = target.charAt(i);
			//---------------------------long tail	
			Boolean isAfterLongTail  =false;
			Boolean isAfter2LongTail = false;
			if(i>=1){
				isAfterLongTail = (longTail.indexOf(target.charAt(i-1))>-1);
				if(i>=2){
					isAfter2LongTail = (longTail.indexOf(target.charAt(i-2))>-1);
				}
			}
//			 isAfterLongTail = (longTail.indexOf(target.charAt(i-1))>-1) && (i>=1);
//			 isAfter2LongTail = (longTail.indexOf(target.charAt(i-2))>-1) && (i>=2);
			//---------------------------vowel		
			///is that  vowel?
			int vowelPos = vowel.indexOf(c);
			//lowerleftVowel = สระ = ปิปีปึปืปั
			if (thaiStyle.isFixTail && (vowelPos>-1) && isAfterLongTail) {
				c = lowerleftVowel.charAt(vowelPos);
			}
			//is that bottomVowel?                
			int bottomVowelPos = bottomVowel.indexOf(c);
			//---------------------------tone		
			//is that tone?
			int tonePos = tone.indexOf(c);
			//skip newline
			Boolean isNewline = (c == newline);
			//tone found
			if (!isNewline && thaiStyle.isFixFloat && (tonePos>-1)) {
				//previous char is tone
				Boolean isAfterTone = false;
				if(i>=1){
					isAfterTone = (tone.indexOf(target.charAt(i-1))>-1);
				}
				//next is aum?
				Boolean isBeforeAum = false;
				if(i+1<target.length()){
					isBeforeAum = (aum.indexOf(target.charAt(i+1))>-1);
				}
				//prev is vowel?
				//prev is vowel?
				Boolean isAfterVowel = false;
				try{
					isAfterVowel = (vowel.indexOf(target.charAt(i-1))>-1)&&(i>=1);
				}catch (Exception e) {
					// TODO: handle exception
				}
				//prev is lowvowel?
				Boolean isAfterLowVowel;// = (lowerleftVowel.indexOf(target.charAt(i-1))>-1) && (i>=1);
				
				isAfterLowVowel = false;
				
				try{
					isAfterLowVowel = (lowerleftVowel.indexOf(target.charAt(i-1))>-1) && (i>=1);;
				}catch (Exception e) {
					// TODO: handle exception
				}
				if (!isAfterTone && !isBeforeAum && !isAfterVowel) {
					if (thaiStyle.isFixTail && isAfterLongTail) {
						//avoid tail by move high tone to lower left 
						c = lowerleftTone.charAt(tonePos);
					} else {
						//just pull 'em down
						c = lowTone.charAt(tonePos);
					}
				}
				if (thaiStyle.isFixTail) {

					Boolean isAfterBottomVowel = false;
					try{
						isAfterBottomVowel = (bottomVowel.indexOf(target.charAt(i-1))>-1)&& (i>=1);
					}catch (Exception e) {
						// TODO: handle exception
					}
					if (isAfter2LongTail && (isAfterVowel || isAfterLowVowel)) {
						//highTone = ปิ่ปิ้ปิ๊ปิ๋
						c = highTone.charAt(tonePos);
					} else if (isAfter2LongTail && isAfterBottomVowel) {
						//bottomVowel = ปุ่ปุ้ปุ๊ปุ๋
						c = lowerleftTone.charAt(tonePos);
					} else if (isAfterLongTail && isBeforeAum) {
						//highTone = ป่ำป้ำป๊ำป๋ำ      
						c = highTone.charAt(tonePos);
					}
				}
			}
			//---------------------------bottomVowel                
			//is that bottomVowel?
			//var bottomVowelPos = bottomVowel.indexOf(char);
			Boolean isAfterLowTail = false;
			if(i>=1){
				isAfterLowTail = (lowTail.indexOf(target.charAt(i-1))>-1);
			}
			//�?ุ�?ู�?ฺ	
			if ((bottomVowelPos>-1) && isAfterLowTail && thaiStyle.isFixLowTail) {
				c = lowBottomVowel.charAt(bottomVowelPos);
			}
			//---------------------------aum		                
			//is that aum?
			Boolean isAum = (aum.indexOf(c)>-1);
			Boolean isAfterTone = false;
			if(i>=1){
				isAfterTone = (tone.indexOf(target.charAt(i-1))>-1);
				}
			Boolean isAfterLongTailTone = isAfter2LongTail && isAfterTone;
			//ปำฟำ�?ำ,ป่ำป้ำป๊ำป๋ำ
			if (thaiStyle.isFixAum && isAum && (isAfterLongTail || isAfterLongTailTone)) {
				//break aum
				c = aumCombo.charAt(0);
			}
			//---------------------------grab                
			resultString += c;
		}
		//return
		return resultString;
	}
//	private function replace(target:String, source_chr:String, replace_chr:String):String {
//		//default
//		if (target == undefined) {
//			var target = new String(_value);
//		}
//		if (source_chr == undefined) {
//			var source_chr = "";
//		}
//		if (replace_chr == undefined) {
//			var replace_chr = "";
//		}
//		//result                
//		var resultString = target.split(source_chr).join(replace_chr);
//		//return
//		return resultString;
//	}
	public String applyThaiStyle(String target){
		//default
		if (target == null) {
			target = "";
		}
		//result                
		String resultString = "";
		//fix Yoying
		target = fixYoYing(target);
		//fix Float
		target = fixFloat(target);
		//outputType : multibyte_cs, ascii_ps7, multibyte
		if(thaiStyle.outputType.equalsIgnoreCase("multibyte_cs")){
			resultString = csChr(target);
		}else if(thaiStyle.outputType.equalsIgnoreCase("ascii_ps7")){
			resultString = ps7Chr(target);
		}else{
			resultString = target;
		}
		
		//fix newline (0x0D0A->0x0A)
		if (thaiStyle.isFixLineFeed) {
//			resultString = replace(resultString, (char)(0x000D), (char)(0x000A));
			resultString = resultString.replace((char)(0x000D), (char)(0x000A));
		}
		//else {          
		//resultString = replace(resultString, (char)(0x000D), (char)(0x000D)+(char)(0x000A));
		//}
		//return    
		return resultString;
	}
	private void setFontFamily(String fontFamily) {
		if (thaiStyle.fontFamily == "") {
			thaiStyle.fontFamily = "psl_ad";
		} else {
			thaiStyle.fontFamily = fontFamily;
		}
		
		if(fontFamily.equalsIgnoreCase("ds")){
			//ย�?เว้น อ่อ้อ๊อ๋อ์
			//highTone = วรรณยุ�?ต์ = ป่ำป้ำป๊ำป๋ำ
			highTone = ""+(char)(0x203A)+(char)(0x0153)+(char)(0x009D)+(char)(0x009D)+(char)(0xF717)+(char)(0x2122)+(char)(0x0161);
			//lowTone
			lowTone = ""+(char)(0x2039)+(char)(0x0152)+(char)(0x008D)+(char)(0x008E)+(char)(0x008F)+(char)(0x0E4D)+(char)(0x0E47);
			//วรรณยุ�?ต์ = ป่ป้ป๊ป๋ป์ป�?ป็
			lowerleftTone = ""+(char)(0x2020)+(char)(0x2021)+(char)(0x02C6)+(char)(0x2030)+(char)(0x0160)+(char)(0x2122)+(char)(0x0161);
			//สระซ้าย = ปิปีปึปืปั
			lowerleftVowel = ""+(char)(0x00D4)+(char)(0x00D5)+(char)(0x00D6)+(char)(0x00D7)+(char)(0x02DC);
			//สระล่าง = �?ุ�?ู�?ฺ
			lowBottomVowel = ""+(char)(0x00FC)+(char)(0x00FD)+(char)(0x00FE);
			//aum = สระอำ
			aumCombo = ""+(char)(0x2122)+(char)(0x00D2);
			//ย�?เว้น อ่อ้อ๊อ๋อ์,ป่ป้ป๊ป๋ป์ป�?ป็,ป่ำป้ำป๊ำป๋ำ,อำ
			escapeChar = ""+(char)(0x2039)+(char)(0x0152)+(char)(0x008D)+(char)(0x008E)+(char)(0x008F)+lowerleftTone+highTone+(char)(0x00D2);
			//�?,�?
			yoyingNoTail = ""+(char)(0x0090);
			thothanNoTail = ""+(char)(0x0080);
		}else if(fontFamily.equalsIgnoreCase("psl_ad")){
			//highTone = วรรณยุ�?ต์ = ป่ำป้ำป๊ำป๋ำ
			highTone = ""+(char)(0x009B)+(char)(0x009C)+(char)(0x009D)+(char)(0x009E)+(char)(0x009F)+(char)(0x0E4D)+(char)(0x0E47);
			//lowTone
			lowTone = ""+(char)(0x008B)+(char)(0x008C)+(char)(0x008D)+(char)(0x008E)+(char)(0x008F)+(char)(0x0E4D)+(char)(0x0E47);
			//tone = วรรณยุ�?ต์ = ป่ป้ป๊ป๋ป์ป�?ป็
			lowerleftTone = ""+(char)(0x0086)+(char)(0x0087)+(char)(0x0088)+(char)(0x0089)+(char)(0x008A)+(char)(0x0099)+(char)(0x009A);
			//lowerleftVowel = สระ = ปิปีปึปืปั
			lowerleftVowel = ""+(char)(0x0081)+(char)(0x0082)+(char)(0x0083)+(char)(0x0084)+(char)(0x0098);
			//bottomVowel = สระล่าง = �?ุ�?ู�?ฺ
			lowBottomVowel = ""+(char)(0xF0FC)+(char)(0xF0FD)+(char)(0xF0FE);
			//aum = สระอำ
			aumCombo = ""+(char)(0x0099)+(char)(0x0E32);
			//�?,�?
			yoyingNoTail = ""+(char)(0xF090);
			thothanNoTail = ""+(char)(0xF080);
		}else if(fontFamily.equalsIgnoreCase("psl_sp")){
			//highTone = วรรณยุ�?ต์ = ป่ำป้ำป๊ำป๋ำ
			highTone = ""+(char)(0xF713)+(char)(0xF714)+(char)(0xF715)+(char)(0xF716)+(char)(0xF717)+(char)(0x0E4D)+(char)(0x0E47);
			//lowTone
			lowTone = ""+(char)(0xF70A)+(char)(0xF70B)+(char)(0xF70C)+(char)(0xF70D)+(char)(0xF70E)+(char)(0x0E4D)+(char)(0x0E47);
			//วรรณยุ�?ต์ = ป่ป้ป๊ป๋ป์ป�?ป็
			lowerleftTone = ""+(char)(0xF705)+(char)(0xF706)+(char)(0xF707)+(char)(0xF708)+(char)(0xF709)+(char)(0xF711)+(char)(0xF712);
			//สระซ้าย = ปิปีปึปืปั
			lowerleftVowel = ""+(char)(0xF701)+(char)(0xF702)+(char)(0xF703)+(char)(0xF704)+(char)(0xF710);
			//สระล่าง = �?ุ�?ู�?ฺ
			lowBottomVowel = ""+(char)(0xF718)+(char)(0xF719)+(char)(0xF71A);
			//aum = สระอำ
			aumCombo = ""+(char)(0xF711)+(char)(0x0E32);
			//�?,�?
			yoyingNoTail = ""+(char)(0xF70F);
			thothanNoTail = ""+(char)(0xF700);
		}
		
	}
	public void setThaiStyle(ThaiStyle inputThaiStyle) {
		//--------default style
		//fontFamily
//		thaiStyle.fontFamily = "psl_ad";
//		thaiStyle.fontFamily = "ds";
		thaiStyle.fontFamily = "psl_sp";
		//fix
		thaiStyle.isFixFloat = true;
		thaiStyle.isFixTail = true;
		thaiStyle.isFixAum = true;
		thaiStyle.isFixLowTail = true;
		thaiStyle.isFixYoying = false;
		thaiStyle.isFixYoyingTail = true;
		thaiStyle.isFixLineFeed = false;
		//outputType : multibyte, multibyte_cs, ascii_ps7
		//thaiStyle.outputType = "multibyte_cs";
		//--------custom style
//		if (inputThaiStyle != undefined) {
//			for (var i in inputThaiStyle) {
//				thaiStyle[i] = inputThaiStyle[i];
//			}
//		}
	}
	
	//-----------------constructor
//	public Thai(String inputString, ThaiStyle inputThaiStyle) {
		public Thai(String inputString) {
//		Log.e("Thai", "Thai.as version 1.3.3");
		//------------------------i hate CodePage ;(
		//System.useCodepage = true;
		//------------------------set style
//		setThaiStyle(inputThaiStyle);
		setThaiStyle(null);
		//------------------------set table
		setFontFamily(thaiStyle.fontFamily);
		//inputType : ascii -> multibyte,
		//------------------------unicode mapping table
		//วรรณยุ�?ต์ = อ่อ้อ๊อ๋อ์อ�?อ็
		tone = ""+(char)(0x0E48)+(char)(0x0E49)+(char)(0x0E4A)+(char)(0x0E4B)+(char)(0x0E4C)+(char)(0x0E4D)+(char)(0x0E47);
		//สระ = อิอีอึอือั
		vowel = ""+(char)(0x0E34)+(char)(0x0E35)+(char)(0x0E36)+(char)(0x0E37)+(char)(0x0E31);
		//สระล่าง = อุอูอฺ
		bottomVowel = ""+(char)(0x0E38)+(char)(0x0E39)+(char)(0x0E3A);
		//สระอำ
		aum = ""+(char)(0x0E33);
		//ป�?ฟ
		longTail = ""+(char)(0x0E1B)+(char)(0x0E1D)+(char)(0x0E1F);
		//�?�?ฎ�?�?ฤฦ
		lowTail = ""+(char)(0xF0AD)+(char)(0x0E0D)+(char)(0x0E0E)+(char)(0x0E0F)+(char)(0x0E10)+(char)(0x0E24)+(char)(0x0E26);
		//�?,�?
		yoying = ""+(char)(0x0E0D);
		yoyingPS7 = ""+(char)(0xF0AD);
		thothan = ""+(char)(0x0E10);
		//------------------------ascii to unicode
		//***have to add to style for ascii input
//		inputString = asciiToUnicode(inputString);
		//------------------------return
		if (inputString != "") {
			set(applyThaiStyle(inputString));
		} else {
			set("");
		}
	}
	
	int Number(char c){
		
		return Integer.parseInt(Integer.toString(c | 0x10000).substring(1));
	}
}
