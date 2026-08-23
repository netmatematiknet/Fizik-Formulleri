package com.mobilprogramlar.FizikFormullerim;

public class Person {
    private final int topicId;
    private final String Name;
    private final String Address;
    private int PicResID;
    private int ResimSayisi;

    public Person(int topicId, String name, String address, int imageID, int resimSayi) {
        this.topicId = topicId;
        Name = name;
        Address = address;
        PicResID = imageID;
        ResimSayisi = resimSayi;
    }

    public int getTopicId() {
        return topicId;
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
