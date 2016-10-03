package com.example.jake_games.survivethezombies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

    final String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SurviveTheZombies/";

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long powerUpTime;
    private long enemigoStartTime;
    private MainThread thread;
    private Background bg, bg2;
    private Player player;
    //private ArrayList<Smoke> smoke;
    private ArrayList<Enemies> enemies;
    private ArrayList<BordeSuperior> bordeSuperior;
    private ArrayList<BordeInferior> bordeInferior;
    private ModoFuria modoFuria;
    private Random rand = new Random();
    private int tamBordes;
    private boolean isReady;
    private Context mContext;


    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;

    private SoundPool sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    int soundId = sp.load(getContext(), R.raw.explosion, 1);
    int soundId2 = sp.load(getContext(), R.raw.explosionfail, 1);
    int soundId3 = sp.load(getContext(), R.raw.powerup, 1);

    private SoundPool sp2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    int soundId4 = sp2.load(getContext(), R.raw.tot2, 1);


    public GamePanel(Context context){
        super(context);
        this.mContext = context;
        
        getHolder().addCallback(this);
        
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter < 1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);

        if(spp.getInt("map", 1) == 1){
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background1));
            bg.setVector(-1);
            bg2 = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background2));
            bg2.setVector(-5);
        }

        if(spp.getInt("map", 1) == 2){
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background3));
            bg.setVector(-1);
            bg2 = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background4));
            bg2.setVector(-5);
        }

        if(spp.getInt("map", 1) == 3){
            bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background5));
            bg.setVector(-1);
            bg2 = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background6));
            bg2.setVector(-5);
        }

        Log.d("Valor de Character: ", Integer.toString(spp.getInt("Character", 1)));

        if(spp.getInt("Character", 1) == 1){
            player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player1), 66, 40, 3);
        }

        if(spp.getInt("Character", 1) == 2){
            player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player2), 66, 40, 3);
        }

        if(spp.getInt("Character", 1) == 3){
            player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player3), 66, 40, 3);
        }
        if((spp.getInt("Character", 1) != 1) && (spp.getInt("Character", 1) != 2) && (spp.getInt("Character", 1) != 3)){
            player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player1), 66, 40, 3);
        }

        enemies = new ArrayList<Enemies>();
        bordeSuperior = new ArrayList<BordeSuperior>();
        bordeInferior = new ArrayList<BordeInferior>();
        powerUpTime = System.nanoTime();
        enemigoStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);

        //Loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && isReady && reset){
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying()){

                if (!started) started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(player.getPlaying()){

            if(bordeInferior.isEmpty()){
                player.setPlaying(false);
                return;
            }
            if(bordeSuperior.isEmpty()){
                player.setPlaying(false);
                return;
            }

            bg.update();
            bg2.update();
            player.update();

            // collision at the bottom
            for(int i = 0; i < bordeInferior.size(); i++){
                if((collision(bordeInferior.get(i), player) || playerOutOfScreen()) && !player.getModoFuriaOn()){
                    soundExplosion();
                    vibrate(500);
                    soundTot();
                    player.setPlaying(false);
                    updateRecord();
                    break;
                }

            }

            // collision at the upper edge
            for(int i = 0; i < bordeSuperior.size(); i++){
                if((collision(bordeSuperior.get(i), player) || playerOutOfScreen()) && !player.getModoFuriaOn()){
                    soundExplosion();
                    vibrate(500);
                    soundTot();
                    player.setPlaying(false);
                    updateRecord();
                    break;
                }
            }


            this.updateBordeSuperior();

            this.updateBordeInferior();

            //enemies
            long enemigoElapsed = (System.nanoTime() - enemigoStartTime) / 1000000;
            if(enemigoElapsed > (2000 - player.getScore() / 4)){

                //the first missile always starts at the same place
                if(enemies.size() == 0) {
                    enemies.add(new Enemies(BitmapFactory.decodeResource(getResources(), R.drawable.zombie),
                            WIDTH + 10, HEIGHT / 2, 45, 15, player.getScore(), 13));
                }
                else{
                    enemies.add(new Enemies(BitmapFactory.decodeResource(getResources(), R.drawable.zombie),
                            WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (tamBordes * 2)) + tamBordes), 45, 15, player.getScore(), 13));
                }

                //timer
                enemigoStartTime = System.nanoTime();
            }
            // enemy collision
            for(int i = 0; i < enemies.size(); i++){
                //update enemies
                enemies.get(i).update();

                if(collision(enemies.get(i), player)){
                    enemies.remove(i);

                    if(!player.getModoFuriaOn()){
                        soundExplosion();
                        vibrate(500);
                        soundTot();
                        player.setPlaying(false);
                        updateRecord();
                    }
                    else{
                        soundFailedExplosion();
                        vibrate(100);
                        player.addScore(50);
                    }
                    break;
                }


                //Eliminate enemy
                if(enemies.get(i).getX() < -100){
                    enemies.remove(i);
                    break;
                }


                // add ray rampage mode
                long elapsed = (System.nanoTime() - powerUpTime) / 1000000;
                if (elapsed > 120){
                    if (modoFuria == null){
                        modoFuria = new ModoFuria(BitmapFactory.decodeResource(getResources(), R.drawable.rayo),
                                WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT - (tamBordes * 2)) + tamBordes), 45, 15, player.getScore(), 13);

                    }
                    if (modoFuria != null){
                        //update
                        modoFuria.update();

                        //collision checks while there have rampage mode to kill the enemy
                        if (collision(modoFuria, player)){
                            modoFuria = null;
                            soundPowerUp();
                            player.addScore(25);
                            //player.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.playermodofuria1));

                            if(spp.getInt("Character", 1) == 1){
                                player.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.playermodofuria1));
                            }

                            if(spp.getInt("Character", 1) == 2){
                                player.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.playermodofuria2));
                            }

                            if(spp.getInt("Character", 1) == 3){
                                player.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.playermodofuria3));
                            }
                            if((spp.getInt("Character", 1) != 1) && (spp.getInt("Character", 1) != 2) && (spp.getInt("Character", 1) != 3)){
                                player.PowerUpOn(BitmapFactory.decodeResource(getResources(), R.drawable.playermodofuria1));
                            }

                        }

                        //eliminate if lightning occurs offscreen
                        else if(modoFuria.getX() < -100){
                            modoFuria = null;
                        }
                    }
                }
            }
        }
        else{
            player.resetDY();
            if (!reset){
                isReady = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), player.getX(),
                        player.getY() - 30, 100, 100, 25);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime() - startReset) / 1000000;

            if (resetElapsed > 2500 && !isReady){
                newGame();
            }
        }
    }

    private void updateRecord() {
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);

        if (player.getScore() > spp.getInt("record", 0)) {
            spp.edit().putInt("record", player.getScore()).commit();
        }

		//get score, username and map
        String p = String.valueOf(player.getScore()) + "/" + spp.getString("username", "---").toString() + "/";
        if (spp.getInt("map", 1) == 1)
            p += "Space";
        else if (spp.getInt("map", 1) == 2)
            p += "Sunny Day";
        else p += "Damn that cold";

		//create file if it's not already created
        File root = new File(fullPath);
        if (!root.exists()) {
            root.mkdirs();
        }

        File file = new File(fullPath, "highscores.txt");

        int lines = 0;

		//get number of records in file
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            line = br.readLine();

            if (line != null)
            lines++;

            while ((line = br.readLine()) != null) {
                lines++;

            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }


		//if it's the first record just put the score
        if (lines == 0)
            try {
                FileOutputStream f = new FileOutputStream(file,true);
                PrintWriter pw = new PrintWriter(f);
                pw.println(p);
                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

		//if it's not the first one, compare the score to every record and update the file as needed
        else if (lines > 0) {

            String alldata = new String();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                int aux = 0;
                int ok = 0;

                line = "ya";

                while ((line = br.readLine()) != null && aux < 10) {
                    aux++;
                    String[] objectProperties = TextUtils.split(line, "/");
                    String[] pproperties = TextUtils.split(p, "/");

                   int obj = Integer.parseInt(objectProperties[0]);
                   int  prop = Integer.parseInt(pproperties[0]);

                    if (prop > obj && ok == 0) {
                        alldata += p;
                        alldata += '\n';
                        alldata += line;

                        if (aux < lines)
                        alldata += '\n';

                        if (aux == lines)
                        aux++;

                        ok = 1;
                    }

                    else if (prop == obj)
                    {
                        alldata += line;

                        if (aux < lines)
                            alldata += '\n';

                        ok = 1;
                    }

                    else {alldata += line;

                        if (aux < lines)
                        alldata += '\n';}

                }

                if (ok == 0 && aux < 10) {alldata += '\n';alldata += p;}

                br.close();
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }

			//put the updated info in the file
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.print(alldata);
                pw.flush();
                pw.close();
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void vibrate(int time){
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);

        if(spp.getInt("vibrationEnabled",1)==1) {
            v.vibrate(time);
        }
    }

    private void soundExplosion(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(spp.getInt("effectsEnabled", 1) == 1)
            sp.play(soundId, .4f, .4f, 0, 0, 1);
    }

    private void soundFailedExplosion(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(spp.getInt("effectsEnabled", 1) == 1)
            sp.play(soundId2, .9f, .9f, 0, 0, 1);
    }

    private void soundPowerUp(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(spp.getInt("effectsEnabled", 1) == 1)
            sp.play(soundId3, .5f, .5f, 0, 0, 1);
    }

    private void soundTot(){
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        if(spp.getInt("effectsEnabled", 1) == 1)
            sp2.play(soundId4, .9f, .9f, 0, 0, 1);
    }

    public boolean playerOutOfScreen(){
        if(player.getY() < 0 || player.getY() > HEIGHT) return true;
        return false;
    }

    public boolean collision(GameObject a, GameObject b){
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if(canvas != null){
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            bg2.draw(canvas);

            if(!dissapear){
                player.draw(canvas);
            }

            //draw lightning
            if(modoFuria != null) modoFuria.draw(canvas);

            //enemies
            for(Enemies m : enemies){
                m.draw(canvas);
            }


            //bordeSuperior
            for(BordeSuperior tb : bordeSuperior){
                tb.draw(canvas);
            }

            //bordeInferior
            for(BordeInferior bb : bordeInferior){
                bb.draw(canvas);
            }
            // explosion
            if(started){
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void updateBordeSuperior(){

        //update upper edge
        for(int i = 0; i < bordeSuperior.size(); i++){
            bordeSuperior.get(i).update();
            if (bordeSuperior.get(i).getX() < -30){
                //eliminates arraylist item and put a new one

                bordeSuperior.remove(i);

                bordeSuperior.add(new BordeSuperior(BitmapFactory.decodeResource(getResources(),
                        R.drawable.pinchosuperior), bordeSuperior.get(bordeSuperior.size() - 1).getX() + 30,
                        0, bordeSuperior.get(bordeSuperior.size() - 1).getHeight()));
            }
        }

    }

    public void updateBordeInferior(){

        //update lower edge
        for(int i = 0; i < bordeInferior.size(); i++){

            //eliminates array list item and put a new one

            bordeInferior.get(i).update();


            if(bordeInferior.get(i).getX() < -30){
                bordeInferior.remove(i);

                bordeInferior.add(new BordeInferior(BitmapFactory.decodeResource(getResources(), R.drawable.pinchoinferior
                ), bordeInferior.get(bordeInferior.size() - 1).getX() + 30, bordeInferior.get(bordeInferior.size() - 1
                ).getY()));
            }
        }
    }

    public void newGame(){
        dissapear = false;

        bordeInferior.clear();
        bordeSuperior.clear();

        enemies.clear();

        tamBordes = 30;

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT / 2);
        modoFuria = null;


        for(int i = 0; i * 30 < WIDTH + 40; i++){
            // creates first upper edge

            bordeSuperior.add(new BordeSuperior(BitmapFactory.decodeResource(getResources(), R.drawable.pinchosuperior
            ), i * 30, 0, 30));
        }

        for(int i = 0; i * 30 < WIDTH + 40; i++){
            // creates first lower edge

            bordeInferior.add(new BordeInferior(BitmapFactory.decodeResource(getResources(), R.drawable.pinchoinferior)
                    , i * 30, HEIGHT - 30));
        }
        isReady = true;
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(40);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Score: " + (player.getScore()), 10, HEIGHT - 30, paint);
        SharedPreferences spp = this.getContext().getSharedPreferences("com.example.jake_games.survivethezombies", 0);
        int best = spp.getInt("record", 0);
        canvas.drawText("Record: " + best, WIDTH - 315, HEIGHT - 30, paint);

        if(!player.getPlaying() && isReady && reset){
            Paint paint1 = new Paint();
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("\n" + "Tap to start", WIDTH / 2 - 60, HEIGHT / 2, paint1);
        }
    }
}