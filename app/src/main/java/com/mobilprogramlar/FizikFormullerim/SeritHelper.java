package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

/**
 * Ana ekran haber bandı — serit_* (push layout, overlay değil).
 */
public final class SeritHelper {

    private SeritHelper() {
    }

    public static void bind(@NonNull Activity activity, @Nullable ViewGroup host) {
        if (host == null) {
            return;
        }
        host.removeAllViews();
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        if (!rc.isStripEnabled()) {
            host.setVisibility(View.GONE);
            return;
        }
        String text = rc.getStripText();
        if (TextUtils.isEmpty(text)) {
            host.setVisibility(View.GONE);
            return;
        }
        String key = text.trim();
        if (AppPrefs.isStripHidden(activity, key)) {
            host.setVisibility(View.GONE);
            return;
        }
        View root = activity.getLayoutInflater().inflate(R.layout.include_serit, host, false);
        host.addView(root);
        host.setVisibility(View.VISIBLE);
        root.setVisibility(View.VISIBLE);

        TextView tv = root.findViewById(R.id.serit_metin);
        tv.setText(key);
        ImageView img = root.findViewById(R.id.serit_resim);
        String imageUrl = rc.getStripImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            img.setVisibility(View.VISIBLE);
            Glide.with(activity).load(imageUrl.trim()).circleCrop().into(img);
        } else {
            img.setVisibility(View.GONE);
        }
        String url = rc.getStripUrl();
        View.OnClickListener open = v -> {
            if (!TextUtils.isEmpty(url)) {
                PlayHelper.openUrl(activity, url.trim());
            }
        };
        root.setOnClickListener(open);
        tv.setOnClickListener(open);
        ImageButton close = root.findViewById(R.id.serit_kapat);
        close.setOnClickListener(v -> {
            AppPrefs.setStripHidden(activity, key);
            host.setVisibility(View.GONE);
            host.removeAllViews();
        });
    }
}
