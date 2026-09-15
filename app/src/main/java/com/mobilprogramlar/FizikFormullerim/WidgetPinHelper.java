package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/** Widget tür seçici (Üç İşlem tarzı kart) → pin. */
public final class WidgetPinHelper {

    private WidgetPinHelper() {
    }

    public static void showPicker(@NonNull AppCompatActivity activity) {
        View root = LayoutInflater.from(activity).inflate(R.layout.dialog_widget_picker, null, false);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(root)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        root.findViewById(R.id.btn_ekle_formula).setOnClickListener(v -> {
            dialog.dismiss();
            pin(activity, FormulaOfDayWidgetProvider.class);
        });
        View rateRow = root.findViewById(R.id.row_rate_from_widget);
        if (rateRow != null) {
            rateRow.setVisibility(View.VISIBLE);
        }
        View btnRate = root.findViewById(R.id.btn_rate_from_widget);
        if (btnRate != null) {
            btnRate.setOnClickListener(v -> {
                dialog.dismiss();
                ReviewHelper.askFromButton(activity);
            });
        }
        root.findViewById(R.id.btn_widget_iptal).setOnClickListener(v -> dialog.dismiss());
        try {
            ThemeColors tc = new ThemeManager(activity).getThemeColors();
            com.google.android.material.button.MaterialButton ekle = root.findViewById(R.id.btn_ekle_formula);
            com.google.android.material.button.MaterialButton rate = root.findViewById(R.id.btn_rate_from_widget);
            android.widget.TextView iptal = root.findViewById(R.id.btn_widget_iptal);
            if (ekle != null) {
                ekle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tc.toolbarBackgroundColor));
                ekle.setTextColor(DialogFit.contrastingOn(tc.toolbarBackgroundColor));
            }
            if (rate != null) {
                rate.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tc.toolbarBackgroundColor));
                rate.setTextColor(DialogFit.contrastingOn(tc.toolbarBackgroundColor));
            }
            if (iptal != null) {
                iptal.setTextColor(tc.toolbarBackgroundColor);
            }
        } catch (Throwable ignored) {
        }
        dialog.show();
        DialogFit.apply(dialog);
    }

    public static void pin(@NonNull Activity activity, @NonNull Class<?> providerClass) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mgr.isRequestPinAppWidgetSupported()) {
            ComponentName name = new ComponentName(activity, providerClass);
            Intent callback = new Intent(activity, providerClass);
            callback.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            PendingIntent success = PendingIntent.getBroadcast(
                    activity,
                    providerClass.getName().hashCode(),
                    callback,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            boolean ok = mgr.requestPinAppWidget(name, null, success);
            if (!ok) {
                Toast.makeText(activity, R.string.widget_pin_unsupported, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, R.string.widget_pin_prompt, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, R.string.widget_pin_manual_hint, Toast.LENGTH_LONG).show();
        }
    }
}
