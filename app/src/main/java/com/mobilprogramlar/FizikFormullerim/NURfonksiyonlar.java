package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NURfonksiyonlar {

    Context cnn;

    public void paylas(Context context, String baslik, String mesaj) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, baslik);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mesaj);
        context.startActivity(Intent.createChooser(sharingIntent, "Paylaşmak İçin Tıklayınız"));
    }



    //KULLANIMI
    //new SnackBarOzel().ozelSnackBar(context.getApplicationContext(),view, nuri.getSoru(),7000);
    public void ozelSnackBar(Context context, View v, String msg, int millisec) {
        Snackbar snackbar_alt = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        View snackBarView_ALT = snackbar_alt.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackBarView_ALT.getLayoutParams();
        params.gravity = Gravity.BOTTOM;

        snackBarView_ALT.setLayoutParams(params);
        snackBarView_ALT.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));   //snackbar_alt BACKGROUN COLOR

        //TextView mainTextView =  (snackBarView_ALT).findViewById(R.id.snackbar_text);
        TextView mainTextView = (snackBarView_ALT).findViewById(com.google.android.material.R.id.snackbar_text);
        mainTextView.setMaxLines(10);       //10 satır maksimum
        mainTextView.setTextColor(Color.WHITE);
        mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);  //Mesajı ORTALAR
        mainTextView.setTextSize(14);
        mainTextView.setPadding(0,0,0,0);

        snackbar_alt.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);     //Animasyon veriyor
        snackbar_alt.setDuration(millisec);     //snackbar_alt 5 saniye bekliyor

        //SpannableStringBuilder builder = new SpannableStringBuilder();
        //builder.append("My message ").append(" ");
        //builder.setSpan(new ImageSpan(context, R.drawable.ic_paylas), builder.length() - 1, builder.length(), 0);

        snackbar_alt.setActionTextColor(Color.BLUE);
        //snackbar_alt buttonuna basıldığında
        snackbar_alt.setAction("Tamam", view -> {
        });
        snackbar_alt.show();

        /*
                Snackbar snackbar;
                snackbar = Snackbar.make(view, cevap_Getir(gosterilen_soruID), Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
                snackBarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_background));
                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_text));
                snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.uyari_textColor_1));
                snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_background));
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(3000);
                snackbar.setAction("Tamam", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                snackbar.show();
                */
    }
















    //KULLANIMI
    //new ToastMesajOzel().gosterTOAST(context.getApplicationContext(),"Sorunun Tamamı",nuri.getSoru(),15000);
    public void gosterTOAST(Context context, String Baslik, String msg,int millisec)
    {
        Handler handler = null;
        final Toast[] toast_sureli = new Toast[1];
        for(int i = 0; i < millisec; i+=8000) {
            LayoutInflater inflater = LayoutInflater.from(context);

            //View layout = inflater.inflate(R.layout.toast_ekrani, null );
            // Doğru kullanım
            View layout = inflater.inflate(R.layout.toast_ekrani, null, false);
            TextView baslikText = layout.findViewById(R.id.toast_baslik);
            TextView textMesaj = layout.findViewById(R.id.toast_mesaj);
            baslikText.setText(Baslik);
            textMesaj.setText(msg);
            toast_sureli[0] = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast_sureli[0].setGravity(Gravity.BOTTOM, 0, 0);
            toast_sureli[0].setDuration(Toast.LENGTH_LONG);
            toast_sureli[0].setView(layout);  //setView yani özel toast kullanımdan kaldırıldı. Bunun yerine SnackBar kullan
            toast_sureli[0].show();
            if(handler == null) {
                handler = new Handler();
                handler.postDelayed(() -> toast_sureli[0].cancel(), millisec);
            }
        }
     }

    public void sureliTOAST(Context context, String msg, int millisec) {
        Handler handler = null;
        final Toast[] toast_sureli = new Toast[1];
        for(int i = 0; i < millisec; i+=8000) {
            toast_sureli[0] = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast_sureli[0].show();
            if(handler == null) {
                handler = new Handler();
                handler.postDelayed(() -> toast_sureli[0].cancel(), millisec);
            }
        }
    }





    public static int reklamIhtimali = 0;    //Reklam yuzde değeri buradan belirlenir.

    public static int getRandom(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min " + min + " greater than max " + max);
        }
        return (int) ( (long) min + Math.random() * ((long)max - min + 1));
    }

    public boolean reklamGoster(Context context, int yuzdeIhtimal) {
        boolean goster = false;
        //sonuc = kosul ? dogruIseCikacakSonuc : yanlisIseCikacakSonuc
        yuzdeIhtimal = Math.min(yuzdeIhtimal, 100);
        yuzdeIhtimal = Math.max(yuzdeIhtimal, 0);
        int rdr = getRandom(0,100);
       /*
        Toast.makeText(context, "ihtimal:"+String.valueOf(yuzdeIhtimal)+"\n"+""+String.valueOf(rdr)+" <"+String.valueOf(yuzdeIhtimal)
                +"\nSonuc: "+(rdr<=yuzdeIhtimal?"Dogru":"Yanlis")
                , Toast.LENGTH_LONG).show();
        */
        if(rdr<=yuzdeIhtimal)
        {
            new NURreklam().reklamGoster(context);
            goster = true;
        }
        return goster;
    }














}
