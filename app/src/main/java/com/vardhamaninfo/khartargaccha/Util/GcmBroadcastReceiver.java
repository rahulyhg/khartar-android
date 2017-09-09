package com.vardhamaninfo.khartargaccha.Util;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.vardhamaninfo.khartargaccha.Activity.Navigation_Drawer_Activity;
import com.vardhamaninfo.khartargaccha.R;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	String title, message;

	static String string;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("GCM Receiver", "onReceive");

		string = "DEFAULT_SOUND";
		// Explicitly specify that GcmIntentService will handle the intent.
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				message = extras.getString("message");
				Log.d("Notification recieved",message == null? "":message);
				title = extras.getString("title");

			}
		}
		if (message != null) {

			postNotificationMessage(
					new Intent(context, Navigation_Drawer_Activity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
					message.trim(), title, context);
		}

	}

	protected static void postNotificationMessage(Intent intentAction,
			String message, String title, Context context) {
		int count = 1;
		final NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);


		final PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intentAction,PendingIntent.FLAG_UPDATE_CURRENT);
		final Notification notification = new NotificationCompat.Builder(
				context).setSmallIcon(R.mipmap.logo)
				.setContentTitle(title).setContentText(message)
				.setContentIntent(pendingIntent)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
				.setAutoCancel(true).getNotification();
		mNotificationManager.notify(
				(int) Calendar.getInstance(TimeZone.getDefault())
						.getTimeInMillis(), notification);
		count++;
	}
}