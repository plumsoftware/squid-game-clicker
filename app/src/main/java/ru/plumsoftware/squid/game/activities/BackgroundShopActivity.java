package ru.plumsoftware.squid.game.activities;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

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
import ru.plumsoftware.squid.game.adapter.BackAdapter;
import ru.plumsoftware.squid.game.data.Data;
import ru.plumsoftware.squid.game.dialogs.CustomProgressDialog;
import ru.plumsoftware.squid.game.heroes.Back;
import ru.plumsoftware.squid.game.heroes.Backs;

public class BackgroundShopActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_background_shop);

//        region::Base variables
        Context context = BackgroundShopActivity.this;
        Activity activity = BackgroundShopActivity.this;

        MobileAds.initialize(this, () -> {

        });

        mInterstitialAdLoader = new InterstitialAdLoader(BackgroundShopActivity.this);
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
                            startActivity(new Intent(BackgroundShopActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onAdClicked() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(new Intent(BackgroundShopActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onAdImpression(@Nullable ImpressionData impressionData) {

                        }
                    });
                }
                assert mInterstitialAd != null;
                mInterstitialAd.show(BackgroundShopActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                progressDialog.dismissProgressDialog();
                finish();
                overridePendingTransition(0, 0);
                startActivity(new Intent(BackgroundShopActivity.this, MainActivity.class));
            }
        });
        progressDialog = new CustomProgressDialog(BackgroundShopActivity.this);
        progressDialog.setMessage("Загрузка...");
//        endregion

//        region::Find views
        TextView textViewScore2 = (TextView) activity.findViewById(R.id.textViewScore2);
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerViewHeroes);
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
        List<Back> list = Backs.buildHeroes();
        for (int i = 0; i < list.size(); i++) {
            Back back = list.get(i);
            back.setBuy(sharedPreferences.getBoolean(Data.SP_BACK_IS_BUY[i], false));
            list.set(i, back);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new BackAdapter(context, list));
//        endregion
    }

    @Override
    public void onBackPressed() {
        progressDialog.showProgressDialog();
        sharedPreferences.edit().putBoolean("isShowAppOpen", false).apply();
        if (mInterstitialAdLoader != null) {
            final AdRequestConfiguration adRequestConfiguration =
                    new AdRequestConfiguration.Builder("R-M-13696131-1").build();
            mInterstitialAdLoader.loadAd(adRequestConfiguration);
        }
    }
}