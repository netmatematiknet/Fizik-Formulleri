package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Formula_List extends RecyclerView.Adapter<Adapter_Formula_List.ViewHolder> {
    private final Context context;
    private final String[] konu_dizisi;
    private final String selectedCategory;
    private final IconManager iconManager;
    private final int themeID;

    public Adapter_Formula_List(Context context, String[] konu_dizisi, String category, int themeID) {
        this.context = context;
        this.konu_dizisi = konu_dizisi;
        this.selectedCategory = category;
        this.iconManager = new IconManager(context);
        this.themeID = themeID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adapter_formula_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String konu = konu_dizisi[position];

        int[] colors = updateUIComponents(themeID);

        holder.formula_title.setTextColor(colors[2]);
        holder.formula_description.setTextColor(colors[2]);
        holder.extra_info.setTextColor(colors[2]);
        holder.cardView.setCardBackgroundColor(colors[1]);
        holder.layoutContainer.setBackgroundColor(colors[1]);

        holder.formula_title.setText(konu);
        holder.formula_description.setText(selectedCategory);

        int[] imageResArray = iconManager.getDrawableIdsForTopic(konu);

        if (CategoryHelper.isApplications(context, selectedCategory)) {
            holder.extra_info.setText(context.getString(R.string.ucretsiz));
            holder.formula_image.setImageResource(R.drawable.uygulamalarimiz);
        } else {
            holder.extra_info.setText(context.getString(R.string.konudaki_resim_sayisi, imageResArray.length));
            holder.formula_image.setImageResource(R.drawable.einstein_equation);
        }

        holder.itemView.setOnClickListener(v -> {
            if (CategoryHelper.isApplications(context, selectedCategory)) {
                return;
            }
            Intent intent = new Intent(context, Formula_Detail.class);
            intent.putExtra("formula_category", selectedCategory);
            intent.putExtra("formula_title", konu);
            context.startActivity(intent);
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            ).start();
        });
    }

    @Override
    public int getItemCount() {
        return konu_dizisi.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout layoutContainer;
        ImageView formula_image;
        TextView formula_title;
        TextView formula_description;
        TextView extra_info;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            layoutContainer = itemView.findViewById(R.id.layout_container_1);
            formula_image = itemView.findViewById(R.id.formula_image);
            formula_title = itemView.findViewById(R.id.formula_title);
            formula_description = itemView.findViewById(R.id.formula_description);
            extra_info = itemView.findViewById(R.id.extra_info);
        }
    }

    private int[] updateUIComponents(int themeId) {
        ThemeManager themeManager = new ThemeManager(context);
        ThemeColors themeColor = themeManager.getThemeColors();
        return new int[]{themeColor.activityBackgroundColor, themeColor.cardBackgroundColor, themeColor.cardTextColor};
    }
}
