package com.mobilprogramlar.FizikFormullerim;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Shared formula image pager used by FormulGoster and tablet detail pane.
 */
public final class FormulaPagerHelper {

    private FormulaPagerHelper() {
    }

    public static void bind(
            ExtendedViewPager pager,
            TextView pageIndicator,
            int[] images,
            String title
    ) {
        if (pager == null || images == null || images.length == 0) {
            return;
        }
        TouchImageAdapter adapter = new TouchImageAdapter(images, title, pageIndicator);
        pager.setAdapter(adapter);
        pager.clearOnPageChangeListeners();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateIndicator(pageIndicator, title, position, images.length);
            }
        });
        updateIndicator(pageIndicator, title, 0, images.length);
    }

    public static void updateIndicator(TextView pageIndicator, String title, int position, int total) {
        if (pageIndicator == null || total <= 0) {
            return;
        }
        String page = (position + 1) + " / " + total;
        String label = (title == null || title.isEmpty()) ? page : title + "  ·  " + page;
        pageIndicator.setText(label);
        pageIndicator.setContentDescription(
                pageIndicator.getContext().getString(R.string.sayfa_gostergesi_a11y, position + 1, total));
    }

    public static final class TouchImageAdapter extends PagerAdapter {
        private final int[] images;
        private final String title;
        private final TextView pageIndicator;

        public TouchImageAdapter(int[] images, String title, TextView pageIndicator) {
            this.images = images;
            this.title = title;
            this.pageIndicator = pageIndicator;
        }

        @Override
        public int getCount() {
            return images == null ? 0 : images.length;
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull ViewGroup container, int position) {
            TouchImageView imageView = new TouchImageView(container.getContext());
            imageView.setImageResource(images[position]);
            imageView.setContentDescription(
                    container.getContext().getString(R.string.formul_gorseli_a11y, position + 1, images.length));
            container.addView(imageView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
