package com.biniyamshopping.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

public class About extends AppCompatActivity {
    TextView teleg, fac, email;
    String url;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        teleg = findViewById(R.id.telg);
        fac = findViewById(R.id.fac);
        email = findViewById(R.id.email);

        teleg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(gettelegram());
            }
        });
        fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(getOpenFacebookIntent());
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendEmail();
            }
        });
    }

    public Intent gettelegram() {
        try {
            getPackageManager().getPackageInfo("org.telegram.messenger", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/gulitonlineshop"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/gulitonlineshop"));
        }
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/426253597411506"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Gulit-%E1%8C%89%E1%88%8A%E1%89%B5-Online-SHOP"));
        }
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
