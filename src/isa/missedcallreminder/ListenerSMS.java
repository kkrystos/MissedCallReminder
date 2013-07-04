package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class ListenerSMS extends BroadcastReceiver {
	private String smsNumber;
	private String smsName;
	private String smsBody;
	private String smsNumberSub;
	private static String[] FROM = { NAZWA, NUMER, ID, _ID, };
	private String numerDB;
	private AlarmManager am;
	private String values;
	private String intervals;
	private int ivalues;
	private int iintervals;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Cursor kursor = null;

		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		values = getPrefs.getString("listDuration", "500");
		ivalues = Integer.parseInt(values);
		intervals = getPrefs.getString("listIntervals", "10000");
		iintervals = Integer.parseInt(intervals);

		if (intent != null
				&& intent.getAction() != null
				&& "android.provider.Telephony.SMS_Received"
						.compareToIgnoreCase(intent.getAction()) == 0) {
			Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pduArray.length];
			for (int i = 0; i < pduArray.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
				smsNumber = messages[i].getOriginatingAddress();
				smsBody = messages[i].getMessageBody();
			}
			if (smsNumber.length() >= 9) {
				smsNumberSub = smsNumber.substring(smsNumber.length() - 9);
			}

			kursor = context.getContentResolver().query(TRESC_URI, FROM,
					NUMER + "='" + smsNumberSub + "'", null, null);
			pokazZdarzenia(kursor);

			if (numerDB != null) {
				if (numerDB.length() != 0) {
					Intent i = new Intent(context,
							NotificationSmsActivity.class);
					i.putExtra("smsNumberSub", smsNumberSub);
					i.putExtra("smsName", smsName);
					i.putExtra("smsBody", smsBody);
					PendingIntent pi = PendingIntent.getActivity(context, 0, i,
							PendingIntent.FLAG_CANCEL_CURRENT);
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + 1000, iintervals, pi);
					Toast.makeText(
							context,
							"SMS: " + "from: \n" + smsNumberSub + "\n"
									+ smsName + "\n" + smsBody,
							Toast.LENGTH_SHORT).show();
					numerDB = "";
				}
			}

		}
	}

	public void pokazZdarzenia(Cursor kursor) {
		while (kursor.moveToNext()) {
			smsName = kursor.getString(0);
			numerDB = kursor.getString(1);
		}
	}
}
