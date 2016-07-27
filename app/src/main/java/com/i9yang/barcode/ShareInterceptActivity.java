package com.i9yang.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShareInterceptActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		if (intent != null) {
			final String action = intent.getAction();
			String text = null;
			switch (action) {
				case Intent.ACTION_PROCESS_TEXT:
					text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
					break;
			}

			String uri = "http://www.oxforddictionaries.com/definition/english/";
			uri += text;

			WebView webView = new WebView(this);
			webView.setWebViewClient(new WebViewClient());
			webView.loadUrl(uri);
			setContentView(webView);
		}
	}

}
