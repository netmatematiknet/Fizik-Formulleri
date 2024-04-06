package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;

public class FormullerFizik extends AppCompatActivity {
    private static final String TAG = "FormullerFizik";
    private AdView mAdView1;
    int[] resimsayilari;
    int[] images;
    EEPROM eeprom;
    Intent intent;
    Context context;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formuller_fizik);

        //Google Analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Google Addmob REKLAM
        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
        //Google Addmob REKLAM

        resimsayilari = new int[]{5,13,10,10,25,24,42,20,7,6,23,20,15,8,9,21,20,10,10,12,17};

        context = getApplicationContext();
        // Persons is creating
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Person("Fizik Bilimine Giriş","Tyt",R.drawable.einstein_equation, resimsayilari[0]));
        persons.add(new Person("Madde ve Özellikleri: Kütle ve Özkütle","Tyt",R.drawable.einstein_equation, resimsayilari[1]));
        persons.add(new Person("Madde ve Özellikleri: Dayanıklılık ve Kılcallık","Tyt",R.drawable.einstein_equation, resimsayilari[2]));
        persons.add(new Person("Kuvvet ve Hareket: Tek Boyutta Hareket","Tyt",R.drawable.einstein_equation, resimsayilari[3]));
        persons.add(new Person("Kuvvet ve Hareket: Newton Yasaları","Tyt",R.drawable.einstein_equation, resimsayilari[4]));
        persons.add(new Person("Kuvvet ve Hareket: İş, Güç ve Enerji","Tyt",R.drawable.einstein_equation, resimsayilari[5]));
        persons.add(new Person("Isı, Sicaklık, Hal Değişimi, Genleşme","Tyt",R.drawable.einstein_equation, resimsayilari[6]));
        persons.add(new Person("Basınç ve Kaldırma Kuvveti: Basınç","Tyt",R.drawable.einstein_equation, resimsayilari[7]));
        persons.add(new Person("Basınç ve Kaldırma Kuvveti: Kaldırma Kuvveti","Tyt",R.drawable.einstein_equation, resimsayilari[8]));
        persons.add(new Person("Elektrik ve Manyetizma: Elektrostatik","Tyt",R.drawable.einstein_equation, resimsayilari[9]));
        persons.add(new Person("Elektrik ve Manyetizma: Elektrik Akımı","Tyt",R.drawable.einstein_equation, resimsayilari[10]));
        persons.add(new Person("Elektrik ve Manyetizma: Mıknatıslar ve Manyetik Alan","Tyt",R.drawable.einstein_equation, resimsayilari[11]));
        persons.add(new Person("Optik: Aydınlanma ve Gölge","Tyt",R.drawable.einstein_equation, resimsayilari[12]));
        persons.add(new Person("Optik: Işığın Yansıması ve Düzlem Ayna","Tyt",R.drawable.einstein_equation, resimsayilari[13]));
        persons.add(new Person("Optik: Küresel Aynalar","Tyt",R.drawable.einstein_equation, resimsayilari[14]));
        persons.add(new Person("Optik: Işığın Kırılması ve Renk","Tytu",R.drawable.einstein_equation, resimsayilari[15]));
        persons.add(new Person("Optik: Mercekler ve Optik Araçlar","Tyt",R.drawable.einstein_equation, resimsayilari[16]));
        persons.add(new Person("Dalgalar: Dalgaların Genel Özellikleri","Tyt",R.drawable.einstein_equation, resimsayilari[17]));
        persons.add(new Person("Dalgalar: Yay Dalgaları","Tyt",R.drawable.einstein_equation, resimsayilari[18]));
        persons.add(new Person("Dalgalar: Su Dalgaları","Tyt",R.drawable.einstein_equation, resimsayilari[19]));
        persons.add(new Person("Dalgalar: Ses ve Deprem Dalgaları","Tyt",R.drawable.einstein_equation, resimsayilari[20]));


        // Adapter is creating
        PersonAdapter personAdapter = new PersonAdapter(this, R.layout.formuller_listview,persons);
        // Finding listview and persons will short in listview.
        ListView listviewPerson = findViewById(R.id.listView_persons);
        if(listviewPerson != null){
            listviewPerson.setAdapter(personAdapter);
        }


        assert listviewPerson != null;
        listviewPerson.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            switch (position){
                case 0:
                    images = new int[]{ R.drawable.f01_fizik_bil_giris_01, R.drawable.f01_fizik_bil_giris_02, R.drawable.f01_fizik_bil_giris_03,R.drawable.f01_fizik_bil_giris_04,R.drawable.f01_fizik_bil_giris_05 };
                    aokEEPROM(images, position, "Fizik Bilimine Giriş " );
                    //aokEEPROM(images, position, "Temel Kavramlar : " + String.valueOf(images.length)+" sayfa");
                    break;
                case 1:
                    images = new int[]{ R.drawable.f02_madde_ozel_kutle_01, R.drawable.f02_madde_ozel_kutle_02, R.drawable.f02_madde_ozel_kutle_03, R.drawable.f02_madde_ozel_kutle_04,R.drawable.f02_madde_ozel_kutle_05,
                            R.drawable.f02_madde_ozel_kutle_06,R.drawable.f02_madde_ozel_kutle_07,R.drawable.f02_madde_ozel_kutle_08,R.drawable.f02_madde_ozel_kutle_09,R.drawable.f02_madde_ozel_kutle_10,
                            R.drawable.f02_madde_ozel_kutle_11,R.drawable.f02_madde_ozel_kutle_12,R.drawable.f02_madde_ozel_kutle_13,};
                    aokEEPROM(images, position, "Madde ve Özellikleri: Kütle ve Özkütle ");
                    break;
                case 2:
                    images = new int[]{ R.drawable.f02_madde_ozel_dayaniklilik_01,R.drawable.f02_madde_ozel_dayaniklilik_02,R.drawable.f02_madde_ozel_dayaniklilik_03,R.drawable.f02_madde_ozel_dayaniklilik_04,
                            R.drawable.f02_madde_ozel_dayaniklilik_05,R.drawable.f02_madde_ozel_dayaniklilik_06,R.drawable.f02_madde_ozel_dayaniklilik_07,R.drawable.f02_madde_ozel_dayaniklilik_08,
                            R.drawable.f02_madde_ozel_dayaniklilik_09,R.drawable.f02_madde_ozel_dayaniklilik_10};
                    aokEEPROM(images, position, "Madde ve Özellikleri: Dayanıklılık ve Kılcallık ");
                    break;
                case 3:
                    images = new int[]{ R.drawable.f03_kuvvet_hareket_tekboyut_01,R.drawable.f03_kuvvet_hareket_tekboyut_02,R.drawable.f03_kuvvet_hareket_tekboyut_03,R.drawable.f03_kuvvet_hareket_tekboyut_04,R.drawable.f03_kuvvet_hareket_tekboyut_05,
                            R.drawable.f03_kuvvet_hareket_tekboyut_06,R.drawable.f03_kuvvet_hareket_tekboyut_07,R.drawable.f03_kuvvet_hareket_tekboyut_08,R.drawable.f03_kuvvet_hareket_tekboyut_09,R.drawable.f03_kuvvet_hareket_tekboyut_10};
                    aokEEPROM(images, position, "Kuvvet ve Hareket: Tek Boyutta Hareket");
                    break;
                    //Kuvvet ve Hareket: Tek Boyutta Hareket
                //f03_kuvvet_hareket_tekboyut_10
                case 4:
                    images = new int[]{ R.drawable.f03_kuvvet_hareket_newton_01, R.drawable.f03_kuvvet_hareket_newton_02,R.drawable.f03_kuvvet_hareket_newton_03,R.drawable.f03_kuvvet_hareket_newton_04,R.drawable.f03_kuvvet_hareket_newton_05,
                            R.drawable.f03_kuvvet_hareket_newton_06,R.drawable.f03_kuvvet_hareket_newton_07,R.drawable.f03_kuvvet_hareket_newton_08,R.drawable.f03_kuvvet_hareket_newton_09,R.drawable.f03_kuvvet_hareket_newton_10,
                            R.drawable.f03_kuvvet_hareket_newton_11,R.drawable.f03_kuvvet_hareket_newton_12,R.drawable.f03_kuvvet_hareket_newton_13,R.drawable.f03_kuvvet_hareket_newton_14,R.drawable.f03_kuvvet_hareket_newton_15,
                            R.drawable.f03_kuvvet_hareket_newton_16,R.drawable.f03_kuvvet_hareket_newton_17,R.drawable.f03_kuvvet_hareket_newton_18,R.drawable.f03_kuvvet_hareket_newton_19,R.drawable.f03_kuvvet_hareket_newton_20,
                            R.drawable.f03_kuvvet_hareket_newton_21,R.drawable.f03_kuvvet_hareket_newton_22,R.drawable.f03_kuvvet_hareket_newton_23,R.drawable.f03_kuvvet_hareket_newton_24,R.drawable.f03_kuvvet_hareket_newton_25};
                    aokEEPROM(images, position, "Kuvvet ve Hareket: Newton Yasaları ");
                    break;
                case 5:
                    images = new int[]{ R.drawable.f03_kuvvet_hareket_isguc_01,R.drawable.f03_kuvvet_hareket_isguc_02,R.drawable.f03_kuvvet_hareket_isguc_02,R.drawable.f03_kuvvet_hareket_isguc_04,R.drawable.f03_kuvvet_hareket_isguc_05,
                            R.drawable.f03_kuvvet_hareket_isguc_06,R.drawable.f03_kuvvet_hareket_isguc_07,R.drawable.f03_kuvvet_hareket_isguc_08,R.drawable.f03_kuvvet_hareket_isguc_09,R.drawable.f03_kuvvet_hareket_isguc_10,
                            R.drawable.f03_kuvvet_hareket_isguc_11,R.drawable.f03_kuvvet_hareket_isguc_12,R.drawable.f03_kuvvet_hareket_isguc_13,R.drawable.f03_kuvvet_hareket_isguc_14,R.drawable.f03_kuvvet_hareket_isguc_15,
                            R.drawable.f03_kuvvet_hareket_isguc_16,R.drawable.f03_kuvvet_hareket_isguc_17,R.drawable.f03_kuvvet_hareket_isguc_18,R.drawable.f03_kuvvet_hareket_isguc_19,R.drawable.f03_kuvvet_hareket_isguc_20,
                            R.drawable.f03_kuvvet_hareket_isguc_21,R.drawable.f03_kuvvet_hareket_isguc_22,R.drawable.f03_kuvvet_hareket_isguc_23,R.drawable.f03_kuvvet_hareket_isguc_24};
                    aokEEPROM(images, position, "Kuvvet ve Hareket: İş, Güç ve Enerji ");
                    break;
                case 6:
                    images = new int[]{ R.drawable.f04_isi_sicaklik_01,R.drawable.f04_isi_sicaklik_02,R.drawable.f04_isi_sicaklik_03,R.drawable.f04_isi_sicaklik_04,R.drawable.f04_isi_sicaklik_05,
                            R.drawable.f04_isi_sicaklik_06,R.drawable.f04_isi_sicaklik_07,R.drawable.f04_isi_sicaklik_08,R.drawable.f04_isi_sicaklik_09,R.drawable.f04_isi_sicaklik_10,
                            R.drawable.f04_isi_sicaklik_11,R.drawable.f04_isi_sicaklik_12,R.drawable.f04_isi_sicaklik_13,R.drawable.f04_isi_sicaklik_14,R.drawable.f04_isi_sicaklik_15,
                            R.drawable.f04_isi_sicaklik_16,R.drawable.f04_isi_sicaklik_17,R.drawable.f04_isi_sicaklik_18,R.drawable.f04_isi_sicaklik_19,R.drawable.f04_isi_sicaklik_20,
                            R.drawable.f04_isi_sicaklik_21,R.drawable.f04_isi_sicaklik_22,R.drawable.f04_isi_sicaklik_23,R.drawable.f04_isi_sicaklik_24,R.drawable.f04_isi_sicaklik_25,
                            R.drawable.f04_isi_sicaklik_26,R.drawable.f04_isi_sicaklik_27,R.drawable.f04_isi_sicaklik_28,R.drawable.f04_isi_sicaklik_29,R.drawable.f04_isi_sicaklik_30,
                            R.drawable.f04_isi_sicaklik_31,R.drawable.f04_isi_sicaklik_32,R.drawable.f04_isi_sicaklik_33,R.drawable.f04_isi_sicaklik_34,R.drawable.f04_isi_sicaklik_35,
                            R.drawable.f04_isi_sicaklik_36,R.drawable.f04_isi_sicaklik_37,R.drawable.f04_isi_sicaklik_38,R.drawable.f04_isi_sicaklik_39,R.drawable.f04_isi_sicaklik_40,
                            R.drawable.f04_isi_sicaklik_41,R.drawable.f04_isi_sicaklik_42};
                    aokEEPROM(images, position, "Isı, Sicaklık, Hal Değişimi, Genleşme ");
                    break;
                case 7:
                    images = new int[]{ R.drawable.f05_basinc_kaldirma_basinc_01, R.drawable.f05_basinc_kaldirma_basinc_02,R.drawable.f05_basinc_kaldirma_basinc_03,R.drawable.f05_basinc_kaldirma_basinc_04,R.drawable.f05_basinc_kaldirma_basinc_05,
                            R.drawable.f05_basinc_kaldirma_basinc_06,R.drawable.f05_basinc_kaldirma_basinc_07,R.drawable.f05_basinc_kaldirma_basinc_08,R.drawable.f05_basinc_kaldirma_basinc_09,R.drawable.f05_basinc_kaldirma_basinc_10,
                            R.drawable.f05_basinc_kaldirma_basinc_11,R.drawable.f05_basinc_kaldirma_basinc_12,R.drawable.f05_basinc_kaldirma_basinc_13,R.drawable.f05_basinc_kaldirma_basinc_14,R.drawable.f05_basinc_kaldirma_basinc_15,
                            R.drawable.f05_basinc_kaldirma_basinc_16,R.drawable.f05_basinc_kaldirma_basinc_17,R.drawable.f05_basinc_kaldirma_basinc_18,R.drawable.f05_basinc_kaldirma_basinc_19,R.drawable.f05_basinc_kaldirma_basinc_20};
                    aokEEPROM(images, position, "Basınç ve Kaldırma Kuvveti: Basınç ");
                    break;
                case 8:
                    images = new int[]{ R.drawable.f05_basinc_kaldirma_kald_kuv_01,R.drawable.f05_basinc_kaldirma_kald_kuv_02,R.drawable.f05_basinc_kaldirma_kald_kuv_03,R.drawable.f05_basinc_kaldirma_kald_kuv_04,R.drawable.f05_basinc_kaldirma_kald_kuv_05,
                            R.drawable.f05_basinc_kaldirma_kald_kuv_06,R.drawable.f05_basinc_kaldirma_kald_kuv_07};
                    aokEEPROM(images, position, "Basınç ve Kaldırma Kuvveti: Kaldırma Kuvveti ");
                    break;
                case 9:
                    images = new int[]{ R.drawable.f06_elektrik_many_elektrostatik_01, R.drawable.f06_elektrik_many_elektrostatik_02,R.drawable.f06_elektrik_many_elektrostatik_03,R.drawable.f06_elektrik_many_elektrostatik_04,R.drawable.f06_elektrik_many_elektrostatik_05,
                            R.drawable.f06_elektrik_many_elektrostatik_06};
                    aokEEPROM(images, position, "Elektrik ve Manyetizma: Elektrostatik ");
                    break;
                case 10:
                    images = new int[]{ R.drawable.f06_elektrik_many_elektrikakimi_01, R.drawable.f06_elektrik_many_elektrikakimi_02,R.drawable.f06_elektrik_many_elektrikakimi_03,R.drawable.f06_elektrik_many_elektrikakimi_04,R.drawable.f06_elektrik_many_elektrikakimi_05,
                            R.drawable.f06_elektrik_many_elektrikakimi_06,R.drawable.f06_elektrik_many_elektrikakimi_07,R.drawable.f06_elektrik_many_elektrikakimi_08,R.drawable.f06_elektrik_many_elektrikakimi_09,R.drawable.f06_elektrik_many_elektrikakimi_10,
                            R.drawable.f06_elektrik_many_elektrikakimi_11,R.drawable.f06_elektrik_many_elektrikakimi_12,R.drawable.f06_elektrik_many_elektrikakimi_13,R.drawable.f06_elektrik_many_elektrikakimi_14,R.drawable.f06_elektrik_many_elektrikakimi_15,
                            R.drawable.f06_elektrik_many_elektrikakimi_16,R.drawable.f06_elektrik_many_elektrikakimi_17,R.drawable.f06_elektrik_many_elektrikakimi_18,R.drawable.f06_elektrik_many_elektrikakimi_19,R.drawable.f06_elektrik_many_elektrikakimi_20,
                            R.drawable.f06_elektrik_many_elektrikakimi_21,R.drawable.f06_elektrik_many_elektrikakimi_22,R.drawable.f06_elektrik_many_elektrikakimi_23};
                    aokEEPROM(images, position, "Elektrik ve Manyetizma: Elektrik Akımı ");
                    break;
                case 11:
                    images = new int[]{ R.drawable.f06_elektrik_many_manyetizma_01,R.drawable.f06_elektrik_many_manyetizma_02,R.drawable.f06_elektrik_many_manyetizma_03,R.drawable.f06_elektrik_many_manyetizma_04,R.drawable.f06_elektrik_many_manyetizma_05,
                            R.drawable.f06_elektrik_many_manyetizma_06,R.drawable.f06_elektrik_many_manyetizma_07,R.drawable.f06_elektrik_many_manyetizma_08,R.drawable.f06_elektrik_many_manyetizma_09,R.drawable.f06_elektrik_many_manyetizma_10,
                            R.drawable.f06_elektrik_many_manyetizma_11,R.drawable.f06_elektrik_many_manyetizma_12,R.drawable.f06_elektrik_many_manyetizma_13,R.drawable.f06_elektrik_many_manyetizma_14,R.drawable.f06_elektrik_many_manyetizma_15,
                            R.drawable.f06_elektrik_many_manyetizma_16,R.drawable.f06_elektrik_many_manyetizma_17,R.drawable.f06_elektrik_many_manyetizma_18,R.drawable.f06_elektrik_many_manyetizma_19,R.drawable.f06_elektrik_many_manyetizma_20};
                    aokEEPROM(images, position, "Elektrik ve Manyetizma: Mıknatıslar ve Manyetik Alan ");
                    break;
                case 12:
                    images = new int[]{ R.drawable.f07_optik_aydinlanma_golge_01,R.drawable.f07_optik_aydinlanma_golge_02,R.drawable.f07_optik_aydinlanma_golge_03,R.drawable.f07_optik_aydinlanma_golge_04,R.drawable.f07_optik_aydinlanma_golge_05,
                            R.drawable.f07_optik_aydinlanma_golge_06,R.drawable.f07_optik_aydinlanma_golge_07,R.drawable.f07_optik_aydinlanma_golge_08,R.drawable.f07_optik_aydinlanma_golge_09,R.drawable.f07_optik_aydinlanma_golge_10,
                            R.drawable.f07_optik_aydinlanma_golge_11,R.drawable.f07_optik_aydinlanma_golge_12,R.drawable.f07_optik_aydinlanma_golge_13,R.drawable.f07_optik_aydinlanma_golge_14,R.drawable.f07_optik_aydinlanma_golge_15};
                    aokEEPROM(images, position, "Optik: Aydınlanma ve Gölge");
                    break;
                case 13:
                    images = new int[]{ R.drawable.f07_optik_yans_duz_ayna_01,R.drawable.f07_optik_yans_duz_ayna_02,R.drawable.f07_optik_yans_duz_ayna_03,R.drawable.f07_optik_yans_duz_ayna_04,R.drawable.f07_optik_yans_duz_ayna_05,
                            R.drawable.f07_optik_yans_duz_ayna_06,R.drawable.f07_optik_yans_duz_ayna_07,R.drawable.f07_optik_yans_duz_ayna_08};
                    aokEEPROM(images, position, "Optik: Işığın Yansıması ve Düzlem Ayna");
                    break;
                case 14:
                    images = new int[]{ R.drawable.f07_optik_kuresel_ayna_01,R.drawable.f07_optik_kuresel_ayna_02,R.drawable.f07_optik_kuresel_ayna_03,R.drawable.f07_optik_kuresel_ayna_04,R.drawable.f07_optik_kuresel_ayna_05,
                            R.drawable.f07_optik_kuresel_ayna_06,R.drawable.f07_optik_kuresel_ayna_07,R.drawable.f07_optik_kuresel_ayna_08,R.drawable.f07_optik_kuresel_ayna_09};
                    aokEEPROM(images, position, "Optik: Küresel Aynalar");
                    break;
                case 15:
                    images = new int[]{ R.drawable.f07_optik_kirilma_renk_01,R.drawable.f07_optik_kirilma_renk_02,R.drawable.f07_optik_kirilma_renk_03,R.drawable.f07_optik_kirilma_renk_04,R.drawable.f07_optik_kirilma_renk_05,
                            R.drawable.f07_optik_kirilma_renk_06,R.drawable.f07_optik_kirilma_renk_07,R.drawable.f07_optik_kirilma_renk_08,R.drawable.f07_optik_kirilma_renk_09,R.drawable.f07_optik_kirilma_renk_10,
                            R.drawable.f07_optik_kirilma_renk_11,R.drawable.f07_optik_kirilma_renk_12,R.drawable.f07_optik_kirilma_renk_13,R.drawable.f07_optik_kirilma_renk_14,R.drawable.f07_optik_kirilma_renk_15,
                            R.drawable.f07_optik_kirilma_renk_16,R.drawable.f07_optik_kirilma_renk_17,R.drawable.f07_optik_kirilma_renk_18,R.drawable.f07_optik_kirilma_renk_19,R.drawable.f07_optik_kirilma_renk_20,
                            R.drawable.f07_optik_kirilma_renk_21};
                    aokEEPROM(images, position, "Optik: Işığın Kırılması ve Renk");
                    break;
                case 16:
                    images = new int[]{ R.drawable.f07_optik_mercek_optik_01,R.drawable.f07_optik_mercek_optik_02,R.drawable.f07_optik_mercek_optik_03,R.drawable.f07_optik_mercek_optik_04,R.drawable.f07_optik_mercek_optik_05,
                            R.drawable.f07_optik_mercek_optik_06,R.drawable.f07_optik_mercek_optik_07,R.drawable.f07_optik_mercek_optik_08,R.drawable.f07_optik_mercek_optik_09,R.drawable.f07_optik_mercek_optik_10,
                            R.drawable.f07_optik_mercek_optik_11,R.drawable.f07_optik_mercek_optik_12,R.drawable.f07_optik_mercek_optik_13,R.drawable.f07_optik_mercek_optik_14,R.drawable.f07_optik_mercek_optik_15,
                            R.drawable.f07_optik_mercek_optik_16,R.drawable.f07_optik_mercek_optik_17,R.drawable.f07_optik_mercek_optik_18,R.drawable.f07_optik_mercek_optik_19,R.drawable.f07_optik_mercek_optik_20};
                    aokEEPROM(images, position, "Optik: Mercekler ve Optik Araçlar");
                    break;
                case 17:
                    images = new int[]{ R.drawable.f08_dalgalar_genel_oz_01,R.drawable.f08_dalgalar_genel_oz_02,R.drawable.f08_dalgalar_genel_oz_03,R.drawable.f08_dalgalar_genel_oz_04,R.drawable.f08_dalgalar_genel_oz_05,
                            R.drawable.f08_dalgalar_genel_oz_06,R.drawable.f08_dalgalar_genel_oz_07,R.drawable.f08_dalgalar_genel_oz_08,R.drawable.f08_dalgalar_genel_oz_09,R.drawable.f08_dalgalar_genel_oz_10};
                    aokEEPROM(images, position, "Dalgalar: Dalgaların Genel Özellikleri");
                    break;
                case 18:
                    images = new int[]{ R.drawable.f08_dalgalar_yay_dalgalari_01,R.drawable.f08_dalgalar_yay_dalgalari_02,R.drawable.f08_dalgalar_yay_dalgalari_03,R.drawable.f08_dalgalar_yay_dalgalari_04,R.drawable.f08_dalgalar_yay_dalgalari_05,
                            R.drawable.f08_dalgalar_yay_dalgalari_06,R.drawable.f08_dalgalar_yay_dalgalari_07,R.drawable.f08_dalgalar_yay_dalgalari_08,R.drawable.f08_dalgalar_yay_dalgalari_09,R.drawable.f08_dalgalar_yay_dalgalari_10};
                    aokEEPROM(images, position, "Dalgalar: Yay Dalgaları");
                    break;
                case 19:
                    images = new int[]{ R.drawable.f08_dalgalar_su_dalgalari_01,R.drawable.f08_dalgalar_su_dalgalari_02,R.drawable.f08_dalgalar_su_dalgalari_03,R.drawable.f08_dalgalar_su_dalgalari_04,R.drawable.f08_dalgalar_su_dalgalari_05,
                            R.drawable.f08_dalgalar_su_dalgalari_06,R.drawable.f08_dalgalar_su_dalgalari_07,R.drawable.f08_dalgalar_su_dalgalari_08,R.drawable.f08_dalgalar_su_dalgalari_09,R.drawable.f08_dalgalar_su_dalgalari_10,
                            R.drawable.f08_dalgalar_su_dalgalari_11,R.drawable.f08_dalgalar_su_dalgalari_12};
                    aokEEPROM(images, position, "Dalgalar: Su Dalgaları");
                    break;
                case 20:
                    images = new int[]{ R.drawable.f08_dalgalar_ses_deprem_01,R.drawable.f08_dalgalar_ses_deprem_02,R.drawable.f08_dalgalar_ses_deprem_03,R.drawable.f08_dalgalar_ses_deprem_04,R.drawable.f08_dalgalar_ses_deprem_05,
                            R.drawable.f08_dalgalar_ses_deprem_06,R.drawable.f08_dalgalar_ses_deprem_07,R.drawable.f08_dalgalar_ses_deprem_08,R.drawable.f08_dalgalar_ses_deprem_09,R.drawable.f08_dalgalar_ses_deprem_10,
                            R.drawable.f08_dalgalar_ses_deprem_11,R.drawable.f08_dalgalar_ses_deprem_12,R.drawable.f08_dalgalar_ses_deprem_13,R.drawable.f08_dalgalar_ses_deprem_14,R.drawable.f08_dalgalar_ses_deprem_15,
                            R.drawable.f08_dalgalar_ses_deprem_16,R.drawable.f08_dalgalar_ses_deprem_17};
                    aokEEPROM(images, position, "Dalgalar: Ses ve Deprem Dalgaları");
                    break;

                default: break;
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */



    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
        //    return true;
        //}
       // return super.onOptionsItemSelected(item);
    //}


    private void aokEEPROM(int []img, int ps, String title)
    {
        eeprom = new EEPROM();
        eeprom.setListCount(img.length);
        for(i=0;i<eeprom.getCount();i++)
        {
            eeprom.write(i, img[i], title);
        }
        aokIntent(ps);
    }
    private void aokIntent(int ps)  //intent ile FizikGoster sayfasına Konu İd sini gönderiyrouz
    {
        intent = new Intent(context, FormulGoster.class);
        intent.cloneFilter();
        intent.putExtra("brans", 0);
        intent.putExtra("id", ps);
        startActivity(intent);
    }



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        return super.onOptionsItemSelected(item);
    }
*/








//Google Addmob REKLAM
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView1!=null) {
            mAdView1.pause();
        }
        super.onPause();
    }
    /** Called when returning to the activity */
    @Override
    public void onResume() {
        if (mAdView1!=null) {
            mAdView1.resume();
        }
        super.onResume();
    }
    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView1!=null) {
            mAdView1.destroy();
        }
        super.onDestroy();
    }
//Google Addmob REKLAM










    //PAYLAŞ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.kapat) {
            kapat();
            return true;
        } else if (item.getItemId() == R.id.paylas) {
            paylas();
            return true;
        } else if (item.getItemId() == R.id.anasayfa) {
            anasayfa();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void kapat() {
        this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);     //System.exit() yığında birden fazla etkinliğiniz varsa uygulamanızı öldürmez
        getParent().finish();
        //ActivityCompat.finishAffinity(MainActivity.this);
        //finishAndRemoveTask();
    }
    public void anasayfa(){
        Intent myIntent = new Intent(getApplicationContext(), UygulamaAnaSayfa.class);
        startActivity(myIntent);
    }
    public void paylas(){
        String paylasmesajbasligii = "YKS Tyt Fizik Formülleri\n";
        String paylasmesaji = "Tyt Fiziğindeki tüm Fizik Formülleri Elinizin Altında "
                +"Yanınızda kitap taşımanıza gerek yok\n"
                +"https://play.google.com/store/apps/details?id=com.mobilprogramlar.FizikFormullerim";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, paylasmesajbasligii);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, paylasmesaji);
        startActivity(Intent.createChooser(sharingIntent, "Paylaşmak İçin Tıklayınız"));
    }
    //PAYLAŞ








}