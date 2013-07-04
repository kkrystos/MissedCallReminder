package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

public class ServiceSMS extends Service {
	private String smsNumber;
	private String smsName;
	private String smsBody;
	private String smsNumberSub;
	private Toast myToast;
	private String numerDB;
	private String id;
	private static String[] FROM = { NAZWA, NUMER, ID, _ID, };
//	private MissedCallsContentObserver mcco;
	private Handler mHandler;
	private int count = 0;
	private WakeLock wl;
	private AlarmManager am;
	private Intent i;
	private PendingIntent pi;
	private String values;
	private String intervals;
	private int ivalues;
	private int iintervals;
	
	private ListenerSMS smsListener;
	IntentFilter intentFilter1 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		mcco = new MissedCallsContentObserver(new Handler());
//		getApplicationContext().getContentResolver().registerContentObserver(
//				Uri.parse("content://mms-sms"), true, mcco);
//
//		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
	    smsListener = new ListenerSMS();
	    registerReceiver(smsListener, intentFilter1);
	}

	@Override
	public void onDestroy() {
//		getApplicationContext().getContentResolver().unregisterContentObserver(
//				mcco);
	    unregisterReceiver(this.smsListener);
		super.onDestroy();
	}

//	class MissedCallsContentObserver extends ContentObserver {
//		public MissedCallsContentObserver(Handler h) {
//			super(h);
//		}
//
//		@Override
//		public void onChange(boolean selfChange) {
//
//			SharedPreferences getPrefs = PreferenceManager
//					.getDefaultSharedPreferences(getBaseContext());
//			values = getPrefs.getString("listDuration", "500");
//			ivalues = Integer.parseInt(values);
//			intervals = getPrefs.getString("listIntervals", "10000");
//			iintervals = Integer.parseInt(intervals);
//
//			Cursor c = null;
//			Cursor kursor = null;
//
//			Uri sms_content = Uri.parse("content://sms/inbox");
//			c = getApplicationContext().getContentResolver().query(sms_content,
//					new String[]{"person", "address", "body"}, "read = 0", null, null);
//			count = c.getCount();
//			c.moveToFirst();
//				if (c != null) {
//
//					smsNumber = c.getString(1);
//					smsName = c.getString(0);
//					smsBody = c.getString(2);
//
//					if (smsNumber.length() >= 9) {
//						smsNumberSub = smsNumber
//								.substring(smsNumber.length() - 9);
//						kursor = getApplicationContext().getContentResolver()
//								.query(TRESC_URI, FROM,
//										NUMER + "='" + smsNumberSub + "'",
//										null, null);
//						pokazZdarzenia(kursor);
//
//						if (numerDB != null) {
//							if (numerDB.length() != 0) {
//								i = new Intent(getApplicationContext(),
//										NotificationSmsActivity.class);
//								i.putExtra("smsNumberSub", smsNumberSub);
//								i.putExtra("smsName", smsName);
//								i.putExtra("smsBody", smsBody);
//								pi = PendingIntent.getActivity(
//										getApplicationContext(), 0, i,
//										PendingIntent.FLAG_CANCEL_CURRENT);
//								am.setRepeating(AlarmManager.RTC_WAKEUP,
//										System.currentTimeMillis() + 1000,
//										iintervals, pi);
//								Toast.makeText(
//										getApplicationContext(),
//										"SMS: " + "from: \n" + smsNumberSub
//												+ "\n" + smsName + "\n"
//												+ smsBody, Toast.LENGTH_SHORT)
//										.show();
//								numerDB = "";
//							}
//						}
//					}
//				}
//			if (count == 0) {
//				am.cancel(pi);
//			}
//		}
//	}
//
//	public void pokazZdarzenia(Cursor kursor) {
//		StringBuilder konstruktor = new StringBuilder("Zapisane zdarzenia:\n");
//		while (kursor.moveToNext()) {
//			long idd = kursor.getLong(0);
//			numerDB = kursor.getString(1);
//			id = konstruktor.append(idd).toString();
//		}
//	}
}
