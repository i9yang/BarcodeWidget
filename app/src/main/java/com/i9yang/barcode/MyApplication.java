package com.i9yang.barcode;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

public class MyApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		update.setClass(this, WidgetProvider.class);
		update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(this, WidgetProvider.class)));
		this.sendBroadcast(update);
	}
}
