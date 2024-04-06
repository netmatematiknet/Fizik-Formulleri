package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;

public class EEPROM {

    static int []list = null;
    static String[]listString = null;


    public void setListCount(int count)
    {
        list = null;
        listString = null;
        list = new int[count];
        listString = new String[count];
    }
    public int getCount()
    {
        return list.length;
    }
    public void write(int i, int value, String title)
    {
        list[i] = value;
        listString[i] = title;
    }



    //REKLAMLAR
    public static int reklamIhtimali = 90;    //Reklam yuzde değeri buradan belirlenir.
    public static int getRandom(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min " + min + " greater than max " + max);
        }
        return (int) ( (long) min + Math.random() * ((long)max - min + 1));
    }
    public void reklamGoster(Context context, int yuzdeIhtimal)
    {
        yuzdeIhtimal = Math.min(yuzdeIhtimal, 100);
        yuzdeIhtimal = Math.max(yuzdeIhtimal, 0);
        int rdr = getRandom(0,100);
          if(rdr<=yuzdeIhtimal)
        {
            //new Reklam().reklamGoster(context);
        }
    }
    //REKLAMLAR



}
