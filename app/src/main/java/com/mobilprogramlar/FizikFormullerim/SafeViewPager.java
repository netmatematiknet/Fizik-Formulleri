package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

/**
 * ViewPager'ın multi-touch sırasında fırlattığı
 * "pointerIndex out of range" IllegalArgumentException'ını yakalar.
 *
 * Kök neden: Android'in MotionEvent'i ACTION_MOVE sırasında
 * pointer sayısını düşürdüğünde ViewPager eski pointer index'ini
 * kullanmaya çalışır ve crash oluşur.
 * Bu wrapper sınıf exception'ı catch ederek uygulamanın çökmesini önler.
 */
public class SafeViewPager extends ViewPager {

    public SafeViewPager(Context context) {
        super(context);
    }

    public SafeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            // pointerIndex out of range — multi-touch sırasında oluşan Android bug'ı.
            // Event'i tüketmeden false döndür; kaydırma iptal edilir ama uygulama çökmez.
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            // Aynı bug onTouchEvent'te de tetiklenebilir.
            return false;
        }
    }
}