package com.i9yang.barcode.exam;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.i9yang.barcode.MainActivity;
import com.i9yang.barcode.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

	    HashMap<String, String> msgMap = new HashMap<String, String>();
		SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
		Date date = new Date();
		String strDate = dateFormat.format(date);
	    msgMap.put("date", strDate);

	    Gson gson = new Gson();
	    Type type = new TypeToken<Map<String, String>>(){}.getType();
	    Map<String, String> msg = gson.fromJson(message, type);

        if (from.startsWith("/topics/weather")) {
            msgMap.put("title",strDate + " 의 날씨");
	        msgMap.put("text", msg.get("text") + " ( " + msg.get("temp")+ " )" );
        } else if (from.startsWith("/topics/torrent")) {
            msgMap.put("title", strDate + " : " + msg.get("title"));
	        msgMap.put("text", msg.get("text"));
        }

	    sendNotification(msgMap);
    }

    private void sendNotification(HashMap<String, String> msgMap) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

	    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(msgMap.get("title"))
                .setContentText(msgMap.get("text"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

