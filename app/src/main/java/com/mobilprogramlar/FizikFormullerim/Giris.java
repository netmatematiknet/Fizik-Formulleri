package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;

public class Giris extends AppCompatActivity {
    private KenBurnsView kbv;
    private boolean moving = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.giris);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(Giris.this, UygulamaAnaSayfa.class));
            finish();
        }, 3000);

        kbv = findViewById(R.id.kbv);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(3000, adi);
        kbv.setTransitionGenerator(generator);

        kbv.setOnClickListener(v -> {
            if (moving) {
                kbv.pause();
                moving = false;
            } else {
                kbv.resume();
                moving = true;
            }
        });

        kbv.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }
        });
    }
}
