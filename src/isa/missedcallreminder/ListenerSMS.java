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
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class ListenerSMS extends BroadcastReceiver {
	private String smsNumber;
	private String smsName;
	private String smsBody;
	private String smsNumberSub;
	private static String[] FROM = { NAZWA, NUMER, _ID, };
	private String numerDB;
	private AlarmManager am;
	private String values;
	private String intervals;
	private int ivalues;
	private int iintervals;
	boolean hasCheckPref = true;
	Context context;
	DbManager dbManager;
	private Uri photoUri;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Cursor kursor = null;
		this.context = context;
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		values = getPrefs.getString("listDuration", "500");
		ivalues = Integer.parseInt(values);
		intervals = getPrefs.getString("listIntervals", "10000");
		iintervals = Integer.parseInt(intervals);
		hasCheckPref = getPrefs.getBoolean("filtered_checkbox", true);
		dbManager = new DbManager(context);

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

			if (!hasCheckPref) {
				kursor = context.getContentResolver().query(TRESC_URI, FROM,
						NUMER + "='" + smsNumberSub + "'", null, null);
				pokazZdarzenia(kursor);

				if (numerDB != null && numerDB.length() != 0) {
					Intent i = new Intent(context, NotificationListActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);					
					Calendar now = Calendar.getInstance();
					dbManager.dodajZdarzenie(NAZWA_TABELI_2, dbManager.getContactPhotoUri(smsNumberSub)[2],dbManager.getContactPhotoUri(smsNumberSub)[1], smsNumberSub, android.R.drawable.sym_action_email,""+now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE), smsBody);
					Intent iNotiSms = new Intent(context, NotificationSmsActivity.class);
					iNotiSms.putExtra("smsNumberSub", smsNumberSub);
					iNotiSms.putExtra("smsName", dbManager.getContactPhotoUri(smsNumberSub)[1]);
					iNotiSms.putExtra("smsBody", smsBody);
					PendingIntent pi = PendingIntent.getActivity(context, 0, iNotiSms, PendingIntent.FLAG_CANCEL_CURRENT);
					am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000, iintervals, pi);
					context.startActivity(i);
					numerDB = "";
				}
			} else if (hasCheckPref) {
				Intent i = new Intent(context, NotificationListActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Toast.makeText(context,"SMS: " + "from: \n" + smsNumberSub + "\n"+ dbManager.getContactPhotoUri(smsNumberSub)[1] + "\n" + smsBody,
						Toast.LENGTH_SHORT).show();
				Calendar now = Calendar.getInstance();
				dbManager.dodajZdarzenie(NAZWA_TABELI_2, dbManager.getContactPhotoUri(smsNumberSub)[2],dbManager.getContactPhotoUri(smsNumberSub)[1], smsNumberSub, android.R.drawable.sym_action_email, ""+now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE), smsBody);
				Intent iNotiSms = new Intent(context, NotificationSmsActivity.class);
				iNotiSms.putExtra("smsNumberSub", smsNumberSub);
				iNotiSms.putExtra("smsName", dbManager.getContactPhotoUri(smsNumberSub)[1]);
				iNotiSms.putExtra("smsBody", smsBody);
				PendingIntent pi = PendingIntent.getActivity(context, 0, iNotiSms,PendingIntent.FLAG_CANCEL_CURRENT);
				am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000, iintervals, pi);
				context.startActivity(i);
			}
		}
	}

	public void pokazZdarzenia(Cursor kursor) {
		while (kursor.moveToNext()) {
			smsName = kursor.getString(0);
			numerDB = kursor.getString(1);
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

//	private String[] getContactName(String number) {
//		String name = "";
//		String photo = "";
//		Cursor cursor = context.getContentResolver().query(
//				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
//				null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//		while (cursor.moveToNext()) {
//			String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//			String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////			String contactPhoto
//			String photoId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
//			if (photoId != null) {
//				photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photoId));
//			}else {
//				photoUri = null;
//			}
//
//			if (convertStringNr(contactNumber).equalsIgnoreCase(number)) {
//				Log.i("nr", "NR: " + convertStringNr(contactNumber) + " NAME: "
//						+ contactName);
//				name = contactName;
//				if (photoUri!=null) {
//					photo = photoUri.toString();
//				} else {
//					photo = "";
//				}
//				return new String[]{name, photo};
//			}
//		}
//		return new String[]{number, ""};
//
//	}
}
