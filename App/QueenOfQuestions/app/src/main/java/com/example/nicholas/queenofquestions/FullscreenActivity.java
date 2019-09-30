package com.example.nicholas.queenofquestions;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

//    /**
//     * Some older devices needs a small delay between UI widget updates
//     * and a change of the status and navigation bar.
//     */
//    private static final int UI_ANIMATION_DELAY = 300;
//    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private GameState gameState = GameState.START;
    private int questionNum = 0;
    private final int MAX_QUESTION = 3;

    //Buttons
    android.widget.Button mTopButton;
    android.widget.Button mTopMidButton;
    android.widget.Button mBottomMidButton;
    android.widget.Button mBottomButton;

    android.widget.Button[] buttons;


    //Really lazy
    private String[] questionList;
    private String[] guessList;

    private enum GameState{
        START,IN_GAME,GUESS,RESULT;
    }
//    private final Runnable mHidePart2Runnable = new Runnable() {
//        @SuppressLint("InlinedApi")
//        @Override
//        public void run() {
//            // Delayed removal of status and navigation bar
//
//            // Note that some of these constants are new as of API 16 (Jelly Bean)
//            // and API 19 (KitKat). It is safe to use them, as they are inlined
//            // at compile-time and do nothing on earlier devices.
//            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        }
//    };
//    private View mControlsView;
//    private final Runnable mShowPart2Runnable = new Runnable() {
//        @Override
//        public void run() {
//            // Delayed display of UI elements
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.show();
//            }
//            mControlsView.setVisibility(View.VISIBLE);
//        }
//    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mBeginTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                switch (gameState){
                    case START:
                        startGame(Button.fromId(view.getId()));
                        break;
                    case IN_GAME:
                        askQuestion();
                        break;
                    case GUESS:
                        getResult(Button.fromId(view.getId()));
                        break;
                    case RESULT:
                        cleanUp();
                        break;
                }
            }
            return false;
        }
    };

    private void getResult(Button button){
        if(button == Button.TOP) {
            win();
            gameState = GameState.RESULT;
            mBottomMidButton.setEnabled(true);
            mBottomButton.setEnabled(true);
        } else if(button == button.TOP_MID){
            lose();
            gameState = GameState.RESULT;
            mBottomMidButton.setEnabled(true);
            mBottomButton.setEnabled(true);
        }
    }

    private void win(){
        ((android.widget.TextView) findViewById(R.id.fullscreen_content)).setText(R.string.win_statement);
        String[] winResponses = getResources().getStringArray(R.array.win_strings);
        for(int i = 0;i<buttons.length;i++){
            buttons[i].setText(winResponses[i]);
        }
    }

    private void lose(){
        ((android.widget.TextView) findViewById(R.id.fullscreen_content)).setText(R.string.lose_statement);
        String[] loseResponses = getResources().getStringArray(R.array.lose_strings);
        for(int i = 0;i<buttons.length;i++){
            buttons[i].setText(loseResponses[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        initButtons();

        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        // Set the listener for all of the buttons that start the game
        for(android.widget.Button button:buttons){
            button.setOnTouchListener(mBeginTouchListener);
        }
    }

    private void initButtons(){
        mTopButton = ((android.widget.Button)findViewById(R.id.top_button));
        mTopMidButton = ((android.widget.Button)findViewById(R.id.top_mid_button));
        mBottomMidButton = ((android.widget.Button)findViewById(R.id.bottom_mid_button));
        mBottomButton = ((android.widget.Button)findViewById(R.id.bottom_button));
        buttons = new android.widget.Button[]{
                mTopButton,
                mTopMidButton,
                mBottomMidButton,
                mBottomButton
        };
        questionList = getResources().getStringArray(R.array.questions);
        guessList = getResources().getStringArray(R.array.guesses);
    }

    private void startGame(Button button){
        if(button == Button.TOP){
            setupButtons();
            gameState = GameState.IN_GAME;
            askQuestion();
        }
    }

    private void askQuestion(){
        String question;
        if(questionNum < MAX_QUESTION) {
            question = getQuestion();
        } else {
            question = getGuess();
            finalButtons();
            gameState = GameState.GUESS;
        }
        ((android.widget.TextView) findViewById(R.id.fullscreen_content)).setText(question);
        questionNum++;
    }

    private void cleanUp(){
        returnButtons();
        ((android.widget.TextView) findViewById(R.id.fullscreen_content)).setText(R.string.dummy_content);
        gameState = GameState.START;
        questionNum = 0;
    }

    private String getQuestion(){
        return questionList[questionNum];
    }

    private String getGuess(){
        Random r = new Random();
        int num = r.nextInt(guessList.length);
        return resourceString(R.string.guess) + " " + guessList[num];
    }

    private void setupButtons(){
        mTopButton.setText(resourceString(R.string.yes_string));
        mTopMidButton.setText(resourceString(R.string.no_string));
        mBottomMidButton.setText(resourceString(R.string.sometimes_string));
        mBottomButton.setText(resourceString(R.string.idk_string));
    }

    private void returnButtons(){
        mTopButton.setText(resourceString(R.string.animal_button_string));
        mTopMidButton.setText(resourceString(R.string.tbd_string));
        mBottomMidButton.setText(resourceString(R.string.tbd_string));
        mBottomButton.setText(resourceString(R.string.tbd_string));
    }

    private String resourceString(int id){
        return getResources().getString(id);
    }

    private void finalButtons(){
        mBottomMidButton.setEnabled(false);
        mBottomButton.setEnabled(false);
    }


//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        // Trigger the initial hide() shortly after the activity has been
//        // created, to briefly hint to the user that UI controls
//        // are available.
//        delayedHide(100);
//    }
//
//    private void toggle() {
//        if (mVisible) {
//            hide();
//        } else {
//            show();
//        }
//    }
//
//    private void hide() {
//        // Hide UI first
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//        mControlsView.setVisibility(View.GONE);
//        mVisible = false;
//
//        // Schedule a runnable to remove the status and navigation bar after a delay
//        mHideHandler.removeCallbacks(mShowPart2Runnable);
//        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
//    }
//
//    @SuppressLint("InlinedApi")
//    private void show() {
//
//
//        // Schedule a runnable to display UI elements after a delay
//        mHideHandler.removeCallbacks(mHidePart2Runnable);
//        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
//    }
//
//    /**
//     * Schedules a call to hide() in delay milliseconds, canceling any
//     * previously scheduled calls.
//     */
//    private void delayedHide(int delayMillis) {
//        mHideHandler.removeCallbacks(mHideRunnable);
//        mHideHandler.postDelayed(mHideRunnable, delayMillis);
//    }
}
