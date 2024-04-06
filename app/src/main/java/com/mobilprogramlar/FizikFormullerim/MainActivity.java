package com.mobilprogramlar.FizikFormullerim;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Google Analytics REKLAM
        //FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Google Analytics REKLAM

        /*
        //Google Addmob REKLAM
        MobileAds.initialize(this, "ca-app-pub-6848773398638567~9469156132");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //Google Addmob REKLAM
*/

        //Intent intent = new Intent(getApplicationContext(), UygulamaUrunlerimiz.class);
        //startActivity(intent);
    }
}
