package com.cam.libstools;

import java.util.ArrayList;
import java.util.HashMap;



public class DeviceData {
	
	ArrayList<HashMap<String, String>> hashdevice=new ArrayList<HashMap<String,String>>();

	
	
	public DeviceData() {
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("MODEL", "LG-P880");
		map.put("orentation","portrait");
		map.put("setcamorentation","90");
		map.put("previewsizeofwidth","720");
		map.put("previewsizeofheight","1184");
		map.put("statusbarheight","96");

		hashdevice.add(map);
		
		map=new HashMap<String, String>();
		map.put("MODEL", "GT-S5830");
		map.put("orentation","portrait");
		map.put("setcamorentation","90");
		map.put("previewsizeofwidth","960");
		map.put("previewsizeofheight","1265");
		map.put("statusbarheight","25");
		hashdevice.add(map);

        map=new HashMap<String, String>();
        map.put("MODEL", "GT-S5830");
        map.put("orentation","portrait");
        map.put("setcamorentation","90");
        map.put("previewsizeofwidth","960");
        map.put("previewsizeofheight","1265");
        map.put("statusbarheight","25");
        hashdevice.add(map);

        map=new HashMap<String, String>();
        map.put("MODEL", "GT-P3100");
        map.put("orentation","portrait");
        map.put("setcamorentation","90");
        map.put("previewsizeofwidth","960");
        map.put("previewsizeofheight","1265");
        map.put("statusbarheight","25");
        hashdevice.add(map);
	}


	public String [] getDeviceCamSetting(String Model){
		String []deviceinfo=new String[6];
		for(int i=0;i<hashdevice.size();i++){
			if(hashdevice.get(i).get("MODEL").equals(Model)){
				deviceinfo[0]=hashdevice.get(i).get("MODEL");
				deviceinfo[1]=hashdevice.get(i).get("orentation");
				deviceinfo[2]=hashdevice.get(i).get("setcamorentation");
				deviceinfo[3]=hashdevice.get(i).get("previewsizeofwidth");
				deviceinfo[4]=hashdevice.get(i).get("previewsizeofheight");
				deviceinfo[5]=hashdevice.get(i).get("statusbarheight");
				break;
			}
		}
		return deviceinfo;
		
	}
}
