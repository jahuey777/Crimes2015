package com.example.jaimejahuey.criminalintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by jaimejahuey on 5/27/16.
 */
public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        //Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcrHeight = options.outHeight;

        //Figure out how much to scale down by
        int inSampleSize = 1;
        if(srcrHeight>destHeight || srcWidth> destWidth){
            if(srcWidth> srcrHeight){
                inSampleSize = Math.round(srcrHeight/destHeight);
            } else{
                inSampleSize = Math.round(srcWidth/destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read in and create final bitmap
        return BitmapFactory.decodeFile(path, options);
    }
}
