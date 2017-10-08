package com.example.aniem.madhacks;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class Bow extends AppCompatActivity {

    // bow sprites
    private ProgressBar progBow;
    private View bow;
    private View bowtwo;
    private View bowthree;

    // 1x, 2x, 3x images
    private View onered; private View onegreen;
    private View twored; private View twogreen;
    private View threered; private View threegreen;

    // rotating the bows
    private View.OnTouchListener touchHandler;
    float currentRotation; // keeps current rotation of bow constant when changing bows

    // speech recognition
    private SpeechRecognizer mSpeech;
    private Intent intent;

    // global variables
    private ArrayList<String> speech; // arrayList of speech commands
    private int numArrows = 1; // number of arrows being shot at once
    private int duration = 1100; // cooldown timer duration
    private int upProg = 10; // what the progressBar increments by as it cools down
    private TextView test; /**/ // tested the speechRecognition
    private int rotation; // stores rotation of phone
    // screen size
    private DisplayMetrics metrics;
    int screenWidth, screenHeight;
    final int HEADER_OFFSET = 90; // height of the banner at the top of the app /**/ // get actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bow);

        // general environment
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks screen to be "portrait"
        test = (TextView) findViewById(R.id.test); // for debugging purposes
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        // create different bow images
        bow = findViewById(R.id.Bow); bowtwo = findViewById(R.id.bowtwo); bowthree = findViewById(R.id.bowthree);
        bowtwo.setVisibility(View.INVISIBLE); bowthree.setVisibility(View.INVISIBLE); // only bow with one arrow shows up
        // set onClickListeners to execute the "fire();" command
        bow.setOnClickListener(new OnClickListener() { @Override public void onClick(View v) { fire(); } });
        bowtwo.setOnClickListener(new OnClickListener() { @Override public void onClick(View v) { fire(); } });
        bowthree.setOnClickListener(new OnClickListener() { @Override public void onClick(View v) { fire(); } });

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

        // touch listener for bow rotation
        touchHandler = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getting the bow to the correct angle
                rotateBow((int) event.getRawX(), (int) event.getRawY(), v); // rotates the bow based on position of player's finger
                // upon releasing the bow
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    fire();
                }
                return true;
            }
        };
        // setting this listener for the bows
        bow.setOnTouchListener(touchHandler); bowtwo.setOnTouchListener(touchHandler); bowthree.setOnTouchListener(touchHandler);

        /**/ // for speech debug only:
        //test = (TextView) findViewById(R.id.test);
        //promptSpeechInput();

        // creates the speech intent /**/ // work in progress ...
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // creates an Intent
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // processes speech to turn to text
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); // language of the device
        //mSpeech.setRecognitionListener(new SpeechRecognitionListener());
    }

    private void barCool() {
        progBow.setVisibility(ProgressBar.VISIBLE); // player can see the progBar
        /**/ // set different cases for one arrow, two arrows, etc
        if (numArrows == 2) { // two arrows being shot at once
            duration = 1500; // duration of timer
            upProg = 7; // what the progress bar increments by
        } else if (numArrows == 3) { // three arrows being shot at once
            duration = 2100;
            upProg = 5;
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

    private void fire() { // fires arrow
        if (progBow.getVisibility() != ProgressBar.VISIBLE) { // progress bar isn't cooling down
                    /**/ // send gyro data to computer, along with numArrows
            barCool(); // activates cooldown timer
        }
    }

    private void changeArrow(int n) { // changes the number of arrows being shot at once
        // changing the bow&arrow sprite
        bow.setVisibility(View.INVISIBLE); bowtwo.setVisibility(View.INVISIBLE); bowthree.setVisibility(View.INVISIBLE);
        // sets all buttons to red
        onered.setVisibility(View.VISIBLE); onegreen.setVisibility(View.INVISIBLE);
        twored.setVisibility(View.VISIBLE); twogreen.setVisibility(View.INVISIBLE);
        threered.setVisibility(View.VISIBLE); threegreen.setVisibility(View.INVISIBLE);
        if (n == 3) { // 3x
            threegreen.setVisibility(View.VISIBLE); // selects correct sprite
            numArrows = 3; // sets correct amount of arrows to shoot
            bowthree.setRotation(currentRotation); // same rotation to be the same as the bow that was just being used
//            test.setText("rotation is now " + bowthree.getRotation() + ", old rotation was " + rotation);
            bowthree.setVisibility(View.VISIBLE); // makes the bow visible
            /**/ // some other commands, to be sure... i.e. sending data to laptop
        } else if (n == 2) { // 2x
            twogreen.setVisibility(View.VISIBLE);
            numArrows = 2;
            bowtwo.setRotation(currentRotation);
            bowtwo.setVisibility(View.VISIBLE);
        } else { // only one arrow, base case
            onegreen.setVisibility(View.VISIBLE);
            numArrows = 1;
            bow.setRotation(currentRotation);
            bow.setVisibility(View.VISIBLE);
        }
    }

    private void rotateBow(int x, int y, View v) { // will rotate the bow sprites
        int minY = (screenHeight-v.getHeight()) / 2;
        int maxY = minY + v.getHeight();
        double theta = getTheta(x,y,v); // gets angle in degrees
        if (y > minY/* && y < maxY*//**/) { // so the bow doesn't fire when pressing buttons; must be touching the bow
            v.setRotation((float)theta-90); // sets bow rotation to track finger
            currentRotation = v.getRotation(); // sets global variable to keep track of a bow's rotation
        }
    }

    private double getTheta(int x, int y, View v) { // gets the angle between your finger and an object, with respect to the vertical
        double w = x - (screenWidth/2);
        double h = y - (screenHeight/2) - HEADER_OFFSET; // -90 due to header pixel offsets
        //double theta = Math.toDegrees(h/w);
        double theta = Math.toDegrees(Math.atan2(h,w));
        return theta; // returns angle
    }

    // voice commands !! /**/ remove below if all goes well
    /*private void promptSpeechInput() {
        // creates the speech intent
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // creates an Intent
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // processes speech to turn to text
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); // language of the device

        try {
            startActivityForResult(intent, 10);
        } catch(ActivityNotFoundException e) {
            // Toast.makeText(MainActivity.this, "device does not support speech language", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        super.onActivityResult(reqCode, resCode, i);

        switch(reqCode) {
            case 10: if(resCode==RESULT_OK && i!=null) { // speech is ok, Intent isn't null
                speech = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                /** // do something with the text
                test.setText(speech.get(0)); /** // debug, sets text to display onto screen
            }
                break;
        }
    }*/
}