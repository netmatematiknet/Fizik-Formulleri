package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;

public class ViewPagerAdapter extends PagerAdapter {
    private final IconManager iconManager;
    private final Context context;
    private final int[] imageResIds;

    public ViewPagerAdapter(Context context, int[] imageResIds) {
        this.context = context;
        this.imageResIds = imageResIds != null ? imageResIds : new int[0];
        this.iconManager = new IconManager(context);
    }

    /** Geriye uyumluluk: id string dizisi. */
    public ViewPagerAdapter(Context context, String[] imagePaths) {
        this.context = context;
        this.iconManager = new IconManager(context);
        if (imagePaths == null || imagePaths.length == 0) {
            this.imageResIds = new int[0];
            return;
        }
        int[] ids = new int[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                ids[i] = Integer.parseInt(imagePaths[i]);
            } catch (NumberFormatException e) {
                ids[i] = 0;
            }
        }
        this.imageResIds = ids;
    }

    @Override
    public int getCount() {
        return imageResIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_adapter_viewpager, container, false);
        PhotoView photoView = itemView.findViewById(R.id.imageView);

        int resId = imageResIds[position];
        Bitmap bitmap = iconManager.loadDrawableSampled(resId);
        if (bitmap != null) {
            photoView.setImageBitmap(bitmap);
        } else if (resId != 0) {
            photoView.setImageResource(resId);
        } else {
            Log.e("ViewPagerAdapter", "Image missing at " + position);
            photoView.setImageResource(R.drawable.logom);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
