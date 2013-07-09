package isa.missedcallreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.Notification.InboxStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NotificationsActivity extends Activity {

	NotificationManager nm;
	private String values;
	private String intervals;
	private int ivalues;
	private int iintervals;
	private String maxtime;
	private int imaxtime;
	private String led;
	private int iled;
	private WakeLock wl;
	private String strRingtonePreference;
	private Notification n;
	boolean isVibrate;
	boolean isScreenOn;
	private AlarmManager am;
	private Intent ii;
	private PendingIntent pii;
	private String lastCallnumber;
	private String lastName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// SharedPreferences
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		values = getPrefs.getString("listDuration", "500");
		ivalues = Integer.parseInt(values);
		intervals = getPrefs.getString("listIntervals", "10000");
		iintervals = Integer.parseInt(intervals);
		maxtime = getPrefs.getString("listMaxtime", "0");
		imaxtime = Integer.parseInt(maxtime);
		led = getPrefs.getString("led_preff", "0");
		iled = Integer.parseInt(led);
		strRingtonePreference = getPrefs.getString("ring_tone_preff",
				"DEFAULT_SOUND");
		isVibrate = getPrefs.getBoolean("check_vibrate_preff", true);
		isScreenOn = getPrefs.getBoolean("check_screen_on_preff", false);

		Log.i("sound", strRingtonePreference);

		if (isScreenOn) {
			((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
					PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG").acquire();
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN
							| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
					WindowManager.LayoutParams.FLAG_FULLSCREEN
							| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		}
		
		setContentView(R.layout.notification);
		
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Bundle bundle = getIntent().getExtras();
		lastCallnumber = bundle.getString("lastCallnumber");
		lastName = bundle.getString("lastName");

		// Own alarmManager off
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		ii = new Intent(this, NotificationsActivity.class);
		pii = PendingIntent.getActivity(this, 0, ii, 0);

		// Intents and PendingsIntents
		// Start this.activity
		Intent hideIntent = new Intent(getApplicationContext(),
				HideNotification.class);
		hideIntent.putExtra("isCall", false);
		hideIntent.putExtra("isSMS", false);
		hideIntent.putExtra("lastCallnumber", "numer");
		PendingIntent hidePendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, hideIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// call
		Intent callIntent = new Intent(this, HideNotification.class);
		callIntent.putExtra("isCall", true);
		callIntent.putExtra("isSMS", false);
		callIntent.putExtra("lastCallnumber", lastCallnumber);
		PendingIntent pendingIntentCall = PendingIntent.getActivity(
				getApplicationContext(), 1, callIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// sms
		Intent smsIntent = new Intent(this, HideNotification.class);
		smsIntent.putExtra("isSMS", true);
		smsIntent.putExtra("isCall", false);
		smsIntent.putExtra("lastCallnumber", lastCallnumber);
		PendingIntent pendingIntentSMS = PendingIntent.getActivity(
				getApplicationContext(), 2, smsIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// check version of android and do stuff
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//			Notification.Builder noti = new Notification.Builder(
//					getApplicationContext())
//					.setLargeIcon(
//							BitmapFactory.decodeResource(getResources(),
//									R.drawable.ic_launcher))
//					.setSmallIcon(R.drawable.ic_launcher)
//					.setTicker("Nieodebrane Po³¹czenie " +lastName)
//					.setWhen(System.currentTimeMillis())
//					.setContentTitle("Nieodebrane Po³¹czenie")
//					.setContentText(lastName + " " + lastCallnumber)
//					.setContentIntent(hidePendingIntent)
//					.addAction(android.R.drawable.sym_action_call, "Call",
//							pendingIntentCall)
//					.addAction(android.R.drawable.sym_action_email, "SMS",
//							pendingIntentSMS).setAutoCancel(true);
//			if (isVibrate) {
//				noti.setVibrate(new long[] { 0, ivalues });
//			}
//			if (strRingtonePreference.equalsIgnoreCase("DEFAULT_SOUND")) {
//				noti.setSound(Uri
//						.parse("content://settings/system/notification_sound"),
//						AudioManager.STREAM_NOTIFICATION);
//			} else {
//				noti.setSound(Uri.parse(strRingtonePreference),
//						AudioManager.STREAM_NOTIFICATION);
//			}
//			nm.notify(1, noti.build());
			
			
			// Pending intent to be fired when notification is clicked
			

						RemoteViews remoteViews = new RemoteViews(getPackageName(),
								R.layout.notification_layout);
						Notification.Builder builder = new Notification.Builder(getApplicationContext());
						builder.setSmallIcon(R.drawable.ic_launcher);
						
						remoteViews.setOnClickPendingIntent(R.id.noti_callBtn, pendingIntentCall);
						remoteViews.setOnClickPendingIntent(R.id.noti_callTv, pendingIntentCall);
						remoteViews.setOnClickPendingIntent(R.id.noti_smsBtn, pendingIntentSMS);
						remoteViews.setOnClickPendingIntent(R.id.noti_smsTv, pendingIntentSMS);
						remoteViews.setTextViewText(R.id.notification_main_txt, "Nieodebrane Po³¹czenie " +lastName);
						builder.setContent(remoteViews);
						builder.setWhen(System.currentTimeMillis());
						builder.setTicker("Nieodebrane Po³¹czenie " +lastName);
						builder.setContentIntent(hidePendingIntent);
						builder.setAutoCancel(true);
						builder.setStyle(new Notification.BigPictureStyle());
						NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManger.notify(1, builder.build());
					

		} else {
//			NotificationCompat.Builder noti = new NotificationCompat.Builder(
//					getApplicationContext())
//					.setLargeIcon(
//							BitmapFactory.decodeResource(getResources(),
//									R.drawable.ic_launcher))
//					.setSmallIcon(R.drawable.ic_launcher)
//					.setTicker("Nieodebrane Po³¹czenie " +lastName)
//					.setWhen(System.currentTimeMillis())
//					.setContentTitle("Nieodebrane Po³¹czenie")
//					.setContentText(lastName + " " + lastCallnumber)
//					.setContentIntent(hidePendingIntent)
//					.addAction(android.R.drawable.sym_action_call, "Call",
//							pendingIntentCall)
//					.addAction(android.R.drawable.sym_action_email, "SMS",
//							pendingIntentSMS).setAutoCancel(true);
//			if (isVibrate) {
//				noti.setVibrate(new long[] { 0, ivalues });
//			}
//			if (strRingtonePreference.equalsIgnoreCase("DEFAULT_SOUND")) {
//				noti.setSound(Uri
//						.parse("content://settings/system/notification_sound"),
//						AudioManager.STREAM_NOTIFICATION);
//			} else {
//				noti.setSound(Uri.parse(strRingtonePreference),
//						AudioManager.STREAM_NOTIFICATION);
//			}
//			nm.notify(1, noti.build());
			
			Intent intent = new Intent(this, HideNotification.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 01,
					intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
			RemoteViews remoteViews = new RemoteViews(getPackageName(),
					R.layout.notification_layout);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setContent(remoteViews);
			builder.setTicker("Notification by Chitranshu Asthana (Remote View)");
			builder.setContentIntent(pendingIntent);
			builder.setAutoCancel(true);
			builder.setPriority(0);
			Notification notification = builder.build();
			NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManger.notify(02, notification);
		}
		if (isScreenOn == true) {
			new Handler().postDelayed(new Runnable() {
				public void run() {

//					finish();
				}
			}, 10);
		} else if (isScreenOn == false) {
//			finish();
		}

		if (imaxtime != 0) {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				public void run() {
					am.cancel(pii);
				}
			}, imaxtime);
		}

	}

	private void RedFlashLight() {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notif = new Notification();
		notif.defaults |= Notification.DEFAULT_LIGHTS;
		notif.ledARGB = Color.BLUE;
		notif.flags = Notification.FLAG_SHOW_LIGHTS;
		notif.ledOnMS = 1000;
		notif.ledOffMS = 1000;
		nm.notify(7682, notif);
	}
}