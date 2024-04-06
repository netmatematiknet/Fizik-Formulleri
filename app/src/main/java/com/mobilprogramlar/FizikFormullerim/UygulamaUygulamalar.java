package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UygulamaUygulamalar extends AppCompatActivity {

    private UygulamaAdapter adapter;
    private List<UygulamaClass> uygulamaClassList;
    private ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uygulama_uygulamalar);

        toolbarCagir();

        // RecyclerView öğesini bul
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // Listeyi ve adapter'ı oluştur
        uygulamaClassList = new ArrayList<>();
        adapter = new UygulamaAdapter(this, uygulamaClassList);

        // GridLayoutManager ve boşlukları ekle
        int spanCount = 2; // 2 sütunlu grid
        int spacing = dpToPx();
        boolean includeEdge = true;
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        // Diğer ayarlar
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Toolbar ve "geri" butonu (isteğe bağlı)
        //Toolbar toolbar = findViewById(R.id.toolbar); // Eğer layout'ta toolbar tanımladıysanız
        //if (toolbar != null) {
        //    setSupportActionBar(toolbar);
        //    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //}

        // Back pressed listener (isteğe bağlı)
        //getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
        // @Override
        //public void handleOnBackPressed() {
        // Geri tuşuna basıldığında yapılacaklar
        //    NavUtils.navigateUpFromSameTask(UygulamaUygulamalar.this);
        // }
        //});

        // Glide kütüphanesi kullanarak resim yükleme (isteğe bağlı)
        try {
            //Glide.with(this).load(R.drawable.cover).into(imgCover);
        } catch (Exception e) {
            //e.printStackTrace();
        }


/*
        final int MIN_ANDROID_VERSION_2 = 29; // Android 10 (Build.VERSION_CODES.Q)
        if (Build.VERSION.SDK_INT >= MIN_ANDROID_VERSION_2) {
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    // Geri tuşuna basıldığında yapılacaklar
                    NavUtils.navigateUpFromSameTask(UygulamaUygulamalar.this);
                    //Intent intent = new Intent(OnBackPressed_1.this, MainActivity.class);
                    //startActivity(intent);
                    //finish(); // Aktiviteyi sonlandır
                }
            });
        } else {
            // Android 9 öncesi için geri tuşuna basıldığında yapılacak işlemler
            Intent intent = new Intent(this, UygulamaAnaSayfa.class);
            startActivity(intent);
            super.onBackPressed(); // Gerçek geri tuşu işlevini çağırmak gerekli değil. Intent kullanmazsak burayı kullanırız.
        }
*/







        // SONRADAN    EKLENDİİİİİİİİİİ
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // handle backpressed here
                startActivity(new Intent(getApplicationContext(), UygulamaAnaSayfa.class));
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
        // SONRADAN    EKLENDİİİİİİİİİİ









        // Verileri yükle
        loadAlbums();
    }

    private void loadAlbums() {
        int[] covers = new int[]{
                R.drawable.uygulama_1,
                R.drawable.uygulama_2,
                R.drawable.uygulama_3,
                R.drawable.uygulama_4,
                R.drawable.uygulama_5,
                R.drawable.uygulama_6,
                R.drawable.uygulama_7,
                R.drawable.uygulama_8,
                R.drawable.uygulama_9,
                R.drawable.uygulama_10,
        };

        // Yeni ve eski veri listelerini oluştur
        List<UygulamaClass> uygulamaYeniList = new ArrayList<>();

        UygulamaClass a1 = new UygulamaClass(getString(R.string.uygulama_baslik_1), 8, covers[0]);
        uygulamaYeniList.add(a1);
        UygulamaClass a2 = new UygulamaClass(getString(R.string.uygulama_baslik_2), 8, covers[1]);
        uygulamaYeniList.add(a2);
        UygulamaClass a3 = new UygulamaClass(getString(R.string.uygulama_baslik_3), 8, covers[2]);
        uygulamaYeniList.add(a3);
        UygulamaClass a4 = new UygulamaClass(getString(R.string.uygulama_baslik_4), 8, covers[3]);
        uygulamaYeniList.add(a4);
        UygulamaClass a5 = new UygulamaClass(getString(R.string.uygulama_baslik_5), 8, covers[4]);
        uygulamaYeniList.add(a5);
        UygulamaClass a6 = new UygulamaClass(getString(R.string.uygulama_baslik_6), 8, covers[5]);
        uygulamaYeniList.add(a6);
        UygulamaClass a7 = new UygulamaClass(getString(R.string.uygulama_baslik_7), 8, covers[6]);
        uygulamaYeniList.add(a7);
        UygulamaClass a8 = new UygulamaClass(getString(R.string.uygulama_baslik_8), 8, covers[7]);
        uygulamaYeniList.add(a8);
        UygulamaClass a9 = new UygulamaClass(getString(R.string.uygulama_baslik_9), 8, covers[8]);
        uygulamaYeniList.add(a9);
        UygulamaClass a10 = new UygulamaClass(getString(R.string.uygulama_baslik_10), 8, covers[9]);
        uygulamaYeniList.add(a10);




        // Değişiklikleri takip et
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ListDiffCallback(uygulamaClassList, uygulamaYeniList));

        // Değişiklikleri uygulama
        uygulamaClassList.clear();
        uygulamaClassList.addAll(uygulamaYeniList);
        diffResult.dispatchUpdatesTo(adapter);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }




    //GridSpacingItemDecoration sınıfı, RecyclerView öğeleri arasındaki boşluğu ayarlar.
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            //includeEdge parametresi, kenar boşluklarını içerip içermeyeceğini belirler.
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }















    private static class ListDiffCallback extends DiffUtil.Callback {

        private final List<UygulamaClass> oldList;
        private final List<UygulamaClass> newList;

        public ListDiffCallback(List<UygulamaClass> oldList, List<UygulamaClass> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            UygulamaClass a = oldList.get(oldItemPosition);
            UygulamaClass b = newList.get(newItemPosition);
            //return a.getId() == b.getId(); // Eşsiz bir ID kullanıyorsanız
            return Objects.equals(a.getName(), b.getName());
        }



        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            UygulamaClass a = oldList.get(oldItemPosition);
            UygulamaClass b = newList.get(newItemPosition);
            //return a.getBaslik().equals(b.getBaslik()) && a.getKapakResmi() == b.getKapakResmi(); // İçerikleri karşılaştırın
            return a.getName().equals(b.getName()) && a.getThumbnail() == b.getThumbnail();
        }

        /*
        @Override
        public void getChangePayload(int oldItemPosition, int newItemPosition, RecyclerView.ViewHolder holder) {
            // Gerekirse payload kullanarak performansı optimize edin
        }
        */

        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Gerekirse payload kullanarak performansı optimize edin
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }


    }













    //TOOLBAR MENÜ - TOOLBAR MENÜ
    private void toolbarCagir(){
        Toolbar toolbar;
        toolbar= findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.toolbar_anasayfa) {
                anasayfa();
            } else if (id == R.id.toolbar_kapat1) {
                kapat();
            } else if (id == R.id.toolbar_paylas) {
                paylas();
            }
            return false;
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Intent geriButonu = new Intent(getApplicationContext(), UygulamaAnaSayfa.class);
            NavUtils.navigateUpTo(this, geriButonu);
            //super.onBackPressed();

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    // Back is pressed... Finishing the activity
                    //finish();
                }
            });

            return true;
        }
        return false;
    }
    public void kapat() {
        this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);     //System.exit() yığında birden fazla etkinliğiniz varsa uygulamanızı öldürmez
        getParent().finish();
        //ActivityCompat.finishAffinity(MainActivity.this);
        //finishAndRemoveTask();
    }
    public void anasayfa(){
        Intent myIntent = new Intent(getApplicationContext(), UygulamaAnaSayfa.class);
        startActivity(myIntent);
        this.finish();  //Bir sonraki sayfaya geömeden Aktivityi SONLANDIR
    }
    public void paylas(){
            String paylasmesajbasligii = "YKS Tyt Fizik Formülleri\n";
            String paylasmesaji = "Tyt Fiziğindeki tüm Fizik Formülleri Elinizin Altında "
                    +"Yanınızda kitap taşımanıza gerek yok\n"
                    +"https://play.google.com/store/apps/details?id=com.mobilprogramlar.FizikFormullerim";
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, paylasmesajbasligii);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, paylasmesaji);
            startActivity(Intent.createChooser(sharingIntent, "Paylaşmak İçin Tıklayınız"));
    }
    public void cikis(){
        finish();
    }
    //PAYLAŞ

}
