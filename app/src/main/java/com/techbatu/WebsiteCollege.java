package com.techbatu;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteCollege extends AppCompatActivity {

    private WebView wv_clgsite;
    private String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_college);

        wv_clgsite = findViewById(R.id.wv_clgsite);
        url = "https://dbatu.ac.in/";
        wv_clgsite.setWebViewClient(new WebViewClient());
        wv_clgsite.getSettings().setJavaScriptEnabled(true);
        wv_clgsite.loadUrl(url);

    }
}
