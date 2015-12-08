package com.i9yang.barcode.exam;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.i9yang.barcode.FragmentActivity;
import com.i9yang.barcode.NotificationView;
import com.i9yang.barcode.R;

public class PM_Fragment extends Fragment {
	private NotificationManager mNotificationManager;
	private int notificationID = 100;
	private int numMessages = 0;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = container.getContext();
		View view = inflater.inflate(R.layout.pm_fragment, container, false);
		Button startBtn = (Button) view.findViewById(R.id.start);
		startBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				displayNotification();
			}
		});
		Button cancelBtn = (Button) view.findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				cancelNotification();
			}
		});

		Button updateBtn = (Button) view.findViewById(R.id.update);
		updateBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				updateNotification();
			}
		});
		return view;
	}


	protected void displayNotification() {
		displayNotification("New Message", "You've got new message.", "New Message Alert!");
	}

	protected void cancelNotification() {
		mNotificationManager.cancel(notificationID);
	}

	protected void updateNotification() {
		displayNotification("Updated Message", "You've got updated message.", "Updated Message Alert!");
	}

	public void displayNotification(String title, String text, String ticker) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(text);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setNumber(++numMessages);

		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		String[] events = new String[6];
		events[0] = new String("This is first line....");
		events[1] = new String("This is second line...");
		events[2] = new String("This is third line...");
		events[3] = new String("This is 4th line...");
		events[4] = new String("This is 5th line...");
		events[5] = new String("This is 6th line...");
		inboxStyle.setBigContentTitle("Big Title Details:");
		for (int i = 0; i < events.length; i++) {
			inboxStyle.addLine(events[i]);
		}

		Intent resultIntent = new Intent(context, FragmentActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(FragmentActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notificationID, mBuilder.build());
	}
}

