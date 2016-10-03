package com.example.jake_games.survivethezombies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    String userName;
    TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tvUserName = (TextView) findViewById(R.id.tvUserName);

        SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);

        //tvUserName.setText("pepito");
        if(!sp.getString("username", "---").equals("---")){
            tvUserName.setText("Welcome " + sp.getString("username", "---"));
        }
        else{
            tvUserName.setText("Nombre de usuario no establecido");
        }

        if(sp.getInt("inicialPopupl", 0) == 0){
            sp.edit().putInt("inicialPopupl", 1).commit();
            AlertDialog.Builder settingsAlert = new AlertDialog.Builder(this);
            settingsAlert.setMessage("Do you want to configure accessibility settings before?")
                    .setPositiveButton("Settings...", new DialogInterface.OnClickListener(){
                        // Codigo a run cuando pulsemos "settings..."
                        public void onClick(DialogInterface dialog, int which){
                            Intent intent = new Intent(MainActivity.this, AccessibilityActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No, leave me alone!", new DialogInterface.OnClickListener() {
                        // Codigo a run cuando pulsemos "Continuar"
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            settingsAlert.show();

        }

    }

    public void startGame(View v){
        SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(!sp.getString("username", "---").equals("---")){
            Intent intent = new Intent(MainActivity.this, PopUpMaps.class);
            startActivity(intent);
        }
        else{
            showToastMessage("Wait, I want to know what is your name!");
        }

    }

    public void startSettings(View v){
        Intent intent = new Intent(MainActivity.this, AccessibilityActivity.class);
        startActivity(intent);
    }

    public void buttonRanking(View v){
        Intent intent = new Intent(MainActivity.this, RankingActivity.class);
        startActivity(intent);
    }

    public void buttonUserName(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your name");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);


        startActivityForResult(intent, 1001);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1)
            {
                tvUserName = (TextView) findViewById(R.id.tvUserName);
                String txt = data.getStringExtra("result");
                tvUserName.setText("Welcome " + txt);
                sp.edit().putString("username", txt).commit();
            }

            else {
                tvUserName = (TextView) findViewById(R.id.tvUserName);
                ArrayList<String> textMatchlist = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                sp.edit().putString("username", textMatchlist.get(0).toString()).commit();
                tvUserName.setText("Welcome " + textMatchlist.get(0).toString());


                //Log.d("matchlist: ", textMatchlist.get(0).toString());

                //super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){

        moveTaskToBack(true);

    }

    public void characterChoose(View view) {
        Intent intent = new Intent(MainActivity.this, PopUpCharacters.class);
        startActivity(intent);
    }

    public void usernamesimple(View view) {
        Intent intent = new Intent(MainActivity.this, SimpleUser.class);
        startActivityForResult(intent, 1);
    }
}


