package com.cam.libstools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.util.Log;

import com.libs.deviceinfo.Deviceinfo;
import com.libs.systemtools.PrintLog;

import java.util.List;


public class CamInitSetting {
private String 		TAG			=		"CamInitSetting";
private Camera 		cam;
private Activity 	activity;
private DeviceData deviceData;
private String []devicearraydata;
public CamInitSetting(Camera cam,Activity activity) {
	try{
		this.activity=activity;
		this.cam = cam;
        deviceData=new DeviceData();
        devicearraydata=deviceData.getDeviceCamSetting(Deviceinfo.BUILD_MODEL);
        setCameraDisplaySetting();
	}catch (Exception e) {
		Log.e(TAG,"cam is null",e);
	}
}

public void setCameraDisplaySetting(){
	////Set Orentation
	cam.setDisplayOrientation(Integer.parseInt(devicearraydata[2]));
	////Set Camera Preview
	Parameters param=cam.getParameters();
	param.setPreviewSize(Integer.parseInt(devicearraydata[4]), Integer.parseInt(devicearraydata[3]));
	cam.setParameters(param);
	PrintLog.print(activity.getApplicationContext(), Deviceinfo.BUILD_MODEL);
	PrintLog.print(activity.getApplicationContext(),Deviceinfo.getDeviceScreenOrentation(activity));
	
}
public void playAutoFocus(boolean isFocus){
	if(isFocus){
		cam.autoFocus(new AutoFocusCallback() {@Override public void onAutoFocus(boolean success, Camera camera) {}});
	}else{
		cam.cancelAutoFocus();
	}
}

public void playFlash(boolean isFlash){
    if(Deviceinfo.getFlashAvailableOnDevice(activity)){
    Parameters param=cam.getParameters();
    List<String> supportedFlashModes = param.getSupportedFlashModes();
    PrintLog.print(activity.getApplicationContext(),supportedFlashModes.toString());
    //For Support Mode Torch
    if(supportedFlashModes != null && supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)){
        if(isFlash){
            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
        else{
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        cam.setParameters(param);
    }else{
        //For unSupport Mode Torch
        if(isFlash){
            if(devicearraydata[0].equals("GT-S5830")){
                param.setFlashMode(Parameters.FLASH_MODE_ON);
                cam.setParameters(param);
                cam.autoFocus(new Camera.AutoFocusCallback() {public void onAutoFocus(final boolean success, Camera camera) { }});
                cam.startPreview();
                param =cam.getParameters();
                param.setFlashMode(Parameters.FLASH_MODE_OFF);
                cam.setParameters(param);
            }
        }else{
            if(devicearraydata[0].equals("GT-S5830")){
                param.setFlashMode(Parameters.FLASH_MODE_ON);
                cam.setParameters(param);
                cam.autoFocus(new Camera.AutoFocusCallback() {public void onAutoFocus(final boolean success, Camera camera) { }});
                cam.startPreview();
            }
        }
    }
    }else{
       PrintLog.print(activity.getApplicationContext(),"Not Found Flash On Device.");
    }
}
    public void takePicture(){
        cam.takePicture(null,null,new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImagesIO.writeImage(activity,bmp, Integer.parseInt(devicearraydata[2]));

            }
        });

    }


}
