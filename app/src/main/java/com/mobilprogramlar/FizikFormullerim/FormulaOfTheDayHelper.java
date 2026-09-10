package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;

/**
 * Günün formülü: yılın gününe göre TopicCatalog'dan seçer.
 */
public final class FormulaOfTheDayHelper {

    private FormulaOfTheDayHelper() {
    }

    @Nullable
    public static TopicCatalog.Topic today(@NonNull Context context) {
        List<TopicCatalog.Topic> topics = TopicCatalog.all();
        if (topics == null || topics.isEmpty()) {
            return null;
        }
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int index = Math.floorMod(dayOfYear - 1, topics.size());
        return topics.get(index);
    }
}
