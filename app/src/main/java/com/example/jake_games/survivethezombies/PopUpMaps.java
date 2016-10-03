package com.example.jake_games.survivethezombies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



public class PopUpMaps extends Activity{

    RadioButton rbMap1, rbMap2, rbMap3;
    RadioGroup rgPopUp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.7), (int)(height*0.7));

        SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        rbMap1 = (RadioButton) findViewById(R.id.rbMap1);
        rbMap2 = (RadioButton) findViewById(R.id.rbMap2);
        rbMap3 = (RadioButton) findViewById(R.id.rbMap3);
        rgPopUp = (RadioGroup) findViewById(R.id.rgPopUp);

        if(sp.getInt("map", 1) == 1) rbMap1.setChecked(true);
        else rbMap1.setChecked(false);

        if(sp.getInt("map", 1) == 2) rbMap2.setChecked(true);
        else rbMap2.setChecked(false);

        if(sp.getInt("map", 1) == 3) rbMap3.setChecked(true);
        else rbMap3.setChecked(false);


    }

    public void seleccionarMap(View v){
        SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(rbMap1.isChecked()) sp.edit().putInt("map", 1).commit();
        else if(rbMap2.isChecked()) sp.edit().putInt("map", 2).commit();
        else if (rbMap3.isChecked()) sp.edit().putInt("map", 3).commit();

        Intent intent = new Intent(PopUpMaps.this, Game.class);
        startActivity(intent);
    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
