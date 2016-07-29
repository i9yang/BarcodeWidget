package com.i9yang.barcode.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
			String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber, "KR");

			Intent serviceIntent = new Intent(context, CallingService.class);
			serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
			context.startService(serviceIntent);
		}
	}
}
