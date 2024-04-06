package com.mobilprogramlar.FizikFormullerim;

import android.content.Context;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class NURreklam {
    private InterstitialAd interstitial;
    //private static final String _Interstitial = "ca-app-pub-6848773398638567/5733997623";
    private static final String _Interstitial = "ca-app-pub-3940256099942544/1033173712";   //TEST
    //private static final String _Interstitial = String.valueOf(R.string.interstitial_gecis_gecerli_id);

    public void reklamGoster(Context context)
    {
/*
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(_Interstitial);
        //interstitial.setAdUnitId(String.valueOf(R.string.interstitial_gecis_gecerli_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });
*/
    }



}
