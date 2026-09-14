package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Fizik formül görselleri TopicCatalog + drawable PNG üzerinden yüklenir.
 * Matematik assets/WebP yolu kullanılmaz.
 */
public class IconManager {
    private static final String TAG = "IconManager";

    private final Context context;

    public IconManager(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    public int[] getDrawableIdsForTopic(@Nullable String topicTitle) {
        if (topicTitle == null || topicTitle.isEmpty()) {
            return new int[0];
        }
        for (TopicCatalog.Topic topic : TopicCatalog.all()) {
            if (topicTitle.equals(topic.title)) {
                return topic.imageResIds != null ? topic.imageResIds : new int[0];
            }
        }
        return new int[0];
    }

    /** Geriye uyumluluk: dosya adı yerine drawable id string. */
    @NonNull
    public String[] getImagesForTopic(@Nullable String category, @Nullable String topic) {
        int[] ids = getDrawableIdsForTopic(topic);
        String[] out = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            out[i] = String.valueOf(ids[i]);
        }
        return out;
    }

    public int getNumberOfImagesForTopic(@Nullable String category, @Nullable String topic) {
        return getDrawableIdsForTopic(topic).length;
    }

    public int getCategoryIndex(@Nullable String categoryName) {
        if (categoryName == null) {
            return -1;
        }
        String[] main = context.getResources().getStringArray(R.array.main_topics);
        for (int i = 0; i < main.length; i++) {
            if (categoryName.equals(main[i])) {
                return i;
            }
        }
        return -1;
    }

    /** Ana sayfa kart görselleri — Fizik + Uygulamalarımız + Ayarlar. */
    public int[] getImagesForCategories() {
        return new int[]{
                R.drawable.einstein_equation,
                R.drawable.uygulamalarimiz,
                R.mipmap.ayarlar_01
        };
    }

    @Nullable
    public Bitmap loadDrawableSampled(@DrawableRes int resId) {
        if (resId == 0) {
            return null;
        }
        try {
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, bounds);

            int maxSide = Math.max(getScreenWidth(), getScreenHeight());
            int sample = 1;
            int longest = Math.max(bounds.outWidth, bounds.outHeight);
            while (longest / sample > maxSide && sample < 16) {
                sample *= 2;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = sample;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(context.getResources(), resId, opts);
        } catch (Exception e) {
            Log.e(TAG, "Drawable yüklenemedi: " + resId, e);
            return null;
        }
    }

    /** Eski WebP API — Fizik'te drawable id string bekler. */
    @Nullable
    public Bitmap loadWebPImage(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        try {
            int id = Integer.parseInt(fileName);
            return loadDrawableSampled(id);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Geçersiz drawable id: " + fileName);
            return null;
        }
    }

    public static int[] getTabIcons() {
        return new int[]{
                R.drawable.number_m_01, R.drawable.number_m_02, R.drawable.number_m_03, R.drawable.number_m_04,
                R.drawable.number_m_05, R.drawable.number_m_06, R.drawable.number_m_07, R.drawable.number_m_08,
                R.drawable.number_m_09, R.drawable.number_m_10, R.drawable.number_m_11, R.drawable.number_m_12,
                R.drawable.number_m_13, R.drawable.number_m_14, R.drawable.number_m_15, R.drawable.number_m_16,
                R.drawable.number_m_17, R.drawable.number_m_18, R.drawable.number_m_19, R.drawable.number_m_20,
                R.drawable.number_m_21, R.drawable.number_m_22, R.drawable.number_m_23, R.drawable.number_m_24
        };
    }

    public static int[] getTabIconsActive() {
        return new int[]{
                R.drawable.number_k_01, R.drawable.number_k_02, R.drawable.number_k_03, R.drawable.number_k_04,
                R.drawable.number_k_05, R.drawable.number_k_06, R.drawable.number_k_07, R.drawable.number_k_08,
                R.drawable.number_k_09, R.drawable.number_k_10, R.drawable.number_k_11, R.drawable.number_k_12,
                R.drawable.number_k_13, R.drawable.number_k_14, R.drawable.number_k_15, R.drawable.number_k_16,
                R.drawable.number_k_17, R.drawable.number_k_18, R.drawable.number_k_19, R.drawable.number_k_20,
                R.drawable.number_k_21, R.drawable.number_k_22, R.drawable.number_k_23, R.drawable.number_k_24
        };
    }

    private int getScreenWidth() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /** Konu başlık listesi (Formula_List için). */
    @NonNull
    public static String[] physicsTopicTitles() {
        List<TopicCatalog.Topic> topics = TopicCatalog.all();
        String[] titles = new String[topics.size()];
        for (int i = 0; i < topics.size(); i++) {
            titles[i] = topics.get(i).title;
        }
        return titles;
    }
}
