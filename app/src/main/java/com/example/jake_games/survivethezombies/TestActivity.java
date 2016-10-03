package com.example.jake_games.survivethezombies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TestActivity extends AppCompatActivity {

    EditText result1, result2, result3, result4, result5;
    int probabilidad = 0;
    int resultint1 = 0, resultint2 = 0, resultint3 = 0, resultint4 = 0, resultint5 = 0;
    String resultstring1, resultstring2, resultstring3, resultstring4, resultstring5;
    boolean datosIntroducidos = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void enviarResult(View view){
        result1 = (EditText) findViewById(R.id.editText1);
        result2 = (EditText) findViewById(R.id.editText2);
        result3 = (EditText) findViewById(R.id.editText3);
        result4 = (EditText) findViewById(R.id.editText4);
        result5 = (EditText) findViewById(R.id.editText5);

        resultstring1 = result1.getText().toString();
        if(resultstring1 == null || resultstring1.equals("")) datosIntroducidos=false;
        resultstring2 = result2.getText().toString();
        if(resultstring2 == null || resultstring2.equals("")) datosIntroducidos=false;
        resultstring3 = result3.getText().toString();
        if(resultstring3 == null || resultstring3.equals("")) datosIntroducidos=false;
        resultstring4 = result4.getText().toString();
        if(resultstring4 == null || resultstring4.equals("")) datosIntroducidos=false;
        resultstring5 = result5.getText().toString();
        if(resultstring5 == null || resultstring5.equals("")) datosIntroducidos=false;

        // Debug
        /*
        Log.d("Texto1: ", result1.getText().toString());
        Log.d("Texto2: ", result2.getText().toString());
        Log.d("Texto3: ", result3.getText().toString());
        Log.d("Texto4: ", result4.getText().toString());
        Log.d("Texto5: ", result5.getText().toString());
        */

        //Intro
        if(datosIntroducidos){
            // Con integer.parseInt() convierto de String a Int
            resultint1 = Integer.parseInt(result1.getText().toString());
            resultint2 = Integer.parseInt(result2.getText().toString());
            resultint3 = Integer.parseInt(result3.getText().toString());
            resultint4 = Integer.parseInt(result4.getText().toString());
            resultint5 = Integer.parseInt(result5.getText().toString());

            if(resultint1 != 42) probabilidad++;
            if(resultint2 != 73) probabilidad++;
            if(resultint3 != 74) probabilidad++;
            if(resultint4 != 26) probabilidad++;
            if(resultint5 != 5) probabilidad++;

            probabilidad = probabilidad * 20; // 100%
            //Log.d("Probabilidad: ", Integer.toString(probabilidad));

            AlertDialog.Builder resultAlert = new AlertDialog.Builder(this);
            if(probabilidad == 0){
                resultAlert.setMessage("It could not detect any abnormality in your vision.")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener(){
                            // Codigo a run cuando pulsemos "Continuar"
                            public void onClick(DialogInterface dialog, int which){
                                //dialog.dismiss();
                                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .create();
                resultAlert.show();

            }

            else{
                resultAlert.setMessage("It has been detected " + probabilidad + "% \n" + "of blindness. Want to go to the accessibility settings?")
                        .setPositiveButton("settings...", new DialogInterface.OnClickListener(){
                            // Codigo a run cuando pulsemos "settings..."
                            public void onClick(DialogInterface dialog, int which){
                                //dialog.dismiss();
                                //Intent intent = new Intent(TestActivity.this, AccessibilityActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            // Codigo a run cuando pulsemos "Continuar"
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                                //Intent intent = new Intent(TestActivity.this, MainActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        })
                        .create();
                resultAlert.show();
            }
        }

        else{
            AlertDialog.Builder datosNoIntroducidos = new AlertDialog.Builder(this);
            datosNoIntroducidos.setMessage("\n" + "No results have been made correctly")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int which){
                                datosIntroducidos=true;
                                dialog.dismiss();
                            }
                        })
                        .create();
            datosNoIntroducidos.show();
        }
    }
}
