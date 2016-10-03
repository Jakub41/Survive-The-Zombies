package com.example.jake_games.survivethezombies;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class RankingActivity extends AppCompatActivity {


    static ArrayList<String> ranking = new ArrayList<String>();

    public RankingActivity(){

    }

    final String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SurviveTheZombies/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ranking.clear();

        //ranking = new ArrayList<String>(Arrays.asList(data));
        ListView listView = (ListView) findViewById(R.id.listview_ranking);

        File root = new File(fullPath);
        if (!root.exists()) {
            root.mkdirs();
        }

        File file = new File(fullPath, "highscores.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            line = br.readLine();

            if (line != null)
                ranking.add(line);

            while ((line = br.readLine()) != null) {
                ranking.add(line);

            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        final ListAdapter adapter = new ListAdapter(this, ranking);

        listView.setAdapter(adapter);


    }


}