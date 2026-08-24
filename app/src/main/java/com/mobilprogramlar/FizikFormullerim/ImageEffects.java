package com.mobilprogramlar.FizikFormullerim;

import androidx.viewpager.widget.ViewPager;

public class ImageEffects {

    public static ViewPager.PageTransformer applyDepthEffect1() {
        //String efet_adi_2 = "Döndürerek Açılma Efekti: Sayfa sanki bir kapı gibi açılıyormuş gibi döndürülerek açılır.";
        return (view, position) -> {
            //Toast.makeText(view.getContext(), "32t", Toast.LENGTH_SHORT).show();
            view.setPivotX(position < 0 ? view.getWidth() : 0);
            view.setRotationY(90 * position);
        };
    }
    /*
    public static ViewPager.PageTransformer applyDepthEffect2() {
        String efet_adi_54 = "Kadim Efektler: ";
        return (view, position) -> {
            //Toast.makeText(view.getContext(), "54", Toast.LENGTH_SHORT).show();
            view.setTranslationX(view.getWidth() * position);
            view.setRotation(360 * position);
            view.setAlpha(Math.max(0, 1 - Math.abs(position)));
        };
    }



    public static ViewPager.PageTransformer applyEffect3() {
        return (page, position) -> {
            //Toast.makeText(page.getContext(),"3",Toast.LENGTH_SHORT).show();
            page.setScaleX(1 - 0.3f * Math.abs(position));
            page.setScaleY(1 - 0.3f * Math.abs(position));
            page.setRotationY(180 * position);
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect4() {
        String efet_adi_52 = "Efsanevi Efektler: Merlin’s Twist";
        return (view, position) -> {
            //Toast.makeText(view.getContext(), "52", Toast.LENGTH_SHORT).show();
            view.setRotationY(position * -90);
            view.setAlpha(1 - Math.abs(position));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect5() {
        String efet_adi_5 = "Depth Page Transformer: Sayfa derinliğini artırarak, aktif olmayan sayfaları küçülterek ve soluklaştırarak görsel bir derinlik efekti yaratır.";
        final float MIN_SCALE_5 = 0.85f;
        return (view, position) -> {
            //Toast.makeText(view.getContext(), "5t", Toast.LENGTH_SHORT).show();
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 0) {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE_5 + (1 - MIN_SCALE_5) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        };
    }





    public static ViewPager.PageTransformer applyDepthEffect6() {
        String efet_adi_6 = "Zoom Out Page Transformer: Sayfalar arasında gezinirken sayfayı küçültüp büyüterek bir yakınlaştırma efekti oluşturur.";
        final float MIN_SCALE_6 = 0.85f;
        final float MIN_ALPHA_6 = 0.5f;
        return (view, position) -> {
            Toast.makeText(view.getContext(), "6t", Toast.LENGTH_SHORT).show();
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE_6, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA_6 + (scaleFactor - MIN_SCALE_6) / (1 - MIN_SCALE_6) * (1 - MIN_ALPHA_6));
            } else {
                view.setAlpha(0);
            }

        };
    }

    public static ViewPager.PageTransformer applyDepthEffect7() {
        String efet_adi_7 = "Cube Out Transformer: Geçiş sırasında sayfanın bir küp olarak dönmesini sağlayan bir efekt.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "7", Toast.LENGTH_SHORT).show();
            view.setPivotX(position > 0 ? 0 : view.getWidth());
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(-90f * position);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect8() {
        String efet_adi_8 = "Flip Horizontal Transformer: Sayfa, yatay eksende çevrilerek diğer sayfaya geçiş yapar.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "8", Toast.LENGTH_SHORT).show();
            float rotation = 180f * position;
            view.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(rotation);
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect9() {
        String efet_adi_9 = "Accordion Transformer: Sayfa, akordeon gibi katlanarak diğer sayfaya geçer.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "9t", Toast.LENGTH_SHORT).show();
            view.setPivotX(position < 0 ? 0 : view.getWidth());
            view.setScaleX(position < 0 ? 1f + position : 1f - position);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect10() {
        String efet_adi_10 = "Scale Transformer: Sayfaları küçülterek ve büyüterek odaklanmış bir geçiş sağlar.";
        final float MIN_SCALE_10 = 0.85f;
        return (view, position) -> {
            Toast.makeText(view.getContext(), "10t", Toast.LENGTH_SHORT).show();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE_10, 1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        };
    }













    public static ViewPager.PageTransformer applyDepthEffect11() {
        String efet_adi_11 = "Scale Transformer: Sayfaları küçülterek ve büyüterek odaklanmış bir geçiş sağlar.";
        final float MIN_SCALE_10 = 0.85f;
        return (view, position) -> {
            Toast.makeText(view.getContext(), "11", Toast.LENGTH_SHORT).show();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE_10, 1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect12() {
        String efet_adi_12 = "Fade Transformer: Sayfalar arasındaki geçişte solma efekti uygular.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "12", Toast.LENGTH_SHORT).show();
            view.setAlpha(1 - Math.abs(position));
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect13() {
        String efet_adi_13 = "Rotate Down Transformer: Sayfayı aşağı doğru döndürerek geçiş yapar.";
        //final float ROT_MOD_13 = -15f;
        return (view, position) -> {
            Toast.makeText(view.getContext(), "13", Toast.LENGTH_SHORT).show();
            //int width = view.getWidth();
            //int rotation = (int) (ROT_MOD_13 * position);
            //view.setPivotX(width * 0.5f);
            //view.setPivotY(view.getHeight());
            //view.setRotation(rotation);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect14() {
        String efet_adi_14 = "Rotate Up Transformer: Sayfayı yukarı doğru döndürerek geçiş yapar.";
        final float ROT_MOD_14 = 15f;
        return (view, position) -> {
            Toast.makeText(view.getContext(), "14", Toast.LENGTH_SHORT).show();
            int width = view.getWidth();
            int rotation = (int) (ROT_MOD_14 * position);
            view.setPivotX(width * 0.5f);
            view.setPivotY(0f);
            view.setRotation(rotation);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect15() {
        String efet_adi_15 = "Background To Foreground Transformer: Sayfayı arka plandan ön plana doğru büyüterek getirir.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "15", Toast.LENGTH_SHORT).show();
            float height = view.getHeight();
            float width = view.getWidth();
            float scale = Math.max(position > 0 ? 1f : Math.abs(1f + position), 0.5f);
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(width * 0.5f);
            view.setPivotY(height * 0.5f);
            view.setTranslationX(position < 0 ? width * position : -width * position * 0.25f);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect16() {
        String efet_adi_16 = "Cube In Transformer: Sayfa geçişlerinde küp şeklinde bir dönme efekti uygular.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "16", Toast.LENGTH_SHORT).show();
            //view.setPivotX(position > 0 ? 0 : view.getWidth());
            //view.setPivotY(view.getHeight() * 0.5f);
            //view.setRotationY(-90f * position);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect17() {
        String efet_adi_17 = "Cube Out Transformer: Küp dönüşünün dışa doğru yapıldığı bir versiyonudur.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "17", Toast.LENGTH_SHORT).show();
            view.setPivotX(position > 0 ? view.getWidth() : 0);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(90f * position);
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect18() {
        String efet_adi_18 = "Flip Horizontal Transformer: Yatay eksende flip (çevirme) efekti ekler.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "18", Toast.LENGTH_SHORT).show();
            float rotation = 180f * position;
            view.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(rotation);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect19() {
        String efet_adi_19 = "Flip Vertical Transformer: Dikey eksende flip (çevirme) efekti ekler.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "19", Toast.LENGTH_SHORT).show();
            float rotation = -180f * position;
            view.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationX(rotation);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect20() {
        String efet_adi_20 = "Zoom In Transformer: Sayfaya yakınlaşırken bir zoom-in efekti uygular.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "20", Toast.LENGTH_SHORT).show();
            //final float normalizedPosition = Math.abs(Math.abs(position) - 1);
            //view.setScaleX(normalizedPosition / 2 + 0.5f);
            //view.setScaleY(normalizedPosition / 2 + 0.5f);
            //view.setAlpha(normalizedPosition);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect21() {
        String efet_adi_21 = "Stack Transformer: Sayfaları üst üste yığar gibi gösterir.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "21", Toast.LENGTH_SHORT).show();
            if (position <= 0.0f) {
                view.setTranslationX(0f);
                view.setAlpha(1.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
            } else {
                view.setTranslationX(-view.getWidth() * position);
                view.setAlpha(1.0f - position);
                view.setScaleX(1.0f - position);
                view.setScaleY(1.0f - position);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect22() {
        String efet_adi_22 = "Gate Transformer: Sayfalar bir kapı açılıyormuş gibi ikiye ayrılıp açılır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "22", Toast.LENGTH_SHORT).show();
            view.setPivotX(position <= 0 ? view.getWidth() : 0);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(90f * position);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect23() {
        String efet_adi_23 = "Toss Transformer: Sayfaların fırlatılıyormuş gibi bir yan tarafa atılma efekti sağlar.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "23", Toast.LENGTH_SHORT).show();
            //float rotation = 20f * position;
            //view.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
            //view.setPivotX(view.getWidth() * 0.5f);
            //view.setPivotY(view.getHeight());
            //view.setRotationX(rotation);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect24() {
        String efet_adi_24 = "Küp Dönüşümü: Sayfalar küp şeklinde dönerken her sayfa diğer sayfaya bağlanacak şekilde dönüş yapar.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "24", Toast.LENGTH_SHORT).show();
            if (position < 0) {
                view.setPivotX(view.getWidth());
                view.setRotationY(90 * Math.abs(position));
            } else {
                view.setPivotX(0);
                view.setRotationY(-90 * Math.abs(position));
            }
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect25() {
        String efet_adi_25 = "Katlama Efekti: Sayfalar yan yana katlanıyormuş gibi gösterilir.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "25", Toast.LENGTH_SHORT).show();
            if (position < 0) {
                view.setTranslationX(view.getWidth() * -position);
                view.setScaleX(1 - Math.abs(position));
            } else {
                view.setTranslationX(0);
                view.setScaleX(1);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect26() {
        String efet_adi_26 = "Su Dalgası Efekti: Su dalgası benzeri bir animasyon ile sayfalar arası geçiş yapılır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "26", Toast.LENGTH_SHORT).show();
            if (position < 0) {
                view.setTranslationX(view.getWidth() * -position);
                view.setAlpha(1 - Math.abs(position));
            } else {
                view.setTranslationX(0);
                view.setAlpha(1);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect27() {
        String efet_adi_27 = "Dikey Kayma Efekti: Sayfalar dikey olarak kaydırılır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "27t", Toast.LENGTH_SHORT).show();
            if (position < 0) {
                view.setTranslationY(view.getHeight() * position);
            } else {
                view.setTranslationY(0);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect28() {
        String efet_adi_28 = "Zoom Out Slide Efekti: Sayfa değişikliği sırasında mevcut sayfanın küçülüp yeni sayfanın büyüyerek gelmesi sağlanır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "28", Toast.LENGTH_SHORT).show();
            if (position <= 0) {
                view.setScaleX(1 + position);
                view.setScaleY(1 + position);
            } else {
                view.setScaleX(1 - position);
                view.setScaleY(1 - position);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect29() {
        String efet_adi_29 = "Yatay Çevirme Efekti: Sayfalar yatay olarak çevrilecek.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "29", Toast.LENGTH_SHORT).show();
            if (position >= -1 && position <= 1) {
                view.setTranslationX(view.getWidth() * -position);
                view.setCameraDistance(12000);
                view.setRotationY(180 * position);
            } else {
                view.setRotationY(0);
            }
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect30() {
        String efet_adi_30 = "Yakınlaştırarak Slayt Efekti: Sayfalar yakınlaştırılarak kaydırılır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "30", Toast.LENGTH_SHORT).show();
            //final float normalizedposition = Math.abs(Math.abs(position) - 1);
            //view.setScaleX(normalizedposition / 2 + 0.5f);
            //view.setScaleY(normalizedposition / 2 + 0.5f);
            //view.setAlpha(normalizedposition);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect31() {
        String efet_adi_31 = "Parallax Sayfa Efekti: Sayfalar arasında geçiş yapılırken parallax efekti uygulanır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "31", Toast.LENGTH_SHORT).show();
            //int pageWidth = view.getWidth();
            //if (position < -1) {
            //    view.setAlpha(0);
            //} else if (position <= 1) {
            //    view.setTranslationX(pageWidth * -position);
            //    view.setAlpha(1 - Math.abs(position));
            //} else {
            //    view.setAlpha(0);
            //}

        };
    }



    public static ViewPager.PageTransformer applyDepthEffect33() {
        String efet_adi_33 = "Sayfa Sıkıştırma Efekti: Sayfalar kaydırıldıkça sanki sıkıştırılıyormuş gibi etkileşime girer.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "33", Toast.LENGTH_SHORT).show();
            float scale = 1 - 0.35f * Math.abs(position);
            view.setScaleX(scale);
            view.setScaleY(scale);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect34() {
        String efet_adi_34 = "Sayfa Yatay Yansıma Efekti: Sayfa dikey bir aks üzerinde yansıtılarak kaydırılır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "34t", Toast.LENGTH_SHORT).show();
            view.setTranslationX(view.getWidth() * -position);
            view.setScaleX(1 - Math.abs(position));
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect35() {
        String efet_adi_35 = "Dalga Efekti: Sayfalar dalga dalga kayar.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "35", Toast.LENGTH_SHORT).show();
            float yPosition = position < 0 ? position + 1 : Math.abs(1 - position);
            view.setTranslationY(view.getHeight() * yPosition * 0.5f);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect36() {
        String efet_adi_36 = "Halka Dönüşümü: Sayfalar, merkezi bir noktaya doğru dönerken halka şeklinde bir görünüm kazanır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "36", Toast.LENGTH_SHORT).show();
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotation(360 * position);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect37() {
        String efet_adi_37 = "Saydam Geçiş: Sayfalar arasında geçiş saydam bir efektle gerçekleşir.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "37", Toast.LENGTH_SHORT).show();
            view.setAlpha(1 - Math.abs(position));
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect38() {
        String efet_adi_38 = "Spiral Efekti: Sayfalar, spiral bir hareketle dönerken kaydırılır.";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "38t", Toast.LENGTH_SHORT).show();
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotation(360 * position);
            view.setScaleX(1 - Math.abs(position));
            view.setScaleY(1 - Math.abs(position));
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect39() {
        String efet_adi_39 = "Modern Efektler : Neon Pulse ";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "39", Toast.LENGTH_SHORT).show();
            view.setAlpha(0.5f + (1 - Math.abs(position)));
            view.setBackgroundColor(Color.argb(Math.abs(position) * 255, 255, 0, 255));
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect40() {
        String efet_adi_40 = "Modern Efektler: Digital Slide";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "40", Toast.LENGTH_SHORT).show();
            //view.setTranslationX(view.getWidth() * -position);
            //view.setAlpha(1 - Math.abs(position));
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect41() {
        String efet_adi_41 = "Modern Efektler: Holographic Turn";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "41", Toast.LENGTH_SHORT).show();
            //view.setAlpha(0.5f + (1 - Math.abs(position)));
            //view.setScaleX(0.5f + (1 - Math.abs(position)));
            //view.setScaleY(0.5f + (1 - Math.abs(position)));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect42() {
        String efet_adi_42 = "Modern Efektler: Light Speed";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "42", Toast.LENGTH_SHORT).show();
            view.setTranslationX(position < 0 ? 0 : -view.getWidth() * position);
            view.setAlpha(1 - Math.abs(position));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect43() {
        String efet_adi_43 = "Modern Efektler: Quantum Leap";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "43", Toast.LENGTH_SHORT).show();
            view.setTranslationX(view.getWidth() * -position);
            if (position <= 0) {
                view.setAlpha(1 + position);
            } else {
                view.setAlpha(1 - position);
            }

        };
    }


    public static ViewPager.PageTransformer applyDepthEffect44() {
        String efet_adi_44 = "Füturistik Efektler: Astro Zoom";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "44", Toast.LENGTH_SHORT).show();
            //view.setScaleX(1 - Math.abs(position));
            //view.setScaleY(1 - Math.abs(position));
            //view.setAlpha(1 - Math.abs(position));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect45() {
        String efet_adi_45 = "Füturistik Efektler: Cyber Wave";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "45", Toast.LENGTH_SHORT).show();
            view.setRotationY(position * 45);
            view.setAlpha(1 - Math.abs(position));
        };
    }



    public static ViewPager.PageTransformer applyDepthEffect46() {
        String efet_adi_46 = "Füturistik Efektler: Plasma Flow";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "46", Toast.LENGTH_SHORT).show();
            //view.setTranslationX(position < 0 ? 0 : -view.getWidth() * position);
            //view.setBackgroundColor(Color.argb(Math.abs(position) * 255, 0, 0, 255));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect47() {
        String efet_adi_47 = "Füturistik Efektler: Galactic Spin";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "47", Toast.LENGTH_SHORT).show();
            //view.setRotation(position * 360);
            //view.setAlpha(1 - Math.abs(position));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect48() {
        String efet_adi_48 = "Füturistik Efektler: Orbital Shift";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "48t", Toast.LENGTH_SHORT).show();
            view.setTranslationX((float) (-(1 - Math.cos(position * Math.PI)) * 500));
            view.setAlpha(1 - Math.abs(position));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect49() {
        String efet_adi_49 = "Efsanevi Efektler: Dragon’s Breath";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "49", Toast.LENGTH_SHORT).show();
            view.setAlpha(0.5f + (1 - Math.abs(position)));
            view.setBackgroundColor(Color.argb(Math.abs(position) * 128, 255, 165, 0));
        };
    }


    public static ViewPager.PageTransformer applyDepthEffect50() {
        String efet_adi_50 = "Efsanevi Efektler: Wizard’s Portal";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "50", Toast.LENGTH_SHORT).show();
            //view.setScaleX(0.8f + (1 - Math.abs(position)) * 0.2f);
            //view.setScaleY(0.8f + (1 - Math.abs(position)) * 0.2f);
            //view.setRotation(position * 720);
        };
    }

    public static ViewPager.PageTransformer applyDepthEffect51() {
        String efet_adi_51 = "Efsanevi Efektler: Phoenix Rise";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "51", Toast.LENGTH_SHORT).show();
            //view.setTranslationX(view.getWidth() * -position);
            //view.setAlpha(0.5f + (1 - Math.abs(position)));
            //view.setScaleX(0.5f + (1 - Math.abs(position)));
            //view.setScaleY(0.5f + (1 - Math.abs(position)));
        };
    }










    public static ViewPager.PageTransformer applyDepthEffect55() {
        String efet_adi_55 = "Kadim Efektler: ";
        return (view, position) -> {
            Toast.makeText(view.getContext(), "55", Toast.LENGTH_SHORT).show();


        };
    }








































  */



    }
