package com.i9yang.barcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.i9yang.barcode.util.BarcodeUtil;
import io.fabric.sdk.android.Fabric;

import java.util.regex.Pattern;

public class MainActivity extends Activity {
	private final int PICK_FROM_GALLERY = 999;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.main_activity);

		init();

		AdRequest adRequest = new AdRequest.Builder().addTestDevice("00e9bd128078a137").build();
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(adRequest);

		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		update.setClass(this, WidgetProvider.class);
		update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(this, WidgetProvider.class)));
		this.sendBroadcast(update);
	}

	public void saveBarcodeNo(View v) {
		EditText et = (EditText) findViewById(R.id.barcodeNo);
		if (!TextUtils.isEmpty(et.getText())) {
			changeBarcode(et.getText().toString());
		}
	}

	public void scanBarcodeNoFromCamera(View v) {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	public void scanBarcodeNoFromPhoto(View v) {
		Intent intent = new Intent();
		intent.setType("image/-");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String barcode = "";

		if (requestCode == PICK_FROM_GALLERY) {
			if (resultCode == Activity.RESULT_OK) {
				try {
					Bitmap barcodeImg = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()));
					BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
					Frame myFrame = new Frame.Builder().setBitmap(barcodeImg).build();

					SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);

					if (barcodes.size() != 0) {
						barcode = barcodes.valueAt(0).displayValue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			if (scanningResult != null) {
				barcode = scanningResult.getContents();
			}
		}

		if (!TextUtils.isEmpty(barcode)) {
			String regex = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			final String result = barcode;

			if(Pattern.matches(regex, barcode)) {
				alertDialog.setTitle(barcode + " 로 이동?");

				alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
						startActivity(i);
					}
				});

				alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				alertDialog.create();
				alertDialog.show();
			} else {
				alertDialog.setTitle("바코드 교체?");
				alertDialog.setMessage(barcode);

				alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						changeBarcode(result);
					}
				});

				alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				alertDialog.create();
				alertDialog.show();
			}

		}
	}

	public void changeBarcode(String result) {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.barcodeNo), result);
		editor.commit();

		Toast toast = Toast.makeText(getApplicationContext(), "Changed!", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		update.setClass(this, WidgetProvider.class);
		update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(this, WidgetProvider.class)));
		this.sendBroadcast(update);
		init();
	}

	public void init() {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		TextView tv = (TextView) findViewById(R.id.barcodeNoText);
		ImageView iv = (ImageView) findViewById(R.id.barcodeImage);
		String barcodeNo = sharedPref.getString(getString(R.string.barcodeNo), "");
		EditText et = (EditText) findViewById(R.id.barcodeNo);
		et.setText("");

		try {
			if (!TextUtils.isEmpty(barcodeNo)) {
				tv.setText(barcodeNo);
				Bitmap bitmap = BarcodeUtil.encodeAsBitmap(barcodeNo, BarcodeFormat.CODE_128, 700, 300);
				iv.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goExam(View v) {
		Intent intent = new Intent(this, ExamActivity.class);
		startActivity(intent);
	}

	public void goGcm(View v) {
		Intent intent = new Intent(this, GcmActivity.class);
		startActivity(intent);
	}

	public void goWeatherList(View v) {
		Intent intent = new Intent(this, WeatherActivity.class);
		startActivity(intent);
	}
}
