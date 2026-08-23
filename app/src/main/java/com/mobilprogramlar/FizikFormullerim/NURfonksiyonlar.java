package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NURfonksiyonlar {

    public void paylas(Context context, String baslik, String mesaj) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, baslik);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, mesaj);
        context.startActivity(Intent.createChooser(sharingIntent, "Paylaşmak İçin Tıklayınız"));
    }

    public void ozelSnackBar(Context context, View v, String msg, int millisec) {
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        snackBarView.setLayoutParams(params);
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
        TextView mainTextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        mainTextView.setMaxLines(10);
        mainTextView.setTextColor(Color.WHITE);
        mainTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        mainTextView.setTextSize(14);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.setDuration(millisec);
        snackbar.setActionTextColor(Color.BLUE);
        snackbar.setAction("Tamam", view -> {
        });
        snackbar.show();
    }

    public static void openDeveloperPage(Context context, String developerName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + developerName));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=" + developerName));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
