package com.doors.myplacetourist;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.doors.myplacetourist.common.Values;

import androidx.appcompat.app.AppCompatActivity;

public class WebBrowserActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };

        String url = getIntent().getStringExtra(Values.TRIP_URL);
        WebView webView = findViewById(R.id.webBrowserLayout_webView);
        TextView editText = findViewById(R.id.webBrowserLayout_URL);
        Uri uri = Uri.parse(url);
        editText.setText(uri.getAuthority());
        findViewById(R.id.webBrowserLayout_backBtn).setOnClickListener(v->finish());
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().supportZoom();
        webView.loadUrl(url);
        setResult(RESULT_OK);
    }
}
