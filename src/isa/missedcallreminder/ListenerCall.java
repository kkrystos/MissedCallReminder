package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import isa.missedcallreminder.db.DbManager;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ListenerCall extends BroadcastReceiver {
	private static String[] FROM = { NAZWA, NUMER, _ID, };
	private String numerDB;
	boolean hasCheckPref = true;
	private DbManager dbManager;
	Context context;
	static boolean ring = false;
	static boolean callReceived = false;
	String lastCallnumber;
	private AlarmManager am;
	private String intervals;
	private int iintervals;
	private Uri photoUri;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		Cursor kursor = null;
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		intervals = getPrefs.getString("listIntervals", "10000");
		iintervals = Integer.parseInt(intervals);
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		hasCheckPref = getPrefs.getBoolean("filtered_checkbox", true);
		dbManager = new DbManager(context);
		if (state == null)
			return;

		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			ring = true;
			Bundle bundle = intent.getExtras();
			lastCallnumber = bundle.getString("incoming_number");
		}

		if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
			callReceived = true;
		}

		if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
			if (ring == true && callReceived == false) {
				// Missed Call
				
				if (!hasCheckPref) {
					kursor = context.getContentResolver().query(TRESC_URI, FROM,NUMER+ "='"+ lastCallnumber.substring(lastCallnumber.length() - 9) + "'", null, null);
					pokazZdarzenia(kursor);
					kursor.close();
					if (numerDB != null && numerDB.length() != 0) {
						Log.i("test", "listener photoURI: "+dbManager.getContactPhotoUri(lastCallnumber)[0]);
						Intent i = new Intent(context, NotificationListActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Toast.makeText(context, "Nieodebrano : " + lastCallnumber + "\n"+ "!hasCheckPref", Toast.LENGTH_SHORT).show();
						Calendar now = Calendar.getInstance();
						dbManager.dodajZdarzenie(NAZWA_TABELI_2,dbManager.getContactPhotoUri(lastCallnumber)[2],dbManager.getContactPhotoUri(lastCallnumber)[1] ,lastCallnumber,android.R.drawable.sym_call_missed,
								"" + now.get(Calendar.HOUR_OF_DAY) + ":"
										+ now.get(Calendar.MINUTE), "");
						 Intent iNotiCall = new Intent(context, NotificationsActivity.class);
						 iNotiCall.putExtra("lastCallnumber", lastCallnumber);
						 iNotiCall.putExtra("lastName", dbManager.getContactPhotoUri(lastCallnumber)[1]);
						 PendingIntent pi = PendingIntent.getActivity(context,0, iNotiCall, PendingIntent.FLAG_CANCEL_CURRENT);
						 Toast.makeText(context,"Nieodebrano : " + lastCallnumber+"\n"+"!hasCheckPref",Toast.LENGTH_SHORT).show();
						 am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, iintervals, pi);
						 context.startActivity(i);
						 numerDB = "";
					}
				}else if (hasCheckPref) {
					Log.i("test", "listener photoURI: "+dbManager.getContactPhotoUri(lastCallnumber)[0]);
					Intent i = new Intent(context, NotificationListActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Toast.makeText(context, "Nieodebrano : " + lastCallnumber + "\n"+ "hasCheckPref", Toast.LENGTH_SHORT).show();
					Calendar now = Calendar.getInstance();
					dbManager.dodajZdarzenie(NAZWA_TABELI_2,dbManager.getContactPhotoUri(lastCallnumber)[2],dbManager.getContactPhotoUri(lastCallnumber)[1],lastCallnumber,android.R.drawable.sym_call_missed,"" + now.get(Calendar.HOUR_OF_DAY) + ":"
									+ now.get(Calendar.MINUTE), "");
					Intent iNotiCall = new Intent(context,
					 NotificationsActivity.class);
					iNotiCall.putExtra("lastCallnumber", lastCallnumber);
					iNotiCall.putExtra("lastName", dbManager.getContactPhotoUri(lastCallnumber)[1]);
					PendingIntent pi = PendingIntent.getActivity(context, 0, iNotiCall, PendingIntent.FLAG_CANCEL_CURRENT);
					Toast.makeText(context,"Nieodebrano : " + lastCallnumber+"\n"+"hasCheckPref",Toast.LENGTH_SHORT).show();
					am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, iintervals, pi);
					context.startActivity(i);
				}
				Toast.makeText(context,"Nieodebrano : " + lastCallnumber+"\n"+" Missed ",Toast.LENGTH_SHORT).show();
			}
		}
	}

	public String convertStringNr(String nr) {

		String number = "";
		String[] temp;
		String delimiter = "-";
		temp = nr.replaceAll("\\s", "").split(delimiter);
		StringBuilder sb = new StringBuilder();
		for (int ii = 0; ii < temp.length; ii++) {
			sb.append(temp[ii]);
		}
		if (sb.length() >= 9) {
			number = sb.substring(sb.length() - 9);
			return number;
		} else {
			return nr;
		}
	}
	
	
	public void pokazZdarzenia(Cursor kursor) {
		while (kursor.moveToNext()) {
			numerDB = kursor.getString(1);
		}
	}
}