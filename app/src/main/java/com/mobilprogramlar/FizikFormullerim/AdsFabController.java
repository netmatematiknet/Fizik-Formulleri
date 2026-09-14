package com.mobilprogramlar.FizikFormullerim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/** TrafiQ tarzı FAB: Widget + Puan + satın al + ödüllü mola. */
public final class AdsFabController {

    private static final long TIP_MS = 2600L;
    private static final String PREF_UI = "fizik_ads_fab_ui";
    private static final String KEY_X = "fab_x";
    private static final String KEY_Y = "fab_y";
    private static final String KEY_HAS_POS = "fab_has_pos";
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private final Activity activity;
    private final View root;
    private final View cluster;
    private final View scrim;
    private final View menu;
    private final View pulse;
    private final TextView tip;
    private final TextView breakDesc;
    private final TextView label;
    private final MaterialButton widgetBtn;
    private final MaterialButton rateBtn;
    private final MaterialButton breakBtn;
    private final MaterialButton buyBtn;
    private final FloatingActionButton fab;
    private final int touchSlop;
    private boolean open;
    private boolean dragging;
    private float downRawX;
    private float downRawY;
    private float grabDx;
    private float grabDy;
    @Nullable
    private ObjectAnimator floatAnim;

    private AdsFabController(@NonNull Activity activity, @NonNull View root) {
        this.activity = activity;
        this.root = root;
        this.cluster = root.findViewById(R.id.adsFabCluster);
        this.scrim = root.findViewById(R.id.adsFabScrim);
        this.menu = root.findViewById(R.id.adsFabMenu);
        this.pulse = root.findViewById(R.id.adsFabPulse);
        this.tip = root.findViewById(R.id.adsFabTip);
        this.breakDesc = root.findViewById(R.id.adsFabBreakDesc);
        this.label = root.findViewById(R.id.adsFabLabel);
        this.widgetBtn = root.findViewById(R.id.adsFabWidgetBtn);
        this.rateBtn = root.findViewById(R.id.adsFabRateBtn);
        this.breakBtn = root.findViewById(R.id.adsFabBreakBtn);
        this.buyBtn = root.findViewById(R.id.adsFabBuyBtn);
        this.fab = root.findViewById(R.id.adsFabMain);
        this.touchSlop = ViewConfiguration.get(activity).getScaledTouchSlop();
    }

    public static AdsFabController attach(@NonNull Activity activity, @NonNull View adsFabRoot) {
        AdsFabController c = new AdsFabController(activity, adsFabRoot);
        c.bind();
        return c;
    }

    public void setVisible(boolean visible) {
        root.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            onResume();
        } else if (open) {
            closeMenu(false);
        }
    }

    private void bind() {
        // Widget/Puan her zaman; reklam butonları duruma göre
        root.setVisibility(View.VISIBLE);
        root.setClickable(false);
        root.setFocusable(false);
        restoreOrDefaultPosition();
        enableDrag();
        startMotion();
        refreshBreakState(false);
        View.OnClickListener toggle = v -> {
            if (open) {
                closeMenu(true);
            } else {
                openMenu();
            }
        };
        fab.setOnClickListener(toggle);
        if (label != null) {
            label.setOnClickListener(toggle);
        }
        fab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(activity, R.color.trafiq_cta_orange)));
        fab.setImageTintList(ColorStateList.valueOf(
                ContextCompat.getColor(activity, android.R.color.white)));
        scrim.setOnClickListener(v -> closeMenu(true));

        if (widgetBtn != null) {
            widgetBtn.setOnClickListener(v -> {
                closeMenu(false);
                if (activity instanceof AppCompatActivity) {
                    WidgetPinHelper.showPicker((AppCompatActivity) activity);
                }
            });
        }
        if (rateBtn != null) {
            rateBtn.setOnClickListener(v -> {
                closeMenu(false);
                ReviewHelper.askFromButton(activity);
            });
        }
        breakBtn.setOnClickListener(v -> {
            if (!breakBtn.isEnabled()) {
                showTip(activity.getString(R.string.ads_break_active_tip_fmt,
                        AppPrefs.formatAdsPauseRemaining(activity)), true);
                return;
            }
            closeMenu(false);
            RewardedAdHelper.showForAdPause(activity);
        });
        buyBtn.setOnClickListener(v -> {
            closeMenu(false);
            if (activity instanceof AppCompatActivity) {
                ((App) activity.getApplication()).getBillingManager()
                        .launchPurchaseFlow((AppCompatActivity) activity);
            }
        });
    }

    public void onResume() {
        if (root.getVisibility() != View.VISIBLE) {
            bind();
            return;
        }
        refreshBreakState(false);
        if (!open) {
            startMotion();
        }
    }

    private void restoreOrDefaultPosition() {
        SharedPreferences p = uiPrefs();
        if (!p.getBoolean(KEY_HAS_POS, false)) {
            return;
        }
        final float x = p.getFloat(KEY_X, 0f);
        final float y = p.getFloat(KEY_Y, 0f);
        cluster.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cluster.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                moveCluster(x, y);
            }
        });
    }

    private void enableDrag() {
        View.OnTouchListener drag = (v, event) -> {
            if (open) {
                return false;
            }
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dragging = false;
                    downRawX = event.getRawX();
                    downRawY = event.getRawY();
                    grabDx = cluster.getX() - event.getRawX();
                    grabDy = cluster.getY() - event.getRawY();
                    stopMotion();
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    float dx = event.getRawX() - downRawX;
                    float dy = event.getRawY() - downRawY;
                    if (!dragging && (dx * dx + dy * dy) > (touchSlop * touchSlop)) {
                        dragging = true;
                    }
                    if (dragging) {
                        moveCluster(event.getRawX() + grabDx, event.getRawY() + grabDy);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (dragging) {
                        savePosition(cluster.getX(), cluster.getY());
                        startMotion();
                    } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                        v.performClick();
                        startMotion();
                    } else {
                        startMotion();
                    }
                    dragging = false;
                    return true;
                default:
                    return false;
            }
        };
        fab.setOnTouchListener(drag);
        if (label != null) {
            label.setOnTouchListener(drag);
        }
    }

    private void moveCluster(float x, float y) {
        int parentW = root.getWidth();
        int parentH = root.getHeight();
        int w = cluster.getWidth();
        int h = cluster.getHeight();
        if (parentW <= 0 || parentH <= 0 || w <= 0 || h <= 0) {
            cluster.setX(x);
            cluster.setY(y);
            return;
        }
        float maxX = Math.max(0f, parentW - w);
        float maxY = Math.max(0f, parentH - h);
        cluster.setX(Math.max(0f, Math.min(x, maxX)));
        cluster.setY(Math.max(0f, Math.min(y, maxY)));
    }

    private void savePosition(float x, float y) {
        uiPrefs().edit().putBoolean(KEY_HAS_POS, true).putFloat(KEY_X, x).putFloat(KEY_Y, y).apply();
    }

    private SharedPreferences uiPrefs() {
        return activity.getSharedPreferences(PREF_UI, Context.MODE_PRIVATE);
    }

    private void openMenu() {
        open = true;
        stopMotion();
        if (label != null) {
            label.setVisibility(View.GONE);
        }
        scrim.setVisibility(View.VISIBLE);
        scrim.setAlpha(0f);
        scrim.animate().alpha(1f).setDuration(180).start();
        menu.setVisibility(View.VISIBLE);
        refreshBreakState(true);
        tip.setVisibility(View.GONE);

        AnimatorSet set = new AnimatorSet();
        java.util.ArrayList<android.animation.Animator> anims = new java.util.ArrayList<>();
        anims.add(ObjectAnimator.ofFloat(fab, View.ROTATION, 0f, 45f));
        float[] starts = {44f, 36f, 28f, 20f};
        MaterialButton[] buttons = {widgetBtn, rateBtn, breakBtn, buyBtn};
        for (int i = 0; i < buttons.length; i++) {
            MaterialButton b = buttons[i];
            if (b == null || b.getVisibility() != View.VISIBLE) {
                continue;
            }
            b.setAlpha(0f);
            b.setTranslationY(starts[Math.min(i, starts.length - 1)]);
            anims.add(ObjectAnimator.ofFloat(b, View.ALPHA, 0f, 1f));
            anims.add(ObjectAnimator.ofFloat(b, View.TRANSLATION_Y,
                    starts[Math.min(i, starts.length - 1)], 0f));
        }
        set.playTogether(anims);
        set.setDuration(280);
        set.setInterpolator(new OvershootInterpolator(1.1f));
        set.start();

        if (PremiumManager.getInstance(activity).isAdFree()) {
            showTip(activity.getString(R.string.ads_fab_tip_owned), true);
        } else if (!AppRemoteConfig.getInstance(activity).isRewardedEnabled()
                && !AdGate.isRemoveAdsButtonEnabled(activity)) {
            // Sadece widget/puan
        } else if (!AppRemoteConfig.getInstance(activity).isRewardedEnabled()) {
            showTip(activity.getString(R.string.ads_fab_tip_buy_only), true);
        }
    }

    private void closeMenu(boolean animate) {
        open = false;
        tip.setVisibility(View.GONE);
        if (breakDesc != null) {
            breakDesc.setVisibility(View.GONE);
        }
        if (label != null) {
            label.setVisibility(View.VISIBLE);
        }
        if (!animate) {
            menu.setVisibility(View.GONE);
            scrim.setVisibility(View.GONE);
            fab.setRotation(0f);
            startMotion();
            return;
        }
        menu.animate().alpha(0f).setDuration(160).withEndAction(() -> {
            menu.setAlpha(1f);
            menu.setVisibility(View.GONE);
        }).start();
        scrim.animate().alpha(0f).setDuration(160).withEndAction(() -> {
            scrim.setVisibility(View.GONE);
            scrim.setAlpha(1f);
        }).start();
        fab.animate().rotation(0f).setDuration(180).start();
        startMotion();
    }

    private void refreshBreakState(boolean forOpen) {
        boolean owned = PremiumManager.getInstance(activity).isAdFree();
        boolean rewardedOn = AppRemoteConfig.getInstance(activity).isRewardedEnabled();
        boolean buyOn = AdGate.isRemoveAdsButtonEnabled(activity);
        boolean active = AppPrefs.isAdsPaused(activity);

        if (widgetBtn != null) {
            widgetBtn.setVisibility(View.VISIBLE);
        }
        if (rateBtn != null) {
            rateBtn.setVisibility(View.VISIBLE);
        }

        if (owned) {
            breakBtn.setVisibility(View.GONE);
            if (breakDesc != null) {
                breakDesc.setVisibility(View.GONE);
            }
            buyBtn.setVisibility(buyOn ? View.VISIBLE : View.GONE);
            setBuyEnabled(false, R.string.ads_fab_buy_owned);
            if (label != null && !open) {
                label.setText(R.string.ads_fab_buy_owned);
            }
            return;
        }
        if (label != null && !open) {
            label.setText(R.string.ads_fab_cd);
        }
        buyBtn.setVisibility(buyOn ? View.VISIBLE : View.GONE);
        setBuyEnabled(buyOn, R.string.ads_fab_buy);
        if (!rewardedOn) {
            breakBtn.setVisibility(View.GONE);
            if (breakDesc != null) {
                breakDesc.setVisibility(View.GONE);
            }
            return;
        }
        breakBtn.setVisibility(View.VISIBLE);
        if (active) {
            String left = AppPrefs.formatAdsPauseRemaining(activity);
            if (breakDesc != null && (open || forOpen)) {
                breakDesc.setText(activity.getString(R.string.ads_fab_break_active_desc_fmt, left));
                breakDesc.setVisibility(View.VISIBLE);
            }
            setBreakEnabled(false, activity.getString(R.string.ads_fab_break_active));
        } else {
            int hours = AppRemoteConfig.getInstance(activity).getRewardedPauseHours();
            if (breakDesc != null && (open || forOpen)) {
                breakDesc.setText(activity.getString(R.string.ads_fab_break_desc_fmt, hours));
                breakDesc.setVisibility(View.VISIBLE);
            }
            setBreakEnabled(true, activity.getString(R.string.ads_fab_break));
        }
    }

    private void setBreakEnabled(boolean enabled, @NonNull String text) {
        breakBtn.setText(text);
        breakBtn.setEnabled(enabled);
        breakBtn.setClickable(enabled);
        breakBtn.setAlpha(1f);
        int bg = enabled ? R.color.trafiq_primary : R.color.fab_disabled_bg;
        int fg = enabled ? R.color.trafiq_on_primary : R.color.fab_disabled_text;
        breakBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, bg)));
        breakBtn.setTextColor(ContextCompat.getColor(activity, fg));
    }

    private void setBuyEnabled(boolean enabled, int textRes) {
        buyBtn.setText(textRes);
        buyBtn.setEnabled(enabled);
        buyBtn.setClickable(enabled);
        buyBtn.setAlpha(1f);
        int bg = enabled ? R.color.trafiq_cta_orange : R.color.fab_disabled_bg;
        int fg = enabled ? android.R.color.white : R.color.fab_disabled_text;
        buyBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, bg)));
        buyBtn.setTextColor(ContextCompat.getColor(activity, fg));
    }

    private void showTip(@NonNull String text, boolean autoHide) {
        tip.setText(text);
        tip.setVisibility(View.VISIBLE);
        tip.setAlpha(0f);
        tip.setTranslationY(12f);
        tip.animate().alpha(1f).translationY(0f).setDuration(220)
                .setInterpolator(new DecelerateInterpolator()).start();
        if (autoHide) {
            MAIN.postDelayed(() -> tip.animate().alpha(0f).setDuration(220).withEndAction(() -> {
                tip.setVisibility(View.GONE);
                tip.setAlpha(1f);
            }).start(), TIP_MS);
        }
    }

    private void startMotion() {
        stopMotion();
        if (open || root.getVisibility() != View.VISIBLE) {
            return;
        }
        floatAnim = ObjectAnimator.ofFloat(fab, View.TRANSLATION_Y, 0f, -8f, 0f, 6f, 0f);
        floatAnim.setDuration(3800L);
        floatAnim.setRepeatCount(ObjectAnimator.INFINITE);
        floatAnim.start();
        if (pulse != null) {
            ObjectAnimator sx = ObjectAnimator.ofFloat(pulse, View.SCALE_X, 1f, 1.35f);
            ObjectAnimator sy = ObjectAnimator.ofFloat(pulse, View.SCALE_Y, 1f, 1.35f);
            ObjectAnimator pa = ObjectAnimator.ofFloat(pulse, View.ALPHA, 0.8f, 0.1f);
            sx.setDuration(1500L);
            sy.setDuration(1500L);
            pa.setDuration(1500L);
            sx.setRepeatCount(ObjectAnimator.INFINITE);
            sy.setRepeatCount(ObjectAnimator.INFINITE);
            pa.setRepeatCount(ObjectAnimator.INFINITE);
            AnimatorSet pulseSet = new AnimatorSet();
            pulseSet.playTogether(sx, sy, pa);
            pulseSet.start();
            pulse.setTag(pulseSet);
        }
    }

    private void stopMotion() {
        if (floatAnim != null) {
            floatAnim.cancel();
            floatAnim = null;
        }
        fab.setTranslationY(0f);
        if (pulse != null) {
            Object tag = pulse.getTag();
            if (tag instanceof AnimatorSet) {
                ((AnimatorSet) tag).cancel();
                pulse.setTag(null);
            }
            pulse.setScaleX(1f);
            pulse.setScaleY(1f);
            pulse.setAlpha(0.55f);
        }
    }
}
