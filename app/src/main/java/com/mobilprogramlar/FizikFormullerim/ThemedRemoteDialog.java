package com.mobilprogramlar.FizikFormullerim;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

/**
 * Temalı Remote Config diyalogları (kutlama / bilgi / bakım).
 */
public final class ThemedRemoteDialog {

    public interface Action {
        void onPrimary();
    }

    private ThemedRemoteDialog() {
    }

    public static void show(
            @NonNull AppCompatActivity activity,
            @NonNull String title,
            @NonNull String body,
            @Nullable String buttonText,
            @Nullable String imageUrl,
            @DrawableRes int fallbackIcon,
            boolean cancelable,
            @Nullable Action action) {
        show(activity, title, body, buttonText, imageUrl, fallbackIcon, cancelable, action, null);
    }

    public static void show(
            @NonNull AppCompatActivity activity,
            @NonNull String title,
            @NonNull String body,
            @Nullable String buttonText,
            @Nullable String imageUrl,
            @DrawableRes int fallbackIcon,
            boolean cancelable,
            @Nullable Action action,
            @Nullable Runnable onCancel) {
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        View root = LayoutInflater.from(activity).inflate(R.layout.dialog_announcement, null, false);
        ((TextView) root.findViewById(R.id.tv_announce_title)).setText(title);
        ((TextView) root.findViewById(R.id.tv_announce_body)).setText(body);
        MaterialButton btn = root.findViewById(R.id.btn_announce_ok);
        if (!TextUtils.isEmpty(buttonText)) {
            btn.setText(buttonText.trim());
        }
        try {
            ThemeColors tc = new ThemeManager(activity).getThemeColors();
            TextView titleView = root.findViewById(R.id.tv_announce_title);
            TextView bodyView = root.findViewById(R.id.tv_announce_body);
            if (titleView != null) {
                titleView.setTextColor(tc.toolbarBackgroundColor != 0
                        ? tc.toolbarBackgroundColor : tc.activityTextColor);
            }
            if (bodyView != null) {
                bodyView.setTextColor(tc.activityTextColor);
            }
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(tc.toolbarBackgroundColor));
            btn.setTextColor(tc.activityTextColor);
        } catch (Throwable ignored) {
        }
        ImageView icon = root.findViewById(R.id.iv_announce_icon);
        if (icon != null) {
            icon.setImageResource(fallbackIcon);
            icon.clearColorFilter();
            icon.setColorFilter(activity.getResources().getColor(R.color.beyaz, activity.getTheme()));
        }
        ImageView image = root.findViewById(R.id.iv_announce_image);
        if (image != null) {
            if (!TextUtils.isEmpty(imageUrl)) {
                image.setVisibility(View.VISIBLE);
                Glide.with(activity).load(imageUrl.trim()).centerCrop().into(image);
            } else {
                image.setVisibility(View.GONE);
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(root)
                .setCancelable(cancelable)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        btn.setOnClickListener(v -> {
            if (action != null) {
                action.onPrimary();
            }
            dialog.dismiss();
        });
        if (cancelable && onCancel != null) {
            dialog.setOnCancelListener(d -> onCancel.run());
        }
        dialog.show();
    }
}
