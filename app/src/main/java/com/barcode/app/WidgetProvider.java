package com.barcode.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.RemoteViews;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class WidgetProvider extends AppWidgetProvider {
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));

		for (int i = 0; i < appWidgetIds.length; i++) {
			updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
		}
	}

	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		try {
			Bitmap bitmap = encodeAsBitmap("7578117104576120", BarcodeFormat.CODE_128, 600, 300);
			RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.widget_activity);
			v.setImageViewBitmap(R.id.barcode, bitmap);
			appWidgetManager.updateAppWidget(appWidgetId, v);
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

	private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws Exception {
		BitMatrix result = new MultiFormatWriter().encode(contents, format, img_width, img_height);
		Bitmap bitmap = Bitmap.createBitmap(img_width, img_height, Bitmap.Config.ARGB_8888);
		for (int i = 0; i < img_width; i++) {
			for (int j = 0; j < img_height; j++) {
				bitmap.setPixel(i, j, result.get(i, j) ? Color.BLACK : Color.WHITE);
			}
		}

		return bitmap;
	}
}
