package com.cam.libstools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.libs.deviceinfo.Deviceinfo;

import java.io.File;
import java.io.FileOutputStream;
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

            Toast.makeText(activity.getApplicationContext(),"File is Saved in  " + filename, 1000).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {


            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(degrees);
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                throw ex;
            }
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

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        bm.recycle();

        return resizedBitmap;
    }
}
