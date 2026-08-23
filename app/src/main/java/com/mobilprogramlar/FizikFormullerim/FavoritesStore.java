package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public final class FavoritesStore {
    private static final String PREFS = "fizik_favorites";
    private static final String KEY_IDS = "topic_ids";

    private FavoritesStore() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static Set<Integer> getFavoriteIds(Context context) {
        Set<String> raw = prefs(context).getStringSet(KEY_IDS, null);
        Set<Integer> ids = new HashSet<>();
        if (raw == null) {
            return ids;
        }
        for (String s : raw) {
            try {
                ids.add(Integer.parseInt(s));
            } catch (NumberFormatException ignored) {
            }
        }
        return ids;
    }

    public static boolean isFavorite(Context context, int topicId) {
        return getFavoriteIds(context).contains(topicId);
    }

    public static void toggle(Context context, int topicId) {
        Set<Integer> ids = getFavoriteIds(context);
        if (ids.contains(topicId)) {
            ids.remove(topicId);
        } else {
            ids.add(topicId);
        }
        Set<String> raw = new HashSet<>();
        for (Integer id : ids) {
            raw.add(String.valueOf(id));
        }
        prefs(context).edit().putStringSet(KEY_IDS, raw).apply();
    }
}
