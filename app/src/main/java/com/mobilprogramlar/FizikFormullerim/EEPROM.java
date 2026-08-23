package com.mobilprogramlar.FizikFormullerim;

public class EEPROM {

    static int[] list = null;
    static String[] listString = null;

    public void setListCount(int count) {
        list = new int[count];
        listString = new String[count];
    }

    public int getCount() {
        return list.length;
    }

    public void write(int i, int value, String title) {
        list[i] = value;
        listString[i] = title;
    }
}
