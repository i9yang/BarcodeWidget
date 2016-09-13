package com.i9yang.barcode.exam;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.i9yang.barcode.MainActivity;
import com.i9yang.barcode.R;
import com.i9yang.barcode.weather.Weather;
import io.realm.Realm;
import io.realm.RealmResults;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

	    HashMap<String, String> msgMap = new HashMap<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
		Date date = new Date();
		String strDate = dateFormat.format(date);
	    msgMap.put("date", strDate);

	    Gson gson = new Gson();

        if (from.startsWith("/topics/weather")) {
	        Type type = new TypeToken<Map<String, String>>(){}.getType();
		    Map<String, String> msg = gson.fromJson(message, type);
            msgMap.put("title",strDate + " 의 날씨");
	        msgMap.put("text", msg.get("text") + " ( " + msg.get("temp")+ " )" );

	        Realm realm = Realm.getInstance(this);
	        realm.beginTransaction();
	        DateTime dateTime = new DateTime(date);
	        dateTime = dateTime.minusDays(7);
	        RealmResults<Weather> result = realm.where(Weather.class).lessThan("date", dateTime.toDate()).findAll();
	        result.clear();
	        realm.commitTransaction();

	        Long seq = (Long) realm.where(Weather.class).max("seq");

	        seq = seq == null ? 0 : seq;
	        realm.beginTransaction();
	        Weather weather = realm.createObject(Weather.class);
	        weather.setSeq(seq+1);
	        weather.setDate(date);
	        weather.setText(msg.get("text"));
	        weather.setTemperature(msg.get("temp"));
	        realm.commitTransaction();

	        sendNotification(msgMap);
        } else if (from.startsWith("/topics/torrent")) {
	        Type type = new TypeToken<Map<String, String>>(){}.getType();
	        Map<String, String> msg = gson.fromJson(message, type);

            msgMap.put("title", strDate + " : " + msg.get("title"));
	        msgMap.put("text", msg.get("text"));

	        sendNotification(msgMap);
        } else if (from.startsWith("/topics/bus")) {
	        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
	        List<Map<String, String>> msg = gson.fromJson(message, type);

	        for(Map<String,String> map : msg) {
	            msgMap.put("title",map.get("rtNm"));
	            msgMap.put("text", map.get("arrmsg1") + ", " + map.get("arrmsg2"));

		        sendNotification(msgMap);
	        }

        }
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
		        .setStyle(new NotificationCompat.BigTextStyle().bigText(msgMap.get("text")))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	    Random random = new Random();
	    int m = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(m, notificationBuilder.build());
    }
}

