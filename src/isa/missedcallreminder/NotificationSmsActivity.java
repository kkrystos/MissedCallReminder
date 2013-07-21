package isa.missedcallreminder;

import static isa.missedcallreminder.db.Const.NOTIFICATION_SMS_ID;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

public class NotificationSmsActivity extends Activity {

	NotificationManager nm;
	private String values;
	private String intervals;
	private int ivalues;
	private int iintervals;
	private String maxtime;
	private String led;
	private int iled;
	private int imaxtime;
	private WakeLock wl;
	private String strRingtonePreference;
	private Notification n;
	boolean isVibrate;
	boolean isScreenOn;
	private AlarmManager am;
	private Intent ii;
	private PendingIntent pii;
	private String smsNumberSub;
	private String smsName;
	private String smsBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		values = getPrefs.getString("listDuration", "500");
		ivalues = Integer.parseInt(values);
		intervals = getPrefs.getString("listIntervals", "10000");
		iintervals = Integer.parseInt(intervals);
		maxtime = getPrefs.getString("listMaxtime", "0");
		imaxtime = Integer.parseInt(maxtime);
		led = getPrefs.getString("led_pref", "0");
		iled = Integer.parseInt(led);
		strRingtonePreference = getPrefs.getString("ring_tone_pref","DEFAULT_SOUND");
		isVibrate = getPrefs.getBoolean("check_vibrate_pref", true);
		isScreenOn = getPrefs.getBoolean("check_screen_on_pref", false);
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

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Bundle bundle = getIntent().getExtras();
		smsNumberSub = bundle.getString("smsNumberSub");
		smsName = bundle.getString("smsName");
		smsBody = bundle.getString("smsBody");

		// Own alarmManager off
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		ii = new Intent(this, NotificationSmsActivity.class);
		pii = PendingIntent.getActivity(this, 0, ii, 0);

		// Intents and PendingsIntents
		// Start this.activity
		Intent hideIntent = new Intent(getApplicationContext(), HideNotification.class);
//		hideIntent.putExtra("isCall", false);
//		hideIntent.putExtra("isSMS", false);
//		hideIntent.putExtra("lastCallnumber", "numer");
		PendingIntent hidePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, hideIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		// call
//		Intent callIntent = new Intent(this, HideNotification.class);
//		callIntent.putExtra("isCall", true);
//		callIntent.putExtra("isSMS", false);
//		callIntent.putExtra("lastCallnumber", smsNumberSub);
//		PendingIntent pendingIntentCall = PendingIntent.getActivity(
//				getApplicationContext(), 1, callIntent,
//				PendingIntent.FLAG_UPDATE_CURRENT);
		// sms
//		Intent smsIntent = new Intent(this, HideNotification.class);
//		smsIntent.putExtra("isSMS", true);
//		smsIntent.putExtra("isCall", false);
//		smsIntent.putExtra("lastCallnumber", smsNumberSub);
//		PendingIntent pendingIntentSMS = PendingIntent.getActivity(
//				getApplicationContext(), 2, smsIntent,
//				PendingIntent.FLAG_UPDATE_CURRENT);

		// check version of android and do stuff
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//			Notification.Builder noti = new Notification.Builder(
//					getApplicationContext())
//					.setLargeIcon(
//							BitmapFactory.decodeResource(getResources(),
//									R.drawable.ic_launcher))
//					.setSmallIcon(R.drawable.ic_launcher)
//					.setTicker("Nieodczytany SMS " + smsName)
//					.setWhen(System.currentTimeMillis())
//					.setContentTitle("Nieodczytany SMS")
//					.setContentText(smsName + " " + "\n " + smsBody)
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
//
//			nm.notify(NOTIFICATION_SMS_ID, noti.build());
//		} else {
			NotificationCompat.Builder noti = new NotificationCompat.Builder(getApplicationContext())
					.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_new))
					.setSmallIcon(R.drawable.ic_launcher_new)
					.setTicker("Nieodczytany SMS " + smsName)
					.setWhen(System.currentTimeMillis())
					.setContentTitle("Nieodczytany SMS")
					.setContentText(smsName + " " + "\n " + smsBody)
					.setContentIntent(hidePendingIntent)
					.setAutoCancel(true);
			if (isVibrate) {
				noti.setVibrate(new long[] { 0, ivalues });
			}
			if (strRingtonePreference.equalsIgnoreCase("DEFAULT_SOUND")) {
				noti.setSound(Uri.parse("content://settings/system/notification_sound"),AudioManager.STREAM_NOTIFICATION);
			} else {
				noti.setSound(Uri.parse(strRingtonePreference),AudioManager.STREAM_NOTIFICATION);
			}

			nm.notify(NOTIFICATION_SMS_ID, noti.build());
//		}

		if (isScreenOn == true) {
			new Handler().postDelayed(new Runnable() {
				public void run() {

					finish();
				}
			}, 10);
		} else if (isScreenOn == false) {
			finish();
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
}