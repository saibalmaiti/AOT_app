package com.example.aot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class aotportal extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private WebView portal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(aotportal.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        setContentView(R.layout.activity_aotportal);
        portal=(WebView) findViewById(R.id.webView);
        WebSettings webSettings=portal.getSettings();
        portal.loadUrl("http://students.aot.edu.in");
        webSettings.setJavaScriptEnabled(true);
        portal.setWebViewClient(new WebViewClient());
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        if(portal.canGoBack())
        {
            portal.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }
}
