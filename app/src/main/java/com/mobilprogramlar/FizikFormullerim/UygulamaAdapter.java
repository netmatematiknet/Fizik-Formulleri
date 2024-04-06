package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UygulamaAdapter extends RecyclerView.Adapter<UygulamaAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<UygulamaClass> uygulamaClassList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);

            thumbnail.setOnClickListener(v -> {

                //switch (getAdapterPosition()){
                switch (getAbsoluteAdapterPosition()){
                    case 0:
                        //Toast.makeText(mContext,"Item " + getAdapterPosition() + " clicked", Toast.LENGTH_SHORT).show();
                        Intent i0=new Intent(mContext, UygulamaWebview.class);
                        i0.putExtra("uygulama_link", mContext.getString(R.string.uygulama_link_1));
                        mContext.startActivity(i0);
                        break;
                    case 1:
                        Intent i1=new Intent(mContext, UygulamaWebview.class);
                        i1.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_2));
                        mContext.startActivity(i1);
                        break;
                    case 2:
                        Intent i2=new Intent(mContext, UygulamaWebview.class);
                        i2.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_3));
                        mContext.startActivity(i2);
                        break;
                    case 3:
                        Intent i3=new Intent(mContext, UygulamaWebview.class);
                        i3.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_4));
                        mContext.startActivity(i3);
                        break;

                    case 4:
                        Intent i4=new Intent(mContext, UygulamaWebview.class);
                        i4.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_5));
                        mContext.startActivity(i4);
                        break;
                    case 5:
                        Intent i5=new Intent(mContext, UygulamaWebview.class);
                        i5.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_6));
                        mContext.startActivity(i5);
                        break;
                    case 6:
                        Intent i6=new Intent(mContext, UygulamaWebview.class);
                        i6.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_7));
                        mContext.startActivity(i6);
                        break;
                    case 7:
                        Intent i7=new Intent(mContext, UygulamaWebview.class);
                        i7.putExtra("uygulama_link",mContext.getString(R.string.uygulama_link_8));
                        mContext.startActivity(i7);
                        break;
                }
            });




        }
    }




    public UygulamaAdapter(Context mContext, List<UygulamaClass> uygulamaClassList) {
        this.mContext = mContext;
        this.uygulamaClassList = uygulamaClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.uygulama_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UygulamaClass uygulamaClass = uygulamaClassList.get(position);
        holder.title.setText(uygulamaClass.getName());
        //holder.count.setText(uygulama.getNumOfSongs() + " songs");
        holder.count.setText( "Nurullah Tayıpoğlu");

        // loading uygulama cover using Glide library
        Glide.with(mContext).load(uygulamaClass.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(view -> showPopupMenu(holder.overflow));


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.uygulama_menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            /*
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Ayrıntı için resime tıklayınız", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Ayrıntı için resime tıklayınız", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
            */
            int id = menuItem.getItemId();
            if (id == R.id.action_add_favourite) {
                Toast.makeText(mContext, "Ayrıntı için resime tıklayınız", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_play_next) {
                Toast.makeText(mContext, "Ayrıntı için resime tıklayınız", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;



        }
    }

    @Override
    public int getItemCount() {
        return uygulamaClassList.size();
    }
}
