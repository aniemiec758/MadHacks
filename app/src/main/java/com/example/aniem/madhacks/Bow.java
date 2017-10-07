package com.example.aniem.madhacks;

import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.view.View.OnClickListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Bow extends AppCompatActivity {

    private ProgressBar progBow;
    private View bow;
    private Button button;
    private int numArrows; // number of arrows being shot at once
    private int duration; // cooldown timer duration
    private int upProg; // what the progressBar increments by as it cools down

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bow);

        numArrows = 1; // start out one arrow at a time

        /**/ // set onered to be onegreen via visibility exploits

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks screen to be "portrait"

        // create images
        bow = findViewById(R.id.Bow);
        bow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progBow.getVisibility() != ProgressBar.VISIBLE) { // progress bar isn't cooling down
                    /**/ // send gyro data to computer, along with numArrows
                    barCool(); // activates cooldown timer
                }
            }
        });

        // making the bow cooldown bar
        progBow = (ProgressBar)findViewById(R.id.bowProg);
        progBow.setVisibility(ProgressBar.INVISIBLE); // makes it invisible at first
        progBow.setProgress(0);
    }

    private void barCool() {
        progBow.setVisibility(ProgressBar.VISIBLE); // player can see the progBar
        /**/ // set different cases for one arrow, two arrows, etc
        if (numArrows == 2) { // two arrows being shot at once
            duration = 2600; // duration of timer
            upProg = 4; // what the progress bar increments by
        } else if (numArrows == 3) { // three arrows being shot at once
            duration = 3100;
            upProg = 3;
        } else { // just one arrow, default
            duration = 2100;
            upProg = 5;
        }

        new CountDownTimer(duration, 100) { // filling up the bar until completion
            @Override
            public void onTick(long millisUntilFinished) { progBow.setProgress((progBow.getProgress()+upProg)); } // fills bar
            @Override
            public void onFinish() { // when it's filled
                progBow.setVisibility(ProgressBar.INVISIBLE); // goes back to being invisible
                progBow.setProgress(0); // resets bar
            }
        }.start();
    }

    private void setNumArrows(int n) { // changes it to be one, two, or three arrows in the quiver
        numArrows = n;
    } /**/ // voice commands
}