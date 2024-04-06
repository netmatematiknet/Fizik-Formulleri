package com.mobilprogramlar.FizikFormullerim;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class AcilisEkrani extends Activity {

        public void onCreate (Bundle bambam){   //onCreate metoduyla aktivity yi çağırıyoruz.
            super.onCreate(bambam);     //superini çağırıyoruz kendi oluşturması için
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.acilisekrani);   //Acitivity mizi girişekranıyla ilişkilendirelim
        //Giriş ekranı açılsın 3 saniye geçsin sonra gerçek programımıza yönlenelim, Bunun için Thread clasını  kullanıyoruz

            Thread zamanlayici= new Thread(){   //Altta yapacakları tanımlanacak
        public void run(){      //Hiç bir şey yapmasın beklesin sonra geçsin
            try {
                sleep(2000);        //6000 milisaniye uyu
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("tago","Zamanlayıcı Çalışmadı");
            }finally {      //Try dan sonra mesaj ver yada verme sonunda final olarak aşağıyı yap
                Intent i = new Intent(AcilisEkrani.this, UygulamaAnaSayfa.class);
                finish();
                startActivity(i);
            }
        }
        };
            zamanlayici.start();    //Zamanlayıcıyı kullanabilmemiz için start metodu ie başlatmamız lazım



    }
}
