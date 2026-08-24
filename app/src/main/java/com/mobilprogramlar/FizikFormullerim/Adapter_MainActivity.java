
package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_MainActivity extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_AD = 1;

    private final Context context;
    private final String[] categories;
    private final int[] categoriesImages;
    private final int[] themeColorsRgb;
    private final boolean adFree;
    private final boolean bannersOn;
    private final int bannerEveryN;

    public Adapter_MainActivity(Context context, String[] categories) {
        this.context = context;
        this.categories = categories;
        this.adFree = PremiumManager.getInstance(context).isAdFree();
        AppRemoteConfig remote = AppRemoteConfig.getInstance(context);
        this.bannersOn = !adFree && remote.areBannersEnabled() && remote.getBannerEveryNItems() > 0;
        this.bannerEveryN = Math.max(1, remote.getBannerEveryNItems());
        this.categoriesImages = new IconManager(context).getImagesForCategories();
        ThemeColors themeColor = new ThemeManager(context).getThemeColors();
        this.themeColorsRgb = new int[]{
                themeColor.activityBackgroundColor,
                themeColor.cardBackgroundColor,
                themeColor.cardTextColor
        };
    }

    @Override
    public int getItemViewType(int position) {
        if (!bannersOn) {
            return TYPE_ITEM;
        }
        int block = bannerEveryN + 1;
        return (position % block) == bannerEveryN ? TYPE_AD : TYPE_ITEM;
    }

    private int contentIndexFor(int adapterPosition) {
        if (!bannersOn) {
            return adapterPosition;
        }
        int block = bannerEveryN + 1;
        int blockIndex = adapterPosition / block;
        int offset = adapterPosition % block;
        return blockIndex * bannerEveryN + offset;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_AD) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner_ad, parent, false);
            return new AdViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_adapter_mainactivity, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!adFree && getItemViewType(position) == TYPE_AD) {
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            AdHelper.loadBannerAd(context, adViewHolder.adContainerView);
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        int actualPosition = contentIndexFor(position);
        if (actualPosition < 0 || actualPosition >= categories.length) {
            return;
        }
        itemViewHolder.title.setText(categories[actualPosition]);

        itemViewHolder.title.setTextColor(themeColorsRgb[2]);
        itemViewHolder.cardView.setCardBackgroundColor(themeColorsRgb[1]);
        itemViewHolder.layoutContainer.setBackgroundColor(themeColorsRgb[1]);

        if (actualPosition < categoriesImages.length) {
            itemViewHolder.image.setImageResource(categoriesImages[actualPosition]);
        }

        itemViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = null;
            if (CategoryHelper.isApplicationsIndex(actualPosition)
                    || CategoryHelper.isApplications(context, categories[actualPosition])) {
                NtHelper.openDeveloperPage(context, "mobilprogramlar.com");
            } else {
                intent = new Intent(context, Formula_List.class);
                intent.putExtra("category", categories[actualPosition]);
                intent.putExtra(Formula_List.EXTRA_CATEGORY_INDEX, actualPosition);
            }

            if (intent != null) {
                if (!(context instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            ).start();
        });
    }

    @Override
    public int getItemCount() {
        if (!bannersOn) {
            return categories.length;
        }
        int ads = categories.length / bannerEveryN;
        return categories.length + ads;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout layoutContainer;
        ImageView image;
        TextView title;

        ItemViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            layoutContainer = itemView.findViewById(R.id.layout_container_1);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        FrameLayout adContainerView;

        AdViewHolder(View itemView) {
            super(itemView);
            adContainerView = itemView.findViewById(R.id.ad_view_container);
        }
    }
}
