package com.i9yang.barcode;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.google.zxing.BarcodeFormat;
import com.i9yang.barcode.util.BarcodeUtil;

public class WidgetProvider extends AppWidgetProvider {
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i = 0; i < appWidgetIds.length; i++) {
			updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
		}
	}

	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		try {
			SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			String barcodeNo = sharedPref.getString(context.getString(R.string.barcodeNo), "");

			if (! TextUtils.isEmpty(barcodeNo)) {
				Bitmap bitmap = BarcodeUtil.encodeAsBitmap(barcodeNo, BarcodeFormat.CODE_128, 700, 300);
				RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.widget_activity);
				v.setImageViewBitmap(R.id.barcode, bitmap);
				appWidgetManager.updateAppWidget(appWidgetId, v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}


}
