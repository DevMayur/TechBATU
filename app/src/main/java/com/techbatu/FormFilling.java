package com.techbatu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FormFilling extends AppCompatActivity {

    private WebView wv_formfilling;
    private String url="https://formfilling.dbatuapps.in/landing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_filling);

        wv_formfilling = findViewById(R.id.wv_formfilling);
        wv_formfilling.setWebViewClient(new WebViewClient());
        wv_formfilling.getSettings().setJavaScriptEnabled(true);
        wv_formfilling.loadUrl(url);

    }
}
