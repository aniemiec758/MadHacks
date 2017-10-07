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

    // 1x, 2x, 3x images
    private View onered; private View onegreen;
    private View twored; private View twogreen;
    private View threered; private View threegreen;

    // globar variables
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

        // 1x, 2x, 3x images
        onered = findViewById(R.id.onered); onegreen = findViewById(R.id.onegreen);
        twored = findViewById(R.id.twored); twogreen = findViewById(R.id.twogreen);
        threered = findViewById(R.id.threered); threegreen = findViewById(R.id.threegreen);
        // visibilities
        onered.setVisibility(View.INVISIBLE); onegreen.setVisibility(View.VISIBLE); // starts out as the default
        twored.setVisibility(View.VISIBLE); twogreen.setVisibility(View.INVISIBLE);
        threered.setVisibility(View.VISIBLE); threegreen.setVisibility(View.INVISIBLE);
        // setting on-click listeners to change how many arrows are in the quiver
        onered.setOnClickListener(new OnClickListener() { @Override public void onClick(View v) { changeArrow(1); } }); // changes to only one arrow
        twored.setOnClickListener(new OnClickListener() { @Override public void onClick(View v) { changeArrow(2); } }); // changes to two arrows
        threered.setOnClickListener(new OnClickListener() { @Override public void onClick(View v) { changeArrow(3); } }); // changes to three arrows
    }

    private void barCool() {
        progBow.setVisibility(ProgressBar.VISIBLE); // player can see the progBar
        /**/ // set different cases for one arrow, two arrows, etc
        if (numArrows == 2) { // two arrows being shot at once
            duration = 1400; // duration of timer
            upProg = 8; // what the progress bar increments by
        } else if (numArrows == 3) { // three arrows being shot at once
            duration = 1700;
            upProg = 6;
        } else { // just one arrow, default
            duration = 1100;
            upProg = 10;
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

    private void changeArrow(int n) { // changes the number of arrows being shot at once
        // sets all buttons to red
        onered.setVisibility(View.VISIBLE);
        twored.setVisibility(View.VISIBLE);
        threered.setVisibility(View.VISIBLE);
        if (n == 3) { // 3x
            threegreen.setVisibility(View.VISIBLE);
            numArrows = 3;
            /**/ // some other commands, to be sure... i.e. sending data to laptop
        } else if (n == 2) { // 2x
            twogreen.setVisibility(View.VISIBLE);
            numArrows = 2;
        } else { // only one arrow, base case
            onegreen.setVisibility(View.VISIBLE);
            numArrows = 1;
        }
    }
}