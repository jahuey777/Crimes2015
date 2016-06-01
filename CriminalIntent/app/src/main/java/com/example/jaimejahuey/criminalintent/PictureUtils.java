package com.example.jaimejahuey.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Point;

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
        //inSampleSize, this is how big each sample should be for each pixel.
        //As sample size of 1 has one final horizontal pixel for each horizontal pixel in the orignial file
        //A sample size of 2 has 1 hor. pix. for every 2 hor. pix. in the original file.
        //So if its 2, then the image has a quarter of the number of pixels of the original



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

    //Checks how big the screen is and then scales the image down to that size
    //this is conservative since the imageview we load into is always smaller than this size,
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }
}
