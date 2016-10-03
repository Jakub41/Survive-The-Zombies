package com.example.jake_games.survivethezombies;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BordeInferior extends GameObject {

    private Bitmap image;


    public BordeInferior(Bitmap res, int x, int y){
        height = 30;
        width = 30;

        this.x = x;
        this.y = y;
        dx = GamePanel.MOVESPEED;

        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){
        x += dx;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }
}
