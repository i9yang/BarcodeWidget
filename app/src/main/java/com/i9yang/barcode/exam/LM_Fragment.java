package com.i9yang.barcode.exam;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.i9yang.barcode.R;

public class LM_Fragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lm_fragment, container, false);
		Button startBrowser_a = (Button) view.findViewById(R.id.start_browser_a);
		startBrowser_a.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://www.example.com"));
				startActivity(i);
			}
		});
		Button startBrowser_b = (Button) view.findViewById(R.id.start_browser_b);
		startBrowser_b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent("com.i9yang.barcode.LAUNCH",
					Uri.parse("http://www.example.com"));
				startActivity(i);
			}
		});
		Button startBrowser_c = (Button) view.findViewById(R.id.start_browser_c);
		startBrowser_c.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent("com.i9yang.barcode.LAUNCH",
					Uri.parse("https://www.example.com"));
				startActivity(i);
			}
		});

		return view;
	}
}
