package com.mobilprogramlar.FizikFormullerim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FormulGoster extends Activity {
    private static final String TAG = "FormulGoster";
    private AdView mAdView1;
    public int ders = 0;
    public int keys = 0;
    static int[] images;
    static String[] title;
    static int page1 = 1;
    static TextView tv;
    ImageButton img_button_geri;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulgoster);

        //Google Analytics REKLAM
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Google Analytics REKLAM

        //Google Addmob REKLAM
        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);

        tv = findViewById(R.id.textView3);

        try {   //getIntent ile konu id sini aldık
            Bundle extraData = getIntent().getExtras();
            //ders = extraData.getString("brans");
            //Toast.makeText(this, String.valueOf(Konuid) , Toast.LENGTH_SHORT).show();
            assert extraData != null;
            keys = extraData.getInt("id");

            images = EEPROM.list;
            title = EEPROM.listString;
            //Toast.makeText(getApplicationContext(), title[0]+ " - " + String.valueOf(page1), Toast.LENGTH_SHORT).show();
            //tv.setText(title[0]+ " - " + String.valueOf(page1));
            //tv.setText(title[0]+String.valueOf(page1)+"/"+String.valueOf(images.length));

            //setTitle(title[0]);
            //setTitle("Nurullah");

            ExtendedViewPager mViewPager = findViewById(R.id.view_pager);

            final TouchImageAdapter tt = new TouchImageAdapter(keys, images);
            mViewPager.setAdapter(tt);
            mViewPager.setOnFocusChangeListener((v, hasFocus) -> {
                //Toast.makeText(getApplicationContext(), "fff" , Toast.LENGTH_SHORT).show();
                //tv.setText(title[0]+ " - " + String.valueOf(page1));
            });

        }catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage() , Toast.LENGTH_LONG).show();
        }


        FloatingActionButton fabButton1 = findViewById(R.id.fabgeri);
        fabButton1.setOnClickListener(view -> {
            //Bundle extraData = getIntent().getExtras();
            //int Konuid = extraData.getInt("id");
            Bundle extraData = getIntent().getExtras();
            assert extraData != null;
            ders = extraData.getInt("brans");
            if(ders==0){
                Intent geriIntent = new Intent(getApplicationContext(), FormullerFizik.class);
                startActivity(geriIntent);
            }else if(ders==1){
                Intent geriIntent = new Intent(getApplicationContext(), FormullerFizik.class);
                startActivity(geriIntent);
            }

        });



        /*
        FloatingActionButton fab_uyari = findViewById(R.id.fabuyari);
        //@SuppressLint("ResourceAsColor")
        fab_uyari.setOnClickListener(view -> {
            //Snackbar.make(view, "Resimleri sağa sola kaydorarak GEÇİŞ YAPABİLİR ve resimleri iki parmağınızı kullanarak BÜYÜLTEBİLİRSİNİZ", Snackbar.LENGTH_LONG).setAction("Dikkat", null).show();
            //Button snackViewButton = (Button) snackView.findViewById(android.support.design.R.id.snackbar_action);
            //snackViewButton.setTextColor(BUTTON_COLOR);
            Snackbar snackbar;
            snackbar = Snackbar.make(view, "Resimleri sağa sola kaydırarak GEÇİŞ YAPABİLİR ve iki parmağınızı kullanarak BÜYÜLTEBİLİRSİNİZ", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            TextView textView = snackBarView.findViewById(R.id.snackbar_text);
            snackBarView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_background));
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_text));
            snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(), R.color.snackbar_background));
            snackbar.show();
        });
        */




    }















    public static class TouchImageAdapter extends PagerAdapter {
        int idMe=0;
        int []img;
        public TouchImageAdapter(int _id, int[] _img)
        {
            this.idMe = _id;
            this.img = _img;
        }

        //private static int[] images = { R.drawable.fortemelk1, R.drawable.fortemelk2, R.drawable.fortemelk3 };

        @Override
        public int getCount() {
        	return images.length;
        }

        @NonNull
        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            //Toast.makeText(get,"page1:"+page1,Toast.LENGTH_LONG).show();
            TouchImageView img = new TouchImageView(container.getContext());
            img.setImageResource(images[position]);
            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //tv.setText(title[0]+String.valueOf(position)+"/"+String.valueOf(images.length));
            tv.setText(title[0]+String.valueOf(images.length)+" sayfa");
            page1 = position+1;

            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }




//Google Addmob REKLAM
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView1!=null) {
            mAdView1.pause();
        }
        super.onPause();
    }
    /** Called when returning to the activity */
    @Override
    public void onResume() {
        if (mAdView1!=null) {
            mAdView1.resume();
        }
        super.onResume();
    }
    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView1!=null) {
            mAdView1.destroy();
        }
        super.onDestroy();
    }
//Google Addmob REKLAM





}
