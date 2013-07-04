package isa.missedcallreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class HideNotification extends Activity {

	private AlarmManager am;
	private Intent i;
	private Intent ii;
	private PendingIntent pi;
	private PendingIntent pii;
	private String lastCallnumber;
	private boolean isCall = false;
	private boolean isSMS = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		lastCallnumber = bundle.getString("lastCallnumber");

		isCall = bundle.getBoolean("isCall");
		isSMS = bundle.getBoolean("isSMS");

		Toast.makeText(
				getApplicationContext(),
				"isCall " + isCall + "\nisSMS " + isSMS + "\nnr " + lastCallnumber,
				0).show();

		if (isCall) {
			reCall();
		}
		if (isSMS) {
			reSMS();
		}

		hideAlarm();
		hideNotification();
		finish();
	}

	public void reCall() {

		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + lastCallnumber));
		startActivity(callIntent);
	}

	public void reSMS() {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("sms:" + lastCallnumber));
		startActivity(smsIntent);
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
		nm.cancel(1);
	}

}
