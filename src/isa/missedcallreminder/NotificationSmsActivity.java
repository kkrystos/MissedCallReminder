package isa.missedcallreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WAKE LOCK");
		wl.acquire();
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.notification);

		values = getPrefs.getString("listDuration", "500");
		ivalues = Integer.parseInt(values);
		intervals = getPrefs.getString("listIntervals", "10000");
		iintervals = Integer.parseInt(intervals);
		maxtime = getPrefs.getString("listMaxtime", "0");
		imaxtime = Integer.parseInt(maxtime);

		led = getPrefs.getString("led_pref", "0");
		iled = Integer.parseInt(led);

		strRingtonePreference = getPrefs.getString("ring_tone_pref",
				"DEFAULT_SOUND");
		isVibrate = getPrefs.getBoolean("check_vibrate_pref", true);
		isScreenOn = getPrefs.getBoolean("check_screen_on_pref", false);

		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		n = new Notification(R.drawable.ic_launcher, "Missed Call Reminder",
				System.currentTimeMillis());
		Intent i = new Intent(this, MainPreferenceActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		n.setLatestEventInfo(this, "Missed Call Reminder", "MCR", pi);

		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		ii = new Intent(this, NotificationSmsActivity.class);
		pii = PendingIntent.getActivity(this, 0, ii, 0);

		if (isVibrate == true) {
			n.vibrate = new long[] { 0, ivalues };
		}

		if (iled == 0) {
			n.ledARGB = Color.WHITE;
		} else if (iled == 1) {
			n.ledARGB = Color.GREEN;
		} else if (iled == 2) {
			n.ledARGB = Color.RED;
		} else if (iled == 3) {
			n.ledARGB = Color.BLUE;
		}

		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.ledOnMS = 1000;
		n.ledOffMS = 1000;

		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.audioStreamType = AudioManager.STREAM_NOTIFICATION;
		n.sound = Uri.parse(strRingtonePreference);
		nm.notify(1, n);

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