package com.example.jake_games.survivethezombies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

public class PopUpCharacters extends Activity {

    ImageButton turtle;
    ImageButton nian;
    ImageButton koala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_character);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.6), (int) (height * 0.6));

        final SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);

        turtle = (ImageButton) findViewById(R.id.turtle);
        nian = (ImageButton) findViewById(R.id.niancat);
        koala = (ImageButton) findViewById(R.id.koala);

        turtle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putInt("Character", 1).commit();
                finish();
            }
        });

        koala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putInt("Character", 2).commit();
                finish();
            }
        });

        nian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putInt("Character", 3).commit();
                finish();
            }
        });
    }
}
