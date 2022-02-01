package com.biniyamshopping.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Where extends AppCompatActivity {
    RadioButton adis, adama, bahir, dire, hawasa, gondar, mekele;
    Button select;
    String city = "";
    String str = "";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.where);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MobileAds.initialize(this, "ca-app-pub-6002206915132015~3669917621");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adis = (RadioButton) findViewById(R.id.addis);
        select = (Button) findViewById(R.id.select);
        adama = (RadioButton) findViewById(R.id.adama);
        bahir = (RadioButton) findViewById(R.id.bahir);
        dire = (RadioButton) findViewById(R.id.dire);
        hawasa = (RadioButton) findViewById(R.id.hawasa);
        gondar = (RadioButton) findViewById(R.id.gondar);
        mekele = (RadioButton) findViewById(R.id.mekele);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Where.this, Catagories.class);
                intent.putExtra("city", str);
                startActivity(intent);

            }
        });
    }

    public void radioaction(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.addis:
                if (checked)
                    str = "Addis Ababa";
                break;
            case R.id.adama:
                if (checked)
                    str = "Adama";
                break;
            case R.id.bahir:
                if (checked)
                    str = "Bahir Dar";
                break;
            case R.id.dire:
                if (checked)
                    str = "Dire Dawa";
                break;
            case R.id.hawasa:
                if (checked)
                    str = "Awassa";
                break;
            case R.id.gondar:
                if (checked)
                    str = "Gondar";
                break;
            case R.id.mekele:
                if (checked)
                    str = "Mekele";
                break;
        }
    }
}
