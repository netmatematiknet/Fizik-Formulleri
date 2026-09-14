package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/** Toolbar: 1. satır uygulama adı, 2. satır sayfa/konu (veya ana sayfada site). */
public final class ToolbarHelper {

    public static final int SUBTITLE_MAX_CHARS = 28;

    private ToolbarHelper() {
    }

    public static void bindHome(@NonNull Activity activity) {
        TextView title = activity.findViewById(R.id.toolbar_title);
        TextView subtitle = activity.findViewById(R.id.toolbar_subtitle);
        if (title != null) {
            title.setText(R.string.toolbar_baslik_default);
        }
        if (subtitle != null) {
            subtitle.setText(R.string.toolbar_altbaslik_site);
            subtitle.setTextColor(ContextCompat.getColor(activity, R.color.toolbar_subtitle_readable));
        }
    }

    public static void bindPage(@NonNull Activity activity, @Nullable String pageName) {
        TextView title = activity.findViewById(R.id.toolbar_title);
        TextView subtitle = activity.findViewById(R.id.toolbar_subtitle);
        if (title != null) {
            title.setText(R.string.toolbar_baslik_default);
        }
        if (subtitle != null) {
            String page = pageName == null || pageName.trim().isEmpty()
                    ? activity.getString(R.string.toolbar_ayarlar)
                    : pageName.trim();
            subtitle.setText(UiScale.ellipsizeSubtitle(page, SUBTITLE_MAX_CHARS));
            subtitle.setTextColor(ContextCompat.getColor(activity, R.color.toolbar_subtitle_readable));
        }
    }

    /** Liste / detay: alt satırda konu (kısaltılmış). */
    public static void bindTopic(@NonNull Activity activity, @Nullable String topic) {
        TextView title = activity.findViewById(R.id.toolbar_title);
        TextView subtitle = activity.findViewById(R.id.toolbar_subtitle);
        if (title != null) {
            title.setText(R.string.toolbar_baslik_default);
        }
        if (subtitle != null) {
            String t = topic == null || topic.trim().isEmpty()
                    ? activity.getString(R.string.toolbar_baslik_default)
                    : topic.trim();
            subtitle.setText(UiScale.ellipsizeSubtitle(t, SUBTITLE_MAX_CHARS));
            subtitle.setTextColor(ContextCompat.getColor(activity, R.color.toolbar_subtitle_readable));
        }
    }
}
