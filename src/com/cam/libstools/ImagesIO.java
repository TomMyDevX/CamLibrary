package com.cam.libstools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import com.libs.deviceinfo.Deviceinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TomMy on 6/6/13.
 */
public class ImagesIO {
public static void writeImage(Activity activity,Bitmap bmImg,int degree) {

        File filename;
        try {
            bmImg=getResizedBitmap(bmImg, Deviceinfo.getDeviceScreenSize(activity)[1]);
            bmImg=rotate(bmImg,degree);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            String path = Environment.getExternalStorageDirectory().toString();
            new File(path + "/"+activity.getPackageName()).mkdirs();
            filename = new File(path + "/"+activity.getPackageName()+"/"+currentDateandTime+".jpg");
            FileOutputStream out = new FileOutputStream(filename);
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(activity.getContentResolver(),filename.getAbsolutePath(), filename.getName(),filename.getName());
            Log.e("IMG ERROR","File:"+filename);
          //  Toast.makeText(activity.getApplicationContext(),"File is Saved in  " + filename, 1000).show();
        } catch (Exception e) {
           Log.e("IMG ERROR","File Save ERROR",e);
        }

    }

    public static String  writeImageWithReturnURL(Activity activity,Bitmap bmImg,int degree,boolean isFilp,boolean isCenterCrop,boolean isResize,boolean isRotate) {
        File filename;
        try {
            bmImg=isResize?getResizedBitmap(bmImg, Deviceinfo.getDeviceScreenSize(activity)[1]):bmImg;
            bmImg=isRotate?rotate(bmImg,degree):bmImg;
            bmImg=isFilp?flip(bmImg):bmImg;
            bmImg=isCenterCrop?scaleCenterCrop(bmImg,Deviceinfo.getDeviceScreenSize(activity)[1],Deviceinfo.getDeviceScreenSize(activity)[0]):bmImg;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            String path = Environment.getExternalStorageDirectory().toString();
            new File(path + "/"+activity.getPackageName()).mkdirs();
            filename = new File(path + "/"+activity.getPackageName()+"/"+currentDateandTime+".jpg");
            FileOutputStream out = new FileOutputStream(filename);
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(activity.getContentResolver(),filename.getAbsolutePath(), filename.getName(),filename.getName());
            Log.e("ERROR","File:"+filename);
         //   Toast.makeText(activity.getApplicationContext(),"File is Saved in  " + filename, 1000).show();
            return filename.getAbsolutePath();
        } catch (Exception e) {
            Log.e("IMG ERROR","File Save ERROR",e);
            return "";
            //xxx

        }

    }
    public static String  writeImageTempNonExtensionWithReturnURL(Activity activity,Bitmap bmImg,int degree,boolean isFilp,boolean isCenterCrop,boolean isResize,boolean isRotate) {
        File filename;
        try {
            bmImg=isResize?getResizedBitmap(bmImg, Deviceinfo.getDeviceScreenSize(activity)[1]):bmImg;
            bmImg=isRotate?rotate(bmImg,degree):bmImg;
            bmImg=isFilp?flip(bmImg):bmImg;
            bmImg=isCenterCrop?scaleCenterCrop(bmImg,Deviceinfo.getDeviceScreenSize(activity)[1],Deviceinfo.getDeviceScreenSize(activity)[0]):bmImg;
         //   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
         //   String currentDateandTime = sdf.format(new Date());

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Temp");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {

                    return null;
                }
            }
            deletefile(mediaStorageDir.getPath() + File.separator + "IMG");
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG");


          //  String path = Environment.getExternalStorageDirectory().toString();
          //  new File(path + "/"+activity.getPackageName()).mkdirs();
          //  filename = new File(path + "/"+activity.getPackageName()+"/"+currentDateandTime);
            FileOutputStream out = new FileOutputStream(mediaFile);
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();


           // MediaStore.Images.Media.insertImage(activity.getContentResolver(),filename.getAbsolutePath(), filename.getName(),filename.getName());
            Log.e("ERROR","File:"+mediaFile.getAbsolutePath());
            //   Toast.makeText(activity.getApplicationContext(),"File is Saved in  " + filename, 1000).show();
            return mediaFile.getAbsolutePath();
        } catch (Exception e) {
            Log.e("IMG ERROR","File Save ERROR",e);
            return "";

        }

    }
    public static File getPathIO(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Temp");
        return  new File(mediaStorageDir.getPath() + File.separator + "IMG");
    }
    public static String getImagesIOPath(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Temp");
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG.jpg");
        FileOutputStream out = null;
        try {
            Bitmap bmImg= BitmapFactory.decodeFile(mediaStorageDir.getPath() + File.separator + "IMG");
            out = new FileOutputStream(mediaFile);
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.e("com.clicknect.apps.android.nimonyim","Write File to Share.");
        } catch (FileNotFoundException e) {
                Log.e("com.clicknect.apps.android.nimonyim","File not found.");
        }catch (IOException io){
            Log.e("com.clicknect.apps.android.nimonyim","IOException.");
        }
        return  mediaFile.getAbsolutePath();
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth)
    {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        //get the resulting size after scaling
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        //figure out where we should translate to
        float dx = (newWidth - scaledWidth) / 2;
        float dy = (newHeight - scaledHeight) / 2;

        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postTranslate(dx, dy);
        canvas.drawBitmap(source, matrix, null);
        return dest;
    }
    public static boolean deletefile(String FilePath){
        File file = new File(FilePath);
        boolean delstate=false;
        if(file.delete()){
            Log.e("Delete","Delete File|"+FilePath);
            delstate=true;
        }else{
            delstate=false;
        }
        return delstate;
    }

    public static Bitmap flip(Bitmap d)
    {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap src = d;
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {


//            try {
                Matrix matrix = new Matrix();
               // matrix.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
                matrix.postRotate(degrees);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
//                if (b != b2) {
//                    b.recycle();
//                    b = b2;
//                }
//            } catch (OutOfMemoryError ex) {
//                Log.e("IMG ERROR","File RORATE ERROR",ex);
//            }
        }
        return b;
    }
    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float aspect = (float)width / height;

        float scaleWidth = newWidth;

        float scaleHeight = scaleWidth / aspect;        // yeah!

        // create a matrix for the manipulation

        Matrix matrix = new Matrix();

        // resize the bit map

        matrix.postScale(scaleWidth / width, scaleHeight / height);

        // recreate the new Bitmap

        bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
//
//        bm.recycle();
//        if (bm != resizedBitmap)
//            bm.recycle();
//              bm = null;

        return bm;
    }


    public static void manageAlbum(Context context,String filename){
        String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path, "/nimonyim apps/");
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        File file = new File(filename);
        String imagePath =  file.getAbsolutePath();
        MediaScannerConnection.scanFile(context,new String[]{imagePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                      //  if (Config.LOG_DEBUG_ENABLED) {
                            Log.e("Class", "scanned : " + path);
                      //  }
                    }
                });
    }
}
