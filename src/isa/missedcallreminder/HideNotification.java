package isa.missedcallreminder;

import static isa.missedcallreminder.db.Const.NOTIFICATION_CALL_ID;
import static isa.missedcallreminder.db.Const.NOTIFICATION_SMS_ID;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import isa.missedcallreminder.db.DbManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class HideNotification extends Activity {

	private AlarmManager am;
	private Intent i;
	private Intent ii;
	private PendingIntent pi;
	private PendingIntent pii;
	private DbManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbManager = new DbManager(getApplicationContext());
		dbManager.deleteTable(NAZWA_TABELI_2);
		hideAlarm();
		hideNotification();
		NotificationListActivity.activity.finish();
		finish();
	}

	public void hideAlarm() {
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		i = new Intent(this, NotificationsActivity.class);
		pi = PendingIntent.getActivity(this, 0, i, 0);
		ii = new Intent(this, NotificationSmsActivity.class);
		pii = PendingIntent.getActivity(this, 0, ii, 0);
		am.cancel(pi);
		am.cancel(pii);
	}

	public void hideNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_CALL_ID);
		nm.cancel(NOTIFICATION_SMS_ID);
	}
}
