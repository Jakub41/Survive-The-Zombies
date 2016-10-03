package com.example.jake_games.survivethezombies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SimpleUser extends Activity {

    EditText userinput;
    Button setuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usernamesimple);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.6), (int) (height * 0.6));

        userinput = (EditText) findViewById(R.id.setuser);

        setuser = (Button) findViewById(R.id.buttonforuser);

        final SharedPreferences sp = this.getSharedPreferences("com.example.jake_games.survivethezombies", 0);

        setuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = userinput.getText().toString();

                if (!txt.matches(""))
                {
                    sp.edit().putString("username", txt);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",txt);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Put an username",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
