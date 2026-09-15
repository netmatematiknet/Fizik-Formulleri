package com.mobilprogramlar.FizikFormullerim;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * Açık zeminli popup'ları ekran genişliğine sığdırır ve ortalar.
 * Transparent arka planlı AlertDialog'larda WRAP_CONTENT taşmasını engeller.
 */
public final class DialogFit {

    private static final float SIDE_MARGIN_DP = 20f;
    private static final float MAX_WIDTH_DP = 420f;

    private DialogFit() {
    }

    public static void apply(@NonNull AlertDialog dialog) {
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        DisplayMetrics dm = dialog.getContext().getResources().getDisplayMetrics();
        int side = Math.round(SIDE_MARGIN_DP * dm.density);
        int max = Math.round(MAX_WIDTH_DP * dm.density);
        int available = Math.max(0, dm.widthPixels - side * 2);
        int width = Math.min(available, max);
        if (width <= 0) {
            width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    /** Mor/renkli buton üstünde okunaklı yazı (açık temada beyaz kalır). */
    public static int contrastingOn(int backgroundColor) {
        double r = Color.red(backgroundColor) / 255.0;
        double g = Color.green(backgroundColor) / 255.0;
        double b = Color.blue(backgroundColor) / 255.0;
        double lum = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        return lum > 0.62 ? Color.BLACK : Color.WHITE;
    }
}
