package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {

    public interface FavoriteClickListener {
        void onFavoriteClick(Person person);
    }

    private final FavoriteClickListener favoriteClickListener;

    public PersonAdapter(Context context, int resource, List<Person> items,
                         FavoriteClickListener favoriteClickListener) {
        super(context, resource, items);
        this.favoriteClickListener = favoriteClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.formuller_listview, parent, false);
        }
        Person p = getItem(position);
        if (p == null) {
            return v;
        }

        ImageView personImage = v.findViewById(R.id.imageView_personpic);
        TextView personName = v.findViewById(R.id.textView_personname);
        TextView personAddress = v.findViewById(R.id.textView_address);
        ImageButton favorite = v.findViewById(R.id.btn_favorite);

        personName.setText(p.getName());
        personAddress.setText(p.getAddress() + " (" + p.getResimSayisi() + " sayfa)");
        personImage.setImageResource(p.getPictureResourceID());
        personImage.setContentDescription(p.getName());

        boolean isFav = FavoritesStore.isFavorite(getContext(), p.getTopicId());
        favorite.setImageResource(isFav ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        favorite.setContentDescription(getContext().getString(
                isFav ? R.string.favori_kaldir : R.string.favori_ekle));
        favorite.setOnClickListener(view -> {
            if (favoriteClickListener != null) {
                favoriteClickListener.onFavoriteClick(p);
            }
        });

        v.setContentDescription(p.getName() + ", " + p.getAddress() + ", " + p.getResimSayisi() + " sayfa");
        return v;
    }
}
