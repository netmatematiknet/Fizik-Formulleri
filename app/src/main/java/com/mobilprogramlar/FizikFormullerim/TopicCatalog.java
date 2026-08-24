package com.mobilprogramlar.FizikFormullerim;

import java.util.ArrayList;
import java.util.List;

public final class TopicCatalog {

    private static List<Topic> cachedAll;

    private TopicCatalog() {
    }

    public static final class Topic {
        public final int id;
        public final String title;
        public final String level;
        public final int iconRes;
        public final int[] imageResIds;

        public Topic(int id, String title, String level, int iconRes, int[] imageResIds) {
            this.id = id;
            this.title = title;
            this.level = level;
            this.iconRes = iconRes;
            this.imageResIds = imageResIds;
        }
    }

    public static List<Topic> all() {
        if (cachedAll != null) {
            return cachedAll;
        }
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic(0, "Fizik Bilimine Giriş", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f01_fizik_bil_giris_01, R.drawable.f01_fizik_bil_giris_02, R.drawable.f01_fizik_bil_giris_03,
                R.drawable.f01_fizik_bil_giris_04, R.drawable.f01_fizik_bil_giris_05
        }));
        topics.add(new Topic(1, "Madde ve Özellikleri: Kütle ve Özkütle", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f02_madde_ozel_kutle_01, R.drawable.f02_madde_ozel_kutle_02, R.drawable.f02_madde_ozel_kutle_03,
                R.drawable.f02_madde_ozel_kutle_04, R.drawable.f02_madde_ozel_kutle_05, R.drawable.f02_madde_ozel_kutle_06,
                R.drawable.f02_madde_ozel_kutle_07, R.drawable.f02_madde_ozel_kutle_08, R.drawable.f02_madde_ozel_kutle_09,
                R.drawable.f02_madde_ozel_kutle_10, R.drawable.f02_madde_ozel_kutle_11, R.drawable.f02_madde_ozel_kutle_12,
                R.drawable.f02_madde_ozel_kutle_13
        }));
        topics.add(new Topic(2, "Madde ve Özellikleri: Dayanıklılık ve Kılcallık", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f02_madde_ozel_dayaniklilik_01, R.drawable.f02_madde_ozel_dayaniklilik_02,
                R.drawable.f02_madde_ozel_dayaniklilik_03, R.drawable.f02_madde_ozel_dayaniklilik_04,
                R.drawable.f02_madde_ozel_dayaniklilik_05, R.drawable.f02_madde_ozel_dayaniklilik_06,
                R.drawable.f02_madde_ozel_dayaniklilik_07, R.drawable.f02_madde_ozel_dayaniklilik_08,
                R.drawable.f02_madde_ozel_dayaniklilik_09, R.drawable.f02_madde_ozel_dayaniklilik_10
        }));
        topics.add(new Topic(3, "Kuvvet ve Hareket: Tek Boyutta Hareket", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f03_kuvvet_hareket_tekboyut_01, R.drawable.f03_kuvvet_hareket_tekboyut_02,
                R.drawable.f03_kuvvet_hareket_tekboyut_03, R.drawable.f03_kuvvet_hareket_tekboyut_04,
                R.drawable.f03_kuvvet_hareket_tekboyut_05, R.drawable.f03_kuvvet_hareket_tekboyut_06,
                R.drawable.f03_kuvvet_hareket_tekboyut_07, R.drawable.f03_kuvvet_hareket_tekboyut_08,
                R.drawable.f03_kuvvet_hareket_tekboyut_09, R.drawable.f03_kuvvet_hareket_tekboyut_10
        }));
        topics.add(new Topic(4, "Kuvvet ve Hareket: Newton Yasaları", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f03_kuvvet_hareket_newton_01, R.drawable.f03_kuvvet_hareket_newton_02,
                R.drawable.f03_kuvvet_hareket_newton_03, R.drawable.f03_kuvvet_hareket_newton_04,
                R.drawable.f03_kuvvet_hareket_newton_05, R.drawable.f03_kuvvet_hareket_newton_06,
                R.drawable.f03_kuvvet_hareket_newton_07, R.drawable.f03_kuvvet_hareket_newton_08,
                R.drawable.f03_kuvvet_hareket_newton_09, R.drawable.f03_kuvvet_hareket_newton_10,
                R.drawable.f03_kuvvet_hareket_newton_11, R.drawable.f03_kuvvet_hareket_newton_12,
                R.drawable.f03_kuvvet_hareket_newton_13, R.drawable.f03_kuvvet_hareket_newton_14,
                R.drawable.f03_kuvvet_hareket_newton_15, R.drawable.f03_kuvvet_hareket_newton_16,
                R.drawable.f03_kuvvet_hareket_newton_17, R.drawable.f03_kuvvet_hareket_newton_18,
                R.drawable.f03_kuvvet_hareket_newton_19, R.drawable.f03_kuvvet_hareket_newton_20,
                R.drawable.f03_kuvvet_hareket_newton_21, R.drawable.f03_kuvvet_hareket_newton_22,
                R.drawable.f03_kuvvet_hareket_newton_23, R.drawable.f03_kuvvet_hareket_newton_24,
                R.drawable.f03_kuvvet_hareket_newton_25
        }));
        // Fixed: third image is isguc_03 (was incorrectly isguc_02 twice in FormullerFizik)
        topics.add(new Topic(5, "Kuvvet ve Hareket: İş, Güç ve Enerji", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f03_kuvvet_hareket_isguc_01, R.drawable.f03_kuvvet_hareket_isguc_02,
                R.drawable.f03_kuvvet_hareket_isguc_03, R.drawable.f03_kuvvet_hareket_isguc_04,
                R.drawable.f03_kuvvet_hareket_isguc_05, R.drawable.f03_kuvvet_hareket_isguc_06,
                R.drawable.f03_kuvvet_hareket_isguc_07, R.drawable.f03_kuvvet_hareket_isguc_08,
                R.drawable.f03_kuvvet_hareket_isguc_09, R.drawable.f03_kuvvet_hareket_isguc_10,
                R.drawable.f03_kuvvet_hareket_isguc_11, R.drawable.f03_kuvvet_hareket_isguc_12,
                R.drawable.f03_kuvvet_hareket_isguc_13, R.drawable.f03_kuvvet_hareket_isguc_14,
                R.drawable.f03_kuvvet_hareket_isguc_15, R.drawable.f03_kuvvet_hareket_isguc_16,
                R.drawable.f03_kuvvet_hareket_isguc_17, R.drawable.f03_kuvvet_hareket_isguc_18,
                R.drawable.f03_kuvvet_hareket_isguc_19, R.drawable.f03_kuvvet_hareket_isguc_20,
                R.drawable.f03_kuvvet_hareket_isguc_21, R.drawable.f03_kuvvet_hareket_isguc_22,
                R.drawable.f03_kuvvet_hareket_isguc_23, R.drawable.f03_kuvvet_hareket_isguc_24
        }));
        topics.add(new Topic(6, "Isı, Sicaklık, Hal Değişimi, Genleşme", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f04_isi_sicaklik_01, R.drawable.f04_isi_sicaklik_02, R.drawable.f04_isi_sicaklik_03,
                R.drawable.f04_isi_sicaklik_04, R.drawable.f04_isi_sicaklik_05, R.drawable.f04_isi_sicaklik_06,
                R.drawable.f04_isi_sicaklik_07, R.drawable.f04_isi_sicaklik_08, R.drawable.f04_isi_sicaklik_09,
                R.drawable.f04_isi_sicaklik_10, R.drawable.f04_isi_sicaklik_11, R.drawable.f04_isi_sicaklik_12,
                R.drawable.f04_isi_sicaklik_13, R.drawable.f04_isi_sicaklik_14, R.drawable.f04_isi_sicaklik_15,
                R.drawable.f04_isi_sicaklik_16, R.drawable.f04_isi_sicaklik_17, R.drawable.f04_isi_sicaklik_18,
                R.drawable.f04_isi_sicaklik_19, R.drawable.f04_isi_sicaklik_20, R.drawable.f04_isi_sicaklik_21,
                R.drawable.f04_isi_sicaklik_22, R.drawable.f04_isi_sicaklik_23, R.drawable.f04_isi_sicaklik_24,
                R.drawable.f04_isi_sicaklik_25, R.drawable.f04_isi_sicaklik_26, R.drawable.f04_isi_sicaklik_27,
                R.drawable.f04_isi_sicaklik_28, R.drawable.f04_isi_sicaklik_29, R.drawable.f04_isi_sicaklik_30,
                R.drawable.f04_isi_sicaklik_31, R.drawable.f04_isi_sicaklik_32, R.drawable.f04_isi_sicaklik_33,
                R.drawable.f04_isi_sicaklik_34, R.drawable.f04_isi_sicaklik_35, R.drawable.f04_isi_sicaklik_36,
                R.drawable.f04_isi_sicaklik_37, R.drawable.f04_isi_sicaklik_38, R.drawable.f04_isi_sicaklik_39,
                R.drawable.f04_isi_sicaklik_40, R.drawable.f04_isi_sicaklik_41, R.drawable.f04_isi_sicaklik_42
        }));
        topics.add(new Topic(7, "Basınç ve Kaldırma Kuvveti: Basınç", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f05_basinc_kaldirma_basinc_01, R.drawable.f05_basinc_kaldirma_basinc_02,
                R.drawable.f05_basinc_kaldirma_basinc_03, R.drawable.f05_basinc_kaldirma_basinc_04,
                R.drawable.f05_basinc_kaldirma_basinc_05, R.drawable.f05_basinc_kaldirma_basinc_06,
                R.drawable.f05_basinc_kaldirma_basinc_07, R.drawable.f05_basinc_kaldirma_basinc_08,
                R.drawable.f05_basinc_kaldirma_basinc_09, R.drawable.f05_basinc_kaldirma_basinc_10,
                R.drawable.f05_basinc_kaldirma_basinc_11, R.drawable.f05_basinc_kaldirma_basinc_12,
                R.drawable.f05_basinc_kaldirma_basinc_13, R.drawable.f05_basinc_kaldirma_basinc_14,
                R.drawable.f05_basinc_kaldirma_basinc_15, R.drawable.f05_basinc_kaldirma_basinc_16,
                R.drawable.f05_basinc_kaldirma_basinc_17, R.drawable.f05_basinc_kaldirma_basinc_18,
                R.drawable.f05_basinc_kaldirma_basinc_19, R.drawable.f05_basinc_kaldirma_basinc_20
        }));
        topics.add(new Topic(8, "Basınç ve Kaldırma Kuvveti: Kaldırma Kuvveti", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f05_basinc_kaldirma_kald_kuv_01, R.drawable.f05_basinc_kaldirma_kald_kuv_02,
                R.drawable.f05_basinc_kaldirma_kald_kuv_03, R.drawable.f05_basinc_kaldirma_kald_kuv_04,
                R.drawable.f05_basinc_kaldirma_kald_kuv_05, R.drawable.f05_basinc_kaldirma_kald_kuv_06,
                R.drawable.f05_basinc_kaldirma_kald_kuv_07
        }));
        topics.add(new Topic(9, "Elektrik ve Manyetizma: Elektrostatik", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f06_elektrik_many_elektrostatik_01, R.drawable.f06_elektrik_many_elektrostatik_02,
                R.drawable.f06_elektrik_many_elektrostatik_03, R.drawable.f06_elektrik_many_elektrostatik_04,
                R.drawable.f06_elektrik_many_elektrostatik_05, R.drawable.f06_elektrik_many_elektrostatik_06
        }));
        topics.add(new Topic(10, "Elektrik ve Manyetizma: Elektrik Akımı", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f06_elektrik_many_elektrikakimi_01, R.drawable.f06_elektrik_many_elektrikakimi_02,
                R.drawable.f06_elektrik_many_elektrikakimi_03, R.drawable.f06_elektrik_many_elektrikakimi_04,
                R.drawable.f06_elektrik_many_elektrikakimi_05, R.drawable.f06_elektrik_many_elektrikakimi_06,
                R.drawable.f06_elektrik_many_elektrikakimi_07, R.drawable.f06_elektrik_many_elektrikakimi_08,
                R.drawable.f06_elektrik_many_elektrikakimi_09, R.drawable.f06_elektrik_many_elektrikakimi_10,
                R.drawable.f06_elektrik_many_elektrikakimi_11, R.drawable.f06_elektrik_many_elektrikakimi_12,
                R.drawable.f06_elektrik_many_elektrikakimi_13, R.drawable.f06_elektrik_many_elektrikakimi_14,
                R.drawable.f06_elektrik_many_elektrikakimi_15, R.drawable.f06_elektrik_many_elektrikakimi_16,
                R.drawable.f06_elektrik_many_elektrikakimi_17, R.drawable.f06_elektrik_many_elektrikakimi_18,
                R.drawable.f06_elektrik_many_elektrikakimi_19, R.drawable.f06_elektrik_many_elektrikakimi_20,
                R.drawable.f06_elektrik_many_elektrikakimi_21, R.drawable.f06_elektrik_many_elektrikakimi_22,
                R.drawable.f06_elektrik_many_elektrikakimi_23
        }));
        topics.add(new Topic(11, "Elektrik ve Manyetizma: Mıknatıslar ve Manyetik Alan", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f06_elektrik_many_manyetizma_01, R.drawable.f06_elektrik_many_manyetizma_02,
                R.drawable.f06_elektrik_many_manyetizma_03, R.drawable.f06_elektrik_many_manyetizma_04,
                R.drawable.f06_elektrik_many_manyetizma_05, R.drawable.f06_elektrik_many_manyetizma_06,
                R.drawable.f06_elektrik_many_manyetizma_07, R.drawable.f06_elektrik_many_manyetizma_08,
                R.drawable.f06_elektrik_many_manyetizma_09, R.drawable.f06_elektrik_many_manyetizma_10,
                R.drawable.f06_elektrik_many_manyetizma_11, R.drawable.f06_elektrik_many_manyetizma_12,
                R.drawable.f06_elektrik_many_manyetizma_13, R.drawable.f06_elektrik_many_manyetizma_14,
                R.drawable.f06_elektrik_many_manyetizma_15, R.drawable.f06_elektrik_many_manyetizma_16,
                R.drawable.f06_elektrik_many_manyetizma_17, R.drawable.f06_elektrik_many_manyetizma_18,
                R.drawable.f06_elektrik_many_manyetizma_19, R.drawable.f06_elektrik_many_manyetizma_20
        }));
        topics.add(new Topic(12, "Optik: Aydınlanma ve Gölge", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f07_optik_aydinlanma_golge_01, R.drawable.f07_optik_aydinlanma_golge_02,
                R.drawable.f07_optik_aydinlanma_golge_03, R.drawable.f07_optik_aydinlanma_golge_04,
                R.drawable.f07_optik_aydinlanma_golge_05, R.drawable.f07_optik_aydinlanma_golge_06,
                R.drawable.f07_optik_aydinlanma_golge_07, R.drawable.f07_optik_aydinlanma_golge_08,
                R.drawable.f07_optik_aydinlanma_golge_09, R.drawable.f07_optik_aydinlanma_golge_10,
                R.drawable.f07_optik_aydinlanma_golge_11, R.drawable.f07_optik_aydinlanma_golge_12,
                R.drawable.f07_optik_aydinlanma_golge_13, R.drawable.f07_optik_aydinlanma_golge_14,
                R.drawable.f07_optik_aydinlanma_golge_15
        }));
        topics.add(new Topic(13, "Optik: Işığın Yansıması ve Düzlem Ayna", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f07_optik_yans_duz_ayna_01, R.drawable.f07_optik_yans_duz_ayna_02,
                R.drawable.f07_optik_yans_duz_ayna_03, R.drawable.f07_optik_yans_duz_ayna_04,
                R.drawable.f07_optik_yans_duz_ayna_05, R.drawable.f07_optik_yans_duz_ayna_06,
                R.drawable.f07_optik_yans_duz_ayna_07, R.drawable.f07_optik_yans_duz_ayna_08
        }));
        topics.add(new Topic(14, "Optik: Küresel Aynalar", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f07_optik_kuresel_ayna_01, R.drawable.f07_optik_kuresel_ayna_02,
                R.drawable.f07_optik_kuresel_ayna_03, R.drawable.f07_optik_kuresel_ayna_04,
                R.drawable.f07_optik_kuresel_ayna_05, R.drawable.f07_optik_kuresel_ayna_06,
                R.drawable.f07_optik_kuresel_ayna_07, R.drawable.f07_optik_kuresel_ayna_08,
                R.drawable.f07_optik_kuresel_ayna_09
        }));
        topics.add(new Topic(15, "Optik: Işığın Kırılması ve Renk", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f07_optik_kirilma_renk_01, R.drawable.f07_optik_kirilma_renk_02,
                R.drawable.f07_optik_kirilma_renk_03, R.drawable.f07_optik_kirilma_renk_04,
                R.drawable.f07_optik_kirilma_renk_05, R.drawable.f07_optik_kirilma_renk_06,
                R.drawable.f07_optik_kirilma_renk_07, R.drawable.f07_optik_kirilma_renk_08,
                R.drawable.f07_optik_kirilma_renk_09, R.drawable.f07_optik_kirilma_renk_10,
                R.drawable.f07_optik_kirilma_renk_11, R.drawable.f07_optik_kirilma_renk_12,
                R.drawable.f07_optik_kirilma_renk_13, R.drawable.f07_optik_kirilma_renk_14,
                R.drawable.f07_optik_kirilma_renk_15, R.drawable.f07_optik_kirilma_renk_16,
                R.drawable.f07_optik_kirilma_renk_17, R.drawable.f07_optik_kirilma_renk_18,
                R.drawable.f07_optik_kirilma_renk_19, R.drawable.f07_optik_kirilma_renk_20,
                R.drawable.f07_optik_kirilma_renk_21
        }));
        topics.add(new Topic(16, "Optik: Mercekler ve Optik Araçlar", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f07_optik_mercek_optik_01, R.drawable.f07_optik_mercek_optik_02,
                R.drawable.f07_optik_mercek_optik_03, R.drawable.f07_optik_mercek_optik_04,
                R.drawable.f07_optik_mercek_optik_05, R.drawable.f07_optik_mercek_optik_06,
                R.drawable.f07_optik_mercek_optik_07, R.drawable.f07_optik_mercek_optik_08,
                R.drawable.f07_optik_mercek_optik_09, R.drawable.f07_optik_mercek_optik_10,
                R.drawable.f07_optik_mercek_optik_11, R.drawable.f07_optik_mercek_optik_12,
                R.drawable.f07_optik_mercek_optik_13, R.drawable.f07_optik_mercek_optik_14,
                R.drawable.f07_optik_mercek_optik_15, R.drawable.f07_optik_mercek_optik_16,
                R.drawable.f07_optik_mercek_optik_17, R.drawable.f07_optik_mercek_optik_18,
                R.drawable.f07_optik_mercek_optik_19, R.drawable.f07_optik_mercek_optik_20
        }));
        topics.add(new Topic(17, "Dalgalar: Dalgaların Genel Özellikleri", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f08_dalgalar_genel_oz_01, R.drawable.f08_dalgalar_genel_oz_02,
                R.drawable.f08_dalgalar_genel_oz_03, R.drawable.f08_dalgalar_genel_oz_04,
                R.drawable.f08_dalgalar_genel_oz_05, R.drawable.f08_dalgalar_genel_oz_06,
                R.drawable.f08_dalgalar_genel_oz_07, R.drawable.f08_dalgalar_genel_oz_08,
                R.drawable.f08_dalgalar_genel_oz_09, R.drawable.f08_dalgalar_genel_oz_10
        }));
        topics.add(new Topic(18, "Dalgalar: Yay Dalgaları", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f08_dalgalar_yay_dalgalari_01, R.drawable.f08_dalgalar_yay_dalgalari_02,
                R.drawable.f08_dalgalar_yay_dalgalari_03, R.drawable.f08_dalgalar_yay_dalgalari_04,
                R.drawable.f08_dalgalar_yay_dalgalari_05, R.drawable.f08_dalgalar_yay_dalgalari_06,
                R.drawable.f08_dalgalar_yay_dalgalari_07, R.drawable.f08_dalgalar_yay_dalgalari_08,
                R.drawable.f08_dalgalar_yay_dalgalari_09, R.drawable.f08_dalgalar_yay_dalgalari_10
        }));
        topics.add(new Topic(19, "Dalgalar: Su Dalgaları", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f08_dalgalar_su_dalgalari_01, R.drawable.f08_dalgalar_su_dalgalari_02,
                R.drawable.f08_dalgalar_su_dalgalari_03, R.drawable.f08_dalgalar_su_dalgalari_04,
                R.drawable.f08_dalgalar_su_dalgalari_05, R.drawable.f08_dalgalar_su_dalgalari_06,
                R.drawable.f08_dalgalar_su_dalgalari_07, R.drawable.f08_dalgalar_su_dalgalari_08,
                R.drawable.f08_dalgalar_su_dalgalari_09, R.drawable.f08_dalgalar_su_dalgalari_10,
                R.drawable.f08_dalgalar_su_dalgalari_11, R.drawable.f08_dalgalar_su_dalgalari_12
        }));
        topics.add(new Topic(20, "Dalgalar: Ses ve Deprem Dalgaları", "Tyt", R.drawable.einstein_equation, new int[]{
                R.drawable.f08_dalgalar_ses_deprem_01, R.drawable.f08_dalgalar_ses_deprem_02,
                R.drawable.f08_dalgalar_ses_deprem_03, R.drawable.f08_dalgalar_ses_deprem_04,
                R.drawable.f08_dalgalar_ses_deprem_05, R.drawable.f08_dalgalar_ses_deprem_06,
                R.drawable.f08_dalgalar_ses_deprem_07, R.drawable.f08_dalgalar_ses_deprem_08,
                R.drawable.f08_dalgalar_ses_deprem_09, R.drawable.f08_dalgalar_ses_deprem_10,
                R.drawable.f08_dalgalar_ses_deprem_11, R.drawable.f08_dalgalar_ses_deprem_12,
                R.drawable.f08_dalgalar_ses_deprem_13, R.drawable.f08_dalgalar_ses_deprem_14,
                R.drawable.f08_dalgalar_ses_deprem_15, R.drawable.f08_dalgalar_ses_deprem_16,
                R.drawable.f08_dalgalar_ses_deprem_17
        }));
        cachedAll = topics;
        return cachedAll;
    }

    public static Topic getById(int id) {
        for (Topic topic : all()) {
            if (topic.id == id) {
                return topic;
            }
        }
        return null;
    }

    public static int[] imagesFor(int id) {
        Topic topic = getById(id);
        return topic != null ? topic.imageResIds : new int[0];
    }

    public static String titleFor(int id) {
        Topic topic = getById(id);
        return topic != null ? topic.title : "";
    }
}
