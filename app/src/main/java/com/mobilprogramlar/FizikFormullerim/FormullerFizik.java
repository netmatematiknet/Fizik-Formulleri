package com.mobilprogramlar.FizikFormullerim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FormullerFizik extends AppCompatActivity {

    private enum LevelFilter { TYT, AYT, ALL }

    private final List<Person> allPersons = new ArrayList<>();
    private final List<Person> visiblePersons = new ArrayList<>();
    private PersonAdapter personAdapter;
    private boolean favoritesOnly;
    private LevelFilter levelFilter = LevelFilter.TYT;
    private String searchQuery = "";
    private SlidingPaneLayout slidingPane;
    private View detailContent;
    private TextView detailPlaceholder;
    private TextView emptyTopics;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formuller_fizik);
        KenarHelper.apply(this);
        setupToolbar();

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle screen = new Bundle();
        screen.putString(FirebaseAnalytics.Param.SCREEN_NAME, "FormullerFizik");
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, screen);

        FrameLayout adContainer = findViewById(R.id.ad_banner_container);
        if (AdsConsentHelper.canRequestAds(this)) {
            AdsConsentHelper.initializeAds(this);
            AdManager.loadAdaptiveBannerAd(this, adContainer);
            AdManager.loadInterstitialAd(this);
        }

        slidingPane = findViewById(R.id.sliding_pane);
        detailContent = findViewById(R.id.detail_content);
        detailPlaceholder = findViewById(R.id.tv_detail_placeholder);
        emptyTopics = findViewById(R.id.tv_empty_topics);
        listView = findViewById(R.id.listView_persons);

        for (TopicCatalog.Topic topic : TopicCatalog.all()) {
            allPersons.add(new Person(
                    topic.id,
                    topic.title,
                    topic.level,
                    topic.iconRes,
                    topic.imageResIds.length));
        }

        personAdapter = new PersonAdapter(this, R.layout.formuller_listview, visiblePersons, person -> {
            FavoritesStore.toggle(this, person.getTopicId());
            refreshList();
        });
        listView.setAdapter(personAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Person person = visiblePersons.get(position);
            openTopic(person.getTopicId());
        });

        ChipGroup chipGroup = findViewById(R.id.chip_level_filter);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                return;
            }
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_ayt) {
                levelFilter = LevelFilter.AYT;
            } else if (checkedId == R.id.chip_all) {
                levelFilter = LevelFilter.ALL;
            } else {
                levelFilter = LevelFilter.TYT;
            }
            refreshList();
        });

        SearchView searchView = findViewById(R.id.search_topics);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query == null ? "" : query;
                refreshList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText == null ? "" : newText;
                refreshList();
                return true;
            }
        });

        refreshList();
    }

    private void openTopic(int topicId) {
        TopicCatalog.Topic topic = TopicCatalog.getById(topicId);
        if (topic == null) {
            return;
        }
        FirebaseCrashlytics.getInstance().log("open_topic:" + topicId + ":" + topic.title);
        FirebaseCrashlytics.getInstance().setCustomKey("last_topic_id", topicId);
        loadTopicIntoEeprom(topic);

        if (detailContent != null) {
            showTopicInDetailPane(topic);
            if (slidingPane != null) {
                slidingPane.openPane();
            }
            return;
        }

        AdManager.showFormulaInterstitialThen(this, () -> {
            Intent next = new Intent(FormullerFizik.this, FormulGoster.class);
            next.putExtra("brans", 0);
            next.putExtra("id", topicId);
            next.putExtra("title", topic.title);
            startActivity(next);
        });
    }

    private void loadTopicIntoEeprom(TopicCatalog.Topic topic) {
        EEPROM eeprom = new EEPROM();
        eeprom.setListCount(topic.imageResIds.length);
        for (int i = 0; i < topic.imageResIds.length; i++) {
            eeprom.write(i, topic.imageResIds[i], topic.title);
        }
    }

    private void showTopicInDetailPane(TopicCatalog.Topic topic) {
        if (detailPlaceholder != null) {
            detailPlaceholder.setVisibility(View.GONE);
        }
        detailContent.setVisibility(View.VISIBLE);

        ExtendedViewPager pager = detailContent.findViewById(R.id.view_pager);
        TextView indicator = detailContent.findViewById(R.id.tv_page_indicator);
        FloatingActionButton fabBack = detailContent.findViewById(R.id.fab_back);
        FloatingActionButton fabInfo = detailContent.findViewById(R.id.fab_info);

        FormulaPagerHelper.bind(pager, indicator, topic.imageResIds, topic.title);

        if (fabBack != null) {
            fabBack.setOnClickListener(v -> {
                if (slidingPane != null) {
                    slidingPane.closePane();
                }
            });
        }
        if (fabInfo != null) {
            fabInfo.setOnClickListener(v ->
                    Toast.makeText(this, R.string.formul_bilgi_mesaj, Toast.LENGTH_LONG).show());
        }
    }

    private void refreshList() {
        visiblePersons.clear();
        Locale locale = Locale.getDefault();
        String q = searchQuery.trim().toLowerCase(locale);
        for (Person person : allPersons) {
            if (favoritesOnly && !FavoritesStore.isFavorite(this, person.getTopicId())) {
                continue;
            }
            if (levelFilter == LevelFilter.TYT && !"Tyt".equalsIgnoreCase(person.getAddress())) {
                continue;
            }
            if (levelFilter == LevelFilter.AYT && !"Ayt".equalsIgnoreCase(person.getAddress())) {
                continue;
            }
            if (!TextUtils.isEmpty(q)) {
                String haystack = (person.getName() + " " + person.getAddress()).toLowerCase(locale);
                if (!haystack.contains(q)) {
                    continue;
                }
            }
            visiblePersons.add(person);
        }
        personAdapter.notifyDataSetChanged();

        boolean empty = visiblePersons.isEmpty();
        if (emptyTopics != null) {
            emptyTopics.setVisibility(empty ? View.VISIBLE : View.GONE);
            if (empty && levelFilter == LevelFilter.AYT) {
                emptyTopics.setText(R.string.ayt_yakinda);
            } else if (empty) {
                emptyTopics.setText(R.string.konu_sec_placeholder);
            }
        }
        if (listView != null) {
            listView.setVisibility(empty ? View.GONE : View.VISIBLE);
        }
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu_topics);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.toolbar_anasayfa) {
                anasayfa();
            } else if (id == R.id.toolbar_kapat1) {
                finishAffinity();
            } else if (id == R.id.toolbar_paylas) {
                paylas();
            } else if (id == R.id.toolbar_gizlilik) {
                startActivity(new Intent(this, GizlilikPolitikasi.class));
            } else if (id == R.id.toolbar_reklam_tercihleri) {
                if (AdsConsentHelper.isPrivacyOptionsRequired(this)) {
                    AdsConsentHelper.showPrivacyOptionsForm(this);
                } else {
                    startActivity(new Intent(this, GizlilikPolitikasi.class));
                }
            } else if (id == R.id.toolbar_favorites) {
                favoritesOnly = !favoritesOnly;
                item.setTitle(favoritesOnly ? R.string.tum_konular : R.string.sadece_favoriler);
                refreshList();
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onPause() {
        AdManager.pauseBanners();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        AdManager.resumeBanners();
        refreshList();
    }

    public void anasayfa() {
        Intent myIntent = new Intent(getApplicationContext(), UygulamaAnaSayfa.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myIntent);
        finish();
    }

    public void paylas() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.paylas_baslik));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.paylas_mesaj_tam));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.paylas_mesaj_3)));
    }
}
