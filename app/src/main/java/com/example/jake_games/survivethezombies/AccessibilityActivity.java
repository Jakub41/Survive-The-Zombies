package com.example.jake_games.survivethezombies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AccessibilityActivity extends AppCompatActivity implements OnClickListener {

    CheckBox vibration, effects, music;
    Context mContext;
    Button buttonScanner;

    TextView tvFormat, tvContent;

    public void AccessibilityActivity(Context context){
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences spp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility);

        vibration = (CheckBox) findViewById(R.id.checkBox);
        effects = (CheckBox) findViewById(R.id.checkBox2);
        music = (CheckBox) findViewById(R.id.checkBox3);

        //sp.edit().putInt("vibrationEnabled", 1);

        if(spp.getInt("vibrationEnabled", 1) == 1) vibration.setChecked(true);
        else vibration.setChecked(false);

        if(spp.getInt("effectsEnabled", 1) == 1) effects.setChecked(true);
        else effects.setChecked(false);

        if(spp.getInt("musicEnabled", 1) == 1) music.setChecked(true);
        else music.setChecked(false);


        buttonScanner =(Button) findViewById(R.id.buttonScanner);
        buttonScanner.setOnClickListener(this);


    }

    public void runTest(View v){
        Intent intent = new Intent(AccessibilityActivity.this, com.example.jake_games.survivethezombies.TestActivity.class);
        startActivity(intent);
    }

    public void applySettings(View v){

        SharedPreferences spp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);


        vibration = (CheckBox) findViewById(R.id.checkBox);
        effects = (CheckBox) findViewById(R.id.checkBox2);
        music = (CheckBox) findViewById(R.id.checkBox3);

        if(vibration.isChecked()){
            spp.edit().putInt("vibrationEnabled", 1).commit();
        }
        else spp.edit().putInt("vibrationEnabled", 0).commit();

        if(effects.isChecked()){
            spp.edit().putInt("effectsEnabled", 1).commit();
        }
        else spp.edit().putInt("effectsEnabled", 0).commit();

        if(music.isChecked()){
            spp.edit().putInt("musicEnabled", 1).commit();
        }
        else spp.edit().putInt("musicEnabled", 0).commit();

        //Log.d("checkbox: ", Integer.toString(spp.getInt("vibrationEnabled", 1)));

        AlertDialog.Builder settingsAlert = new AlertDialog.Builder(this);
        settingsAlert.setMessage("Apply Settings")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    // Codigo a run cuando pulsemos "settings..."
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        //finish();
                    }
                })
                .create();
        settingsAlert.show();

    }

    public void onClick(View v){
        if(v.getId() == R.id.buttonScanner) {
            IntentIntegrator integrator = new IntentIntegrator(AccessibilityActivity.this);
            integrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        }

}
