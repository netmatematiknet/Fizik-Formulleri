package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;

public class Giris extends AppCompatActivity {
    private KenBurnsView kbv;
    private boolean moving = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        LocaleManager lm = new LocaleManager(newBase);
        super.attachBaseContext(lm.setLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge cutout'u yönetir; deprecated SHORT_EDGES kullanılmaz.
        NtHelper.enableEdgeToEdge(this);
        setContentView(R.layout.giris);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(Giris.this, MainActivity.class));
            finish();
        }, 2200);

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

    @Override
    protected void onPause() {
        super.onPause();
        if (kbv != null) {
            kbv.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (kbv != null) {
            kbv.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kbv = null;
    }
}
