package com.example.jake_games.survivethezombies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class Game extends Activity{

    private MediaPlayer mp;
    private int mpCurrPos;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences spp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        //Title disable
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mp = MediaPlayer.create(this, R.raw.background);
        mp.setLooping(true);
        mp.setVolume(.5f, .5f);

        //Log.d("music", Integer.toString(spp.getInt("musicEnabled", 1)));

        if(spp.getInt("musicEnabled", 1) == 1){
            mp.start();
        }
        else mp.stop();

        setContentView(new GamePanel(this));
    }

    @Override
    protected void onPause(){
        super.onPause();
        mpCurrPos = mp.getCurrentPosition();
        mp.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mp.seekTo(mpCurrPos);
        mp.start();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mp.stop();
        mp.release();
        finish();
    }


}
