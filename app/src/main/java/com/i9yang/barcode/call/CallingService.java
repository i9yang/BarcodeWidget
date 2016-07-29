package com.i9yang.barcode.call;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CallingService extends Service {
	public static final String EXTRA_CALL_NUMBER = "call_number";

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SendPush sendPush = new SendPush();
		sendPush.execute(intent.getStringExtra(EXTRA_CALL_NUMBER));
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
