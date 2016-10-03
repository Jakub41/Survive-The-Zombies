package com.example.jake_games.survivethezombies;

import android.content.Context;

import java.util.ArrayList;


public class Score {
    Context mContext;
    static ArrayList<String> ranking = new ArrayList<String>();
    String name;
    int puntuacion;
    String localizacion;
    static ArrayList<Score> p = new ArrayList<Score>();
    //MiPosicion posicion = new MiPosicion();
    //RankingActivity r = new RankingActivity();

    public Score(int puntuacion, String nombre){
        this.puntuacion = puntuacion;
        this.name = name;
        //getposition();
        //Geocoder geocoder = new Geocoder(this.mContext, Locale.getDefault());
        //MiPosicion posicion = new MiPosicion();
        //this.score=geocoder.getFromLocation();
    }


    public void llenarRanking(){

        if (!p.isEmpty()){
            for (int i = 0; i <p.size() ; i++) {

                if(p.get(i).puntuacion<this.puntuacion) {

                    ranking.add(i, this.name + " | " + this.puntuacion + " | " + this.localizacion);
                }
            }
        }
        else {
            p.add(this);
            ranking.add( this.name + " | " + this.puntuacion + " | " + this.localizacion);

        }

        //r(RankingActivity).actu(ranking, p);
        //r.actu(ranking, p);

    }

    public ArrayList<String> updateRanking(){
        return ranking;

    }
    public void getposition(){
        //this.localizacion=posicion.enviarPosicion();
    }


}