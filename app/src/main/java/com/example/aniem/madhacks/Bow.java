package com.example.aniem.madhacks;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Bow extends AppCompatActivity {

    private View bow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bow);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // locks screen to be "portrait"

        // create images
        bow = findViewById(R.id.Bow);
        //bow.setTranslationY(Resources.getSystem().getDisplayMetrics().widthPixels / 2 - (bow.getWidth()/2)); // centers width
        //bow.setTranslationX(Resources.getSystem().getDisplayMetrics().heightPixels / 2 - (bow.getHeight()/2)); // centers height
        bow.setTranslationX(0);
        bow.setTranslationY(0);
    }
}
