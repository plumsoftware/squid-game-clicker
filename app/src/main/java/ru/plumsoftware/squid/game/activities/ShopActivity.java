package ru.plumsoftware.squid.game.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;

import java.util.List;

import ru.plumsoftware.squid.game.R;
import ru.plumsoftware.squid.game.adapter.HeroAdapter;
import ru.plumsoftware.squid.game.data.AdsIds;
import ru.plumsoftware.squid.game.data.Data;
import ru.plumsoftware.squid.game.dialogs.CustomProgressDialog;
import ru.plumsoftware.squid.game.heroes.Hero;
import ru.plumsoftware.squid.game.heroes.Heroes;

public class ShopActivity extends AppCompatActivity {
    private long score;
    private int click;
    private int imageResId;
    private SharedPreferences sharedPreferences;
    @Nullable
    private InterstitialAd mInterstitialAd = null;
    @Nullable
    private InterstitialAdLoader mInterstitialAdLoader = null;
    private CustomProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

//        region::Base variables
        Context context = ShopActivity.this;
        Activity activity = ShopActivity.this;

        MobileAds.initialize(this, () -> {

        });

        mInterstitialAdLoader = new InterstitialAdLoader(ShopActivity.this);
        mInterstitialAdLoader.setAdLoadListener(new InterstitialAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                progressDialog.dismissProgressDialog();
                if (mInterstitialAd != null) {
                    mInterstitialAd.setAdEventListener(new InterstitialAdEventListener() {
                        @Override
                        public void onAdShown() {

                        }

                        @Override
                        public void onAdFailedToShow(@NonNull AdError adError) {

                        }

                        @Override
                        public void onAdDismissed() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(new Intent(ShopActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onAdClicked() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(new Intent(ShopActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {

                        }
                    });
                }
                mInterstitialAd.show(ShopActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                progressDialog.dismissProgressDialog();
                finish();
                overridePendingTransition(0, 0);
                startActivity(new Intent(ShopActivity.this, MainActivity.class));
            }
        });
        progressDialog = new CustomProgressDialog(ShopActivity.this);
        progressDialog.setMessage("Загрузка...");
//        endregion

//        region::Find views
        TextView textViewScore2 = (TextView) activity.findViewById(R.id.textViewScore2);
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerViewHeroes);
//        endregion

//        region::Animations

//        endregion

//        region::Get data
        sharedPreferences = context.getSharedPreferences(Data.SP_NAME, Context.MODE_PRIVATE);
        score = sharedPreferences.getLong(Data.SP_SCORE, 0);
        click = sharedPreferences.getInt(Data.SP_CLICK, 1);
        imageResId = sharedPreferences.getInt(Data.SP_IMAGE_RES_ID, R.drawable.hero_1);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                textViewScore2.setText(Long.toString(sharedPreferences.getLong(Data.SP_SCORE, 0)));
            }
        });
//        endregion

//        region::Setup heroes
        textViewScore2.setText(Long.toString(score));
        List<Hero> list = Heroes.buildHeroes();
        for (int i = 0; i < list.size(); i++) {
            Hero hero = list.get(i);
            hero.setBuy(sharedPreferences.getBoolean(Data.SP_HEROES_IS_BUY[i], false));
            list.set(i, hero);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new HeroAdapter(context, list));
//        endregion

//        region::Clickers

//        endregion
    }

    @Override
    public void onBackPressed() {
        progressDialog.showProgressDialog();
        sharedPreferences.edit().putBoolean("isShowAppOpen", false).apply();
        if (mInterstitialAdLoader != null) {
            final AdRequestConfiguration adRequestConfiguration =
                    new AdRequestConfiguration.Builder(AdsIds.SHOP_ACTIVITY_ADS_ID).build();
            mInterstitialAdLoader.loadAd(adRequestConfiguration);
        }
    }
}
