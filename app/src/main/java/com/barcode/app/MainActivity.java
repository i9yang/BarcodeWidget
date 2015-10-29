package com.barcode.app;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.barcode.app.util.BarcodeUtil;
import com.google.zxing.BarcodeFormat;
import org.apache.commons.lang3.StringUtils;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		TextView tv = (TextView) findViewById(R.id.barcodeNoText);
		ImageView iv = (ImageView) findViewById(R.id.barcodeImage);
		String barcodeNo = sharedPref.getString(getString(R.string.barcodeNo), "");

		try {
			if (StringUtils.isNotEmpty(barcodeNo)) {
				tv.setText(barcodeNo);
				Bitmap bitmap = BarcodeUtil.encodeAsBitmap(barcodeNo, BarcodeFormat.CODE_128, 700, 300);
				iv.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveBarcodeNo(View v) {
		EditText et = (EditText) findViewById(R.id.barcodeNo);
		if (StringUtils.isNotEmpty(et.getText())) {
			SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(getString(R.string.barcodeNo), et.getText().toString());
			editor.commit();

			TextView tv = (TextView) findViewById(R.id.barcodeNoText);
			tv.setText(et.getText().toString());

			Toast toast = Toast.makeText(getApplicationContext(), "Changed!", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			AppWidgetManager mgr = AppWidgetManager.getInstance(this);
			Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			update.setClass(this, WidgetProvider.class);
			update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(this, WidgetProvider.class)));
			this.sendBroadcast(update);
		}
	}

	public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
}
