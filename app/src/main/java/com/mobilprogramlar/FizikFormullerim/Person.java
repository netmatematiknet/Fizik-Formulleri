package com.mobilprogramlar.FizikFormullerim;

public class Person {
    private final String Name;
    private final String Address;
    private int PicResID;
    private int ResimSayisi;

    public Person(String name, String address){
        Name = name;
        Address = address;
    }
    public Person(String name, String address, int imageID){
        Name = name;
        Address = address;
        PicResID = imageID;
    }
    public Person(String name, String address, int imageID, int resimSayi){
        Name = name;
        Address = address;
        PicResID = imageID;
        ResimSayisi = resimSayi;
    }
    public String getName() {
        return Name;
    }
    public String getAddress() {
        return Address;
    }
    public int getPictureResourceID() {
        return PicResID;
    }
    public int getResimSayisi() {
        return ResimSayisi;
    }
}