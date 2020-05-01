package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebPageActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(WebPageActivity.this);
        progressDialog.setMessage("Loading");
        //progressDialog.show();
        setContentView(R.layout.activity_web_page);

        web=(WebView) findViewById(R.id.webView);
        WebSettings webSettings=web.getSettings();
        web.loadUrl("http://aot.edu.in");
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

    }

    @Override
    public void onBackPressed() {
        if(web.canGoBack())
        {
            web.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }
}
