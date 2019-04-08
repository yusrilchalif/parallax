package com.example.dell.parallax;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    private ParallaxView parallaxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get a display object to access scree
        Display display = getWindowManager().getDefaultDisplay();

        //load resolution into a point object
        Point resolution = new Point();
        display.getSize(resolution);

        //and finally set the view for our game
        parallaxView = new ParallaxView(this, resolution.x, resolution.y);
        setContentView(parallaxView);
    }

    //pause thread
    @Override
    protected void onPause(){
        super.onPause();
        parallaxView.pause();
    }

    //resume thread
    @Override
    protected void onResume(){
        super.onResume();
        parallaxView.resume();
    }
}
