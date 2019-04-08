package com.example.dell.parallax;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Background {
    Bitmap bitmap;
    Bitmap bitmapReversed;

    int width;
    int height;
    boolean reversedFirst;
    float speed;

    int xClip;
    int startY;
    int endY;

    Background(Context context, int screenWidth, int screenHeight, String bitmapName, int sY, int eY, float s){
        int resID = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());

        //load bitmap using id
        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        reversedFirst = false;

        //init animation

        //where to clip the bitmap
        xClip = 0;

        //position background vertical
        startY = sY * (screenHeight /100);
        endY = eY * (screenHeight/100);
        speed = s;

        //create bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, (endY - startY), true);

        //save scale for late use
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        //create mirror image
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        bitmapReversed = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
    }

    void update(long fps){
        //move clip position
        xClip -= speed/fps;
        if(xClip >= width){
            xClip = 0;
            reversedFirst = !reversedFirst;
        }else if(xClip <= 0){
            xClip = width;
            reversedFirst = !reversedFirst;
        }
    }
}
