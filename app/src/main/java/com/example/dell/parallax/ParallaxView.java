package com.example.dell.parallax;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

public class ParallaxView extends SurfaceView implements Runnable {

    ArrayList<Background> backgrounds;

    private volatile boolean running;
    private Thread gameThread = null;

    //drawing declaration
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    Context context;

    long fps = 60;

    int screenWidth;
    int screenHeight;

    ParallaxView(Context context, int screenHeight, int screenWidth){
        super(context);

        this.context = context;

        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        ourHolder = getHolder();
        paint = new Paint();

        //init array list
        backgrounds = new ArrayList<>();

        //load background data
        backgrounds.add(new Background(this.context, screenWidth, screenHeight, "bg", 0, 80, 50));
        backgrounds.add(new Background(this.context, screenWidth, screenHeight, "grass", 70, 110, 200));
        //add background here
    }

    private void drawBackground(int position){
        Background bg = backgrounds.get(position);

        //for regular bitmap
        Rect fromRect1 = new Rect(0,0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        //for reversed bitmap
        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.width, bg.endY);

        //draw two background bitmap
        if(!bg.reversedFirst){
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        }else{
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }
    }

    @Override
    public  void run(){
        while (running){
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();

            //calculate fps
            long timeThisFrame = System.currentTimeMillis()-startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update(){
        //update bg position
        for(Background bg : backgrounds){
            bg.update(fps);
        }
    }

    private void draw(){
        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 3, 70));

            //draw bg paralax
            drawBackground(0);

            //draw rest of game
            paint.setTextSize(50);
            paint.setColor(Color.argb(255, 255, 128, 64));
            canvas.drawText("Tugas Parallax", 400, screenHeight /100*5, paint);
            paint.setTextSize(300);
            canvas.drawText("Ini Text", 50, 400, paint);

            //draw fg parallax
            drawBackground(1);

            //unlock scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    //clean thread
    public void pause(){
        running = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){
            //error condition
        }
    }

    //create new thread
    public void resume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}
