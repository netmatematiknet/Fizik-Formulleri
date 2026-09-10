package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

/**
 * Play In-App Review: 3 formül detayı sonrası, en fazla ~90 günde bir.
 */
public final class InAppReviewHelper {

    private static final String TAG = "InAppReviewHelper";
    private static final String PREFS = "in_app_review_prefs";
    private static final String KEY_DETAIL_OPENS = "formula_detail_opens";
    private static final String KEY_LAST_PROMPT_MS = "last_review_prompt_ms";
    private static final int MIN_DETAIL_OPENS = 3;
    private static final long COOLDOWN_MS = 90L * 24L * 60L * 60L * 1000L;

    private InAppReviewHelper() {
    }

    /** Formula_Detail her açılışta çağrılır. */
    public static void onFormulaDetailOpened(@NonNull Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int opens = prefs.getInt(KEY_DETAIL_OPENS, 0) + 1;
        prefs.edit().putInt(KEY_DETAIL_OPENS, opens).apply();

        if (opens < MIN_DETAIL_OPENS) {
            return;
        }
        long last = prefs.getLong(KEY_LAST_PROMPT_MS, 0L);
        if (System.currentTimeMillis() - last < COOLDOWN_MS) {
            return;
        }
        requestReview(activity, prefs);
    }

    private static void requestReview(@NonNull Activity activity, @NonNull SharedPreferences prefs) {
        ReviewManager manager = ReviewManagerFactory.create(activity);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "review flow request failed", task.getException());
                return;
            }
            ReviewInfo info = task.getResult();
            manager.launchReviewFlow(activity, info)
                    .addOnCompleteListener(launchTask ->
                            prefs.edit().putLong(KEY_LAST_PROMPT_MS, System.currentTimeMillis()).apply());
        });
    }
}
