package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Ana kategoriler: 0 = Fizik Formülleri, 1 = Uygulamalarımız, 2 = Ayarlar.
 */
public final class CategoryHelper {

    public static final int INDEX_PHYSICS = 0;
    public static final int INDEX_APPLICATIONS = 1;
    public static final int INDEX_SETTINGS = 2;

    private CategoryHelper() {
    }

    public static boolean isApplicationsIndex(int categoryIndex) {
        return categoryIndex == INDEX_APPLICATIONS;
    }

    public static boolean isSettingsIndex(int categoryIndex) {
        return categoryIndex == INDEX_SETTINGS;
    }

    public static boolean isApplications(@NonNull Context context, @Nullable String category) {
        if (category == null) {
            return false;
        }
        String[] mainTopics = context.getResources().getStringArray(R.array.main_topics);
        return mainTopics.length > INDEX_APPLICATIONS
                && category.equals(mainTopics[INDEX_APPLICATIONS]);
    }

    public static boolean isSettings(@NonNull Context context, @Nullable String category) {
        if (category == null) {
            return false;
        }
        String[] mainTopics = context.getResources().getStringArray(R.array.main_topics);
        return mainTopics.length > INDEX_SETTINGS
                && category.equals(mainTopics[INDEX_SETTINGS]);
    }

    @Nullable
    public static String applicationsTitle(@NonNull Context context) {
        String[] mainTopics = context.getResources().getStringArray(R.array.main_topics);
        if (mainTopics.length > INDEX_APPLICATIONS) {
            return mainTopics[INDEX_APPLICATIONS];
        }
        return null;
    }
}
