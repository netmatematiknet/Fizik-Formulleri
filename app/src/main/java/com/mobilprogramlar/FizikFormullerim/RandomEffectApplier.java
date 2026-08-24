package com.mobilprogramlar.FizikFormullerim;

import androidx.viewpager.widget.ViewPager;
import java.util.Random;

public class RandomEffectApplier {
    public static void applyRandomEffect(ViewPager viewPager) {
        Random random = new Random();
        int effectNumber = random.nextInt(1) + 1; // 1 ile 55 arası rastgele sayı üretir.

        switch (effectNumber) {
            case 1:
                //Toast.makeText( viewPager.getContext(),"1",Toast.LENGTH_SHORT).show();
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect1());
                break;
/*
            case 2:
                Toast.makeText( viewPager.getContext(),"2",Toast.LENGTH_SHORT).show();
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect2());
                break;

            case 3:
                Toast.makeText( viewPager.getContext(),"3",Toast.LENGTH_SHORT).show();
                viewPager.setPageTransformer(true, ImageEffects.applyEffect3());
                break;

            case 4:
                Toast.makeText( viewPager.getContext(),"4",Toast.LENGTH_SHORT).show();
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect4());
                break;
            case 5:
                Toast.makeText( viewPager.getContext(),"5",Toast.LENGTH_SHORT).show();
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect5());
                break;

            case 6:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect6());
                break;
            case 7:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect7());
                break;
            case 8:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect8());
                break;
            case 9:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect9());
                break;
            case 10:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect10());
                break;


            case 11:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect11());
                break;
            case 12:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect12());
                break;
            case 13:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect13());
                break;
            case 14:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect14());
                break;
            case 15:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect15());
                break;
            case 16:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect16());
                break;
            case 17:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect17());
                break;
            case 18:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect18());
                break;
            case 19:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect19());
                break;
            case 20:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect20());
                break;
            case 21:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect21());
                break;
            case 22:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect22());
                break;
            case 23:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect23());
                break;
            case 24:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect24());
                break;
            case 25:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect25());
                break;
            case 26:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect26());
                break;
            case 27:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect27());
                break;
            case 28:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect28());
                break;
            case 29:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect29());
                break;
            case 30:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect30());
                break;
            case 31:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect31());
                break;
            case 32:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect32());
                break;
            case 33:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect33());
                break;
            case 34:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect34());
                break;
            case 35:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect35());
                break;
            case 36:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect36());
                break;
            case 37:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect37());
                break;
            case 38:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect38());
                break;
            case 39:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect39());
                break;
            case 40:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect40());
                break;
            case 41:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect41());
                break;
            case 42:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect42());
                break;
            case 43:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect43());
                break;
            case 44:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect44());
                break;
            case 45:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect45());
                break;
            case 46:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect46());
                break;
            case 47:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect47());
                break;
            case 48:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect48());
                break;
            case 49:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect49());
                break;
            case 50:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect50());
                break;
            case 51:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect51());
                break;
            case 52:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect52());
                break;
            case 53:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect53());
                break;
            case 54:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect54());
                break;
            case 55:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect55());
                break;
            case 56:
                viewPager.setPageTransformer(true, ImageEffects.applyDepthEffect56());
                break;



*/


            default:
                break;
        }
    }
}
