package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import isa.missedcallreminder.db.DbManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

public class ServiceCall extends Service {
	private String callNumber;
	private Toast myToast;
	private String lastCallnumber;
	private String lastName;
	private String numerDB;
	private String id;
	private static String[] FROM = { NAZWA, NUMER, ID, _ID, };
	// private MissedCallsContentObserver mcco;
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
	DbManager dbManager;

	private ListenerCall listenerCall;
	IntentFilter intentFilter1 = new IntentFilter(
			"android.intent.action.PHONE_STATE");

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// mcco = new MissedCallsContentObserver(new Handler());
		// getApplicationContext().getContentResolver().registerContentObserver(
		// Calls.CONTENT_URI, true, mcco);
		// am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		// dbManager = new DbManager(getApplicationContext());
		listenerCall = new ListenerCall();
		registerReceiver(listenerCall, intentFilter1);
	}

	@Override
	public void onDestroy() {
		// getApplicationContext().getContentResolver().unregisterContentObserver(
		// mcco);
		super.onDestroy();

	}

//	class MissedCallsContentObserver extends ContentObserver {
//		public MissedCallsContentObserver(Handler h) {
//			super(h);
//		}
//
//		@Override
//		public void onChange(boolean selfChange) {
//			boolean bool = false;
//
//			SharedPreferences getPrefs = PreferenceManager
//					.getDefaultSharedPreferences(getBaseContext());
//			values = getPrefs.getString("listDuration", "500");
//			ivalues = Integer.parseInt(values);
//			intervals = getPrefs.getString("listIntervals", "10000");
//			iintervals = Integer.parseInt(intervals);
//			hasCheckPref = getPrefs.getBoolean("filtered_checkbox", true);
//
//			final String[] projection = null;
//			final String selection = null;
//			final String[] selectionArgs = null;
//			final String sortOrder = android.provider.CallLog.Calls.DATE
//					+ " DESC";
//			Cursor cursor = null;
//			Cursor cur = null;
//			Cursor kursor = null;
//			try {
//				cursor = getContentResolver().query(
//						Uri.parse("content://call_log/calls"), projection,
//						selection, selectionArgs, sortOrder);
//				String[] projectionn = new String[] { Calls.NUMBER,
//						Calls.CACHED_NAME };
//				cur = getContentResolver().query(Calls.CONTENT_URI,
//						projectionn, null, null, Calls.DATE + " desc");
//				cur.moveToFirst();
//				lastCallnumber = cur.getString(0);
//				lastName = cur.getString(1);
//
//				if (!hasCheckPref) {
//					kursor = getContentResolver().query(
//							TRESC_URI,
//							FROM,
//							NUMER
//									+ "='"
//									+ lastCallnumber.substring(lastCallnumber
//											.length() - 9) + "'", null, null);
//					pokazZdarzenia(kursor);
//				}
//
//				cursor.moveToFirst();
//				String callLogID = cursor.getString(cursor
//						.getColumnIndex(android.provider.CallLog.Calls._ID));
//				callNumber = cursor.getString(cursor
//						.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
//				String callDate = cursor.getString(cursor
//						.getColumnIndex(android.provider.CallLog.Calls.DATE));
//				String callType = cursor.getString(cursor
//						.getColumnIndex(android.provider.CallLog.Calls.TYPE));
//				String isCallNew = cursor.getString(cursor
//						.getColumnIndex(android.provider.CallLog.Calls.NEW));
//				if (Integer.parseInt(callType) == android.provider.CallLog.Calls.MISSED_TYPE
//						&& Integer.parseInt(isCallNew) > 0) {
//					bool = true;
//				}
//
//			} catch (Exception ex) {
//			} finally {
//				if (cursor != null) {
//					cursor.close();
//				}
//				if (cur != null) {
//					cur.close();
//				}
//				if (kursor != null) {
//					kursor.close();
//				}
//
//			}
//
//			if (bool == false) {
//				am.cancel(pi);
//			}
//
//			if (!hasCheckPref && bool == true) {
//				if (numerDB != null && numerDB.length() != 0) {
//					i = new Intent(getApplicationContext(),
//							NotificationListActivity.class);
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					Toast.makeText(
//							getApplicationContext(),
//							"Nieodebrano : " + lastCallnumber + "\n"
//									+ "!hasCheckPref", Toast.LENGTH_SHORT)
//							.show();
//					// am.setRepeating(AlarmManager.RTC_WAKEUP,
//					// System.currentTimeMillis() + 1000, iintervals, pi);
//					Calendar now = Calendar.getInstance();
//					dbManager.dodajZdarzenie(
//							NAZWA_TABELI_2,
//							getContactName(lastCallnumber)[1],
//							lastName,
//							lastCallnumber,
//							android.R.drawable.sym_call_missed,
//							"" + now.get(Calendar.HOUR_OF_DAY) + ":"
//									+ now.get(Calendar.MINUTE));
//					startActivity(i);
//					bool = false;
//					numerDB = "";
//					// i = new Intent(getApplicationContext(),
//					// NotificationsActivity.class);
//					// i.putExtra("lastCallnumber", lastCallnumber);
//					// i.putExtra("lastName", lastName);
//					// pi = PendingIntent.getActivity(getApplicationContext(),
//					// 0,
//					// i, PendingIntent.FLAG_CANCEL_CURRENT);
//					// Toast.makeText(getApplicationContext(),
//					// "Nieodebrano : " + lastCallnumber+"\n"+
//					// "!hasCheckPref",
//					// Toast.LENGTH_SHORT).show();
//					// am.setRepeating(AlarmManager.RTC_WAKEUP,
//					// System.currentTimeMillis() + 1000, iintervals, pi);
//					// Calendar now = Calendar.getInstance();
//					// dbManager.dodajZdarzenie(NAZWA_TABELI_2,
//					// getContactName(lastCallnumber)[1], lastName,
//					// lastCallnumber, android.R.drawable.sym_call_missed,
//					// ""+now.get(Calendar.HOUR_OF_DAY) + ":" +
//					// now.get(Calendar.MINUTE));
//					// bool = false;
//					// numerDB = "";
//				}
//			} else if (hasCheckPref && bool == true) {
//				i = new Intent(getApplicationContext(),
//						NotificationListActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				Toast.makeText(
//						getApplicationContext(),
//						"Nieodebrano : " + lastCallnumber + "\n"
//								+ "hasCheckPref", Toast.LENGTH_SHORT).show();
//				Calendar now = Calendar.getInstance();
//				dbManager.dodajZdarzenie(
//						NAZWA_TABELI_2,
//						getContactName(lastCallnumber)[1],
//						lastName,
//						lastCallnumber,
//						android.R.drawable.sym_call_missed,
//						"" + now.get(Calendar.HOUR_OF_DAY) + ":"
//								+ now.get(Calendar.MINUTE));
//				startActivity(i);
//				bool = false;
//				// i = new Intent(getApplicationContext(),
//				// NotificationsActivity.class);
//				// i.putExtra("lastCallnumber", lastCallnumber);
//				// i.putExtra("lastName", lastName);
//				// pi = PendingIntent.getActivity(getApplicationContext(), 0,
//				// i, PendingIntent.FLAG_CANCEL_CURRENT);
//				// Toast.makeText(getApplicationContext(),
//				// "Nieodebrano : " + lastCallnumber+"\n"+
//				// "hasCheckPref",
//				// Toast.LENGTH_SHORT).show();
//				// am.setRepeating(AlarmManager.RTC_WAKEUP,
//				// System.currentTimeMillis() + 1000, iintervals, pi);
//				// Calendar now = Calendar.getInstance();
//				// dbManager.dodajZdarzenie(NAZWA_TABELI_2,
//				// getContactName(lastCallnumber)[1], lastName, lastCallnumber,
//				// android.R.drawable.sym_call_missed,
//				// ""+now.get(Calendar.HOUR_OF_DAY) + ":" +
//				// now.get(Calendar.MINUTE));
//				// bool = false;
//			}
//		}
//	}
//
//	private String[] getContactName(String number) {
//		String name = "";
//		String photo = "";
//		Cursor cursor = getContentResolver().query(
//				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
//				null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//
//		while (cursor.moveToNext()) {
//			String contactName = cursor
//					.getString(cursor
//							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//			String contactNumber = cursor
//					.getString(cursor
//							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//			String contactPhoto = cursor
//					.getString(cursor
//							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//
//			if (convertStringNr(contactNumber).equalsIgnoreCase(number)) {
//				Log.i("nr", "NR: " + convertStringNr(contactNumber) + " NAME: "
//						+ contactName);
//				name = contactName;
//				if (contactPhoto != null) {
//					photo = contactPhoto;
//				} else {
//					photo = "";
//				}
//				return new String[] { name, photo };
//			}
//		}
//		return new String[] { number, "" };
//
//	}
//
//	public String convertStringNr(String nr) {
//
//		String number = "";
//		String[] temp;
//		String delimiter = "-";
//		temp = nr.replaceAll("\\s", "").split(delimiter);
//		StringBuilder sb = new StringBuilder();
//		for (int ii = 0; ii < temp.length; ii++) {
//			sb.append(temp[ii]);
//		}
//		if (sb.length() >= 9) {
//			number = sb.substring(sb.length() - 9);
//			return number;
//		} else {
//			return nr;
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