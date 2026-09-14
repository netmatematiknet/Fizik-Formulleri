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
import com.google.android.material.button.MaterialButton;

/**
 * Kendi uygulama tanıtımı — tanitim_* (AdMob değil).
 */
public final class TanitimHelper {

    private TanitimHelper() {
    }

    public static void bind(@NonNull Activity activity, @Nullable ViewGroup host, @NonNull String place) {
        if (host == null) {
            return;
        }
        host.removeAllViews();
        AppRemoteConfig rc = AppRemoteConfig.getInstance(activity);
        if (!rc.isPromoEnabled() || !rc.isPromoPlace(place)) {
            host.setVisibility(View.GONE);
            return;
        }
        String url = rc.getPromoUrl();
        if (TextUtils.isEmpty(url)) {
            host.setVisibility(View.GONE);
            return;
        }
        String id = rc.getPromoId();
        if (!TextUtils.isEmpty(id) && AppPrefs.isPromoDismissed(activity, id.trim())) {
            host.setVisibility(View.GONE);
            return;
        }
        String title = rc.getPromoTitle();
        String message = rc.getPromoMessage();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(message)) {
            host.setVisibility(View.GONE);
            return;
        }
        View root = activity.getLayoutInflater().inflate(R.layout.include_tanitim, host, false);
        host.addView(root);
        host.setVisibility(View.VISIBLE);
        root.setVisibility(View.VISIBLE);

        TextView tvTitle = root.findViewById(R.id.tanitim_baslik);
        TextView tvMsg = root.findViewById(R.id.tanitim_mesaj);
        MaterialButton btn = root.findViewById(R.id.tanitim_buton);
        ImageView img = root.findViewById(R.id.tanitim_resim);

        tvTitle.setText(TextUtils.isEmpty(title) ? activity.getString(R.string.tanitim_baslik_varsayilan) : title.trim());
        if (TextUtils.isEmpty(message)) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(message.trim());
        }
        String button = rc.getPromoButton();
        btn.setText(TextUtils.isEmpty(button)
                ? activity.getString(R.string.tanitim_incele)
                : button.trim());

        String imageUrl = rc.getPromoImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(activity).load(imageUrl.trim()).circleCrop().into(img);
        }

        String finalUrl = url.trim();
        String finalId = id == null ? "" : id.trim();
        View.OnClickListener open = v -> PlayHelper.openUrl(activity, finalUrl);
        btn.setOnClickListener(open);
        root.setOnClickListener(open);

        ImageButton close = root.findViewById(R.id.tanitim_kapat);
        close.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(finalId)) {
                AppPrefs.setPromoDismissed(activity, finalId);
            }
            host.setVisibility(View.GONE);
            host.removeAllViews();
        });
    }
}
