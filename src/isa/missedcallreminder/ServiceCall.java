package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.CallLog.Calls;
import android.widget.CheckBox;
import android.widget.Toast;

public class ServiceCall extends Service {
	private String callNumber;
	private Toast myToast;
	private String lastCallnumber;
	private String lastName;
	private String numerDB;
	private String id;
	private static String[] FROM = { NAZWA, NUMER, ID, _ID, };
	private MissedCallsContentObserver mcco;
	private String values;
	private String intervals;
	private int ivalues;
	private int iintervals;
	NotificationManager nm;
	private WakeLock wl;
	private AlarmManager am;
	private Intent i;
	private PendingIntent pi;
	boolean hasCheckPref = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mcco = new MissedCallsContentObserver(new Handler());
		getApplicationContext().getContentResolver().registerContentObserver(
				Calls.CONTENT_URI, true, mcco);
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	@Override
	public void onDestroy() {
		getApplicationContext().getContentResolver().unregisterContentObserver(
				mcco);
		super.onDestroy();

	}

	class MissedCallsContentObserver extends ContentObserver {
		public MissedCallsContentObserver(Handler h) {
			super(h);
		}

		@Override
		public void onChange(boolean selfChange) {
			boolean bool = false;

			SharedPreferences getPrefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			values = getPrefs.getString("listDuration", "500");
			ivalues = Integer.parseInt(values);
			intervals = getPrefs.getString("listIntervals", "10000");
			iintervals = Integer.parseInt(intervals);
			hasCheckPref = getPrefs.getBoolean("filtered_checkbox", true);

			final String[] projection = null;
			final String selection = null;
			final String[] selectionArgs = null;
			final String sortOrder = android.provider.CallLog.Calls.DATE
					+ " DESC";
			Cursor cursor = null;
			Cursor cur = null;
			Cursor kursor = null;
			try {
				cursor = getContentResolver().query(
						Uri.parse("content://call_log/calls"), projection,
						selection, selectionArgs, sortOrder);
				String[] projectionn = new String[] { Calls.NUMBER,
						Calls.CACHED_NAME };
				cur = getContentResolver().query(Calls.CONTENT_URI,
						projectionn, null, null, Calls.DATE + " desc");
				cur.moveToFirst();
				lastCallnumber = cur.getString(0);
				lastName = cur.getString(1);

				if (!hasCheckPref) {
					kursor = getContentResolver().query(TRESC_URI,FROM,NUMER+ "='"+ lastCallnumber.substring(lastCallnumber
							.length() - 9) + "'", null, null);
					pokazZdarzenia(kursor);
				}

				
				cursor.moveToFirst();
				String callLogID = cursor.getString(cursor
						.getColumnIndex(android.provider.CallLog.Calls._ID));
				callNumber = cursor.getString(cursor
						.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
				String callDate = cursor.getString(cursor
						.getColumnIndex(android.provider.CallLog.Calls.DATE));
				String callType = cursor.getString(cursor
						.getColumnIndex(android.provider.CallLog.Calls.TYPE));
				String isCallNew = cursor.getString(cursor
						.getColumnIndex(android.provider.CallLog.Calls.NEW));
				if (Integer.parseInt(callType) == android.provider.CallLog.Calls.MISSED_TYPE
						&& Integer.parseInt(isCallNew) > 0) {
					bool = true;
				}

			} catch (Exception ex) {
			} finally {
				if (cursor != null) {
					cursor.close();
				}
				if (cur != null) {
					cur.close();
				}
				if (kursor != null) {
					kursor.close();
				}

			}

			if (bool == false) {
				am.cancel(pi);
			}

			if (!hasCheckPref && bool == true) {
				if (numerDB != null && numerDB.length() != 0) {
						i = new Intent(getApplicationContext(),
								NotificationsActivity.class);
						i.putExtra("lastCallnumber", lastCallnumber);
						i.putExtra("lastName", lastName);
						pi = PendingIntent.getActivity(getApplicationContext(), 0,
								i, PendingIntent.FLAG_CANCEL_CURRENT);
						Toast.makeText(getApplicationContext(),
								"Nieodebrano : " + lastCallnumber+"\n"+
						"!hasCheckPref",
								Toast.LENGTH_SHORT).show();
						am.setRepeating(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis() + 1000, iintervals, pi);
						bool = false;
						numerDB = "";
				}
			}else if (hasCheckPref && bool == true) {
				i = new Intent(getApplicationContext(),
						NotificationsActivity.class);
				i.putExtra("lastCallnumber", lastCallnumber);
				i.putExtra("lastName", lastName);
				pi = PendingIntent.getActivity(getApplicationContext(), 0,
						i, PendingIntent.FLAG_CANCEL_CURRENT);
				Toast.makeText(getApplicationContext(),
						"Nieodebrano : " + lastCallnumber+"\n"+
				"hasCheckPref",
						Toast.LENGTH_SHORT).show();
				am.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis() + 1000, iintervals, pi);
				bool = false;
			}
		}
	}

	public void pokazZdarzenia(Cursor kursor) {
		StringBuilder konstruktor = new StringBuilder("Zapisane zdarzenia:\n");
		while (kursor.moveToNext()) {
			long idd = kursor.getLong(0);
			numerDB = kursor.getString(1);
			id = konstruktor.append(idd).toString();
		}
	}

}