package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;

public class Giris extends AppCompatActivity {
    private KenBurnsView kbv;
    private boolean moving = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.giris);



        //Giriş ekranı açılsın 3 saniye geçsin sonra gerçek programımıza yönlenelim
        //Bunun için Thread clasını  kullanıyoruz
        Thread zamanlayici= new Thread(){   //Altta yapacakları tanımlanacak
            public void run(){      //Hiç bir şey yapmasın beklesin sonra geçsin
                try {
                    sleep(3000);        //6000 milisaniye uyu
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Log.i("tago","Zamanlayıcı Çalışmadı");
                }finally {      //Try dan sonra mesaj ver yada verme sonunda final olarak aşağıyı yap
                    Intent i = new Intent(Giris.this, UygulamaAnaSayfa.class);
                    finish();
                    startActivity(i);
                }
            }
        };
        zamanlayici.start();    //Zamanlayıcıyı kullanabilmemiz için start metodu ie başlatmamız lazım











        kbv = findViewById(R.id.kbv);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(3000, adi);
        kbv.setTransitionGenerator(generator);
        kbv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moving) {
                    kbv.pause();
                    moving = false;
                } else {
                    kbv.resume();
                    moving = true;
                }
            }
        });
        kbv.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                //Toast.makeText(Giris.this, "Started", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTransitionEnd(Transition transition) {
                //Toast.makeText(Giris.this, "Finished", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
