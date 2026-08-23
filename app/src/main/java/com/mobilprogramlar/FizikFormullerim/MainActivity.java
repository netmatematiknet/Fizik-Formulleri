package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/** Eski giriş noktası; ana sayfaya yönlendirir. */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, UygulamaAnaSayfa.class));
        finish();
    }
}
