package isa.missedcallreminder;

import isa.missedcallreminder.FilteredNumbers.Contact;

import isa.missedcallreminder.db.DbManager;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import java.util.ArrayList;
import java.util.Random;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainPreferenceActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	private String callNumber;
	private AlarmManager am;
	private Intent i;
	private Intent ii;
	private PendingIntent pi;
	private PendingIntent pii;
	private String locale;
	NotificationManager nm;
	DbManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/////////////// test z buttonami
		dbManager = new DbManager(getApplicationContext(), this);
		setContentView(R.layout.activity_main);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Button bs = (Button) findViewById(R.id.button1);
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		bs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	    	  
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//		            //Set the activity to be launch when selected
//		            Intent notificationIntent = new Intent(getApplicationContext(), MainPreferenceActivity.class);
//		            PendingIntent contentIntent = PendingIntent.getActivity(MainPreferenceActivity.this, 0, notificationIntent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
//		            
//		            Intent callIntent = new Intent(Intent.ACTION_CALL);
//		            callIntent.setData(Uri.parse("tel:517515654"));
//		            
//		            PendingIntent pendingIntentCall = PendingIntent.getActivity(getApplicationContext(), 0, callIntent, 0);
//		            
//		            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//		            smsIntent.setData(Uri.parse("sms:517515654"));
//		            smsIntent.putExtra("sms_body", "dupa dupa dupa");
//		            PendingIntent pendingIntentSMS = PendingIntent.getActivity(getApplicationContext(), 0, smsIntent, 0);
//		            
//		            //Set up the notification
//		            Notification.Builder noti = new Notification.Builder(getApplicationContext())
//		            	.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
//		                .setSmallIcon(R.drawable.ic_launcher)
//		                .setTicker("This is a notification marquee")
//		                .setWhen(System.currentTimeMillis())
//		                .setContentTitle("Message Title 6")
//		                .setContentText("Message Content 6 will have some action buttons")
//		                .setContentIntent(contentIntent)
//		                //At most three action buttons can be added
//		                .addAction(android.R.drawable.ic_dialog_dialer, "Call", pendingIntentCall)
//		                .addAction(android.R.drawable.ic_menu_compass, "SMS", pendingIntentSMS)
//		                .addAction(android.R.drawable.ic_menu_info_details, "Action 3", contentIntent)
//		                .setAutoCancel(true);
//		     
//		            //Show the notification
//		            nm.notify(1, noti.build());
//				}
//				else {
//					
//					Intent notificationIntent = new Intent(getApplicationContext(), MainPreferenceActivity.class);
//			        PendingIntent contentIntent = PendingIntent.getActivity(MainPreferenceActivity.this, 0, notificationIntent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
//					NotificationCompat.Builder noti = new NotificationCompat.Builder(getApplicationContext())
//	            	.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
//	                .setSmallIcon(R.drawable.ic_launcher)
//	                .setTicker("This is a notification marquee")
//	                .setWhen(System.currentTimeMillis())
//	                .setContentTitle("Message Title 6")
//	                .setContentText("Message Content 6 will have some action buttons")
//	                .setContentIntent(contentIntent)
//	                //At most three action buttons can be added
//	                .addAction(android.R.drawable.ic_dialog_dialer, "Action 1", contentIntent)
//	                .addAction(android.R.drawable.ic_menu_compass, "Action 2", contentIntent)
//	                .addAction(android.R.drawable.ic_menu_info_details, "Action 3", contentIntent)
//	                .setAutoCancel(true);
//					
//					nm.notify(1, noti.build());
//				}
				//call oks
			Intent	i = new Intent(getApplicationContext(), NotificationListActivity.class);
			startActivity(i);
//				i.putExtra("lastCallnumber", "517515654");
//				i.putExtra("lastName", "Kamciaks");
//				PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
//						i, PendingIntent.FLAG_CANCEL_CURRENT);
//				Toast.makeText(getApplicationContext(),
//						"Nieodebrano : " + 517515654,
//						Toast.LENGTH_SHORT).show();
//				
//				am.setRepeating(AlarmManager.RTC_WAKEUP,
//						System.currentTimeMillis() + 1000, 15000, pi);
				//sms ?
//				Intent	i = new Intent(getApplicationContext(), NotificationSmsActivity.class);
//				i.putExtra("smsNumberSub", "517515654");
//				i.putExtra("smsName", "kamciaks");
//				i.putExtra("smsBody", "dupa jasiu");
//				pi = PendingIntent.getActivity(getApplicationContext(), 0,
//						i, PendingIntent.FLAG_CANCEL_CURRENT);
//				Toast.makeText(getApplicationContext(),
//						"Nieodebrano : " + 517515654,
//						Toast.LENGTH_SHORT).show();
//				am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//				am.setRepeating(AlarmManager.RTC_WAKEUP,
//						System.currentTimeMillis() + 1000, 15000, pi);
					
				
				
			}
		});
		
		
		
		Button bst = (Button) findViewById(R.id.button2);
		bst.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				am.cancel(pi);
//				Intent	i = new Intent(getApplicationContext(), NotificationSmsActivity.class);
//				i.putExtra("smsNumberSub", "517515654");
//				i.putExtra("smsName", "Kamciaks");
//				i.putExtra("smsBody", "Hej co tam u Ciebie?");
//				pi = PendingIntent.getActivity(getApplicationContext(), 0,
//						i, PendingIntent.FLAG_CANCEL_CURRENT);
//				Toast.makeText(getApplicationContext(),
//						"Nieodebrano : " + 517515654,
//						Toast.LENGTH_SHORT).show();
//				am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//				am.setRepeating(AlarmManager.RTC_WAKEUP,
//						System.currentTimeMillis() + 1000, 15000, pi);
				
//				Math.random();
				
				
//				dbManager.dodajZdarzenie(NAZWA_TABELI_2,"0", "Kamciaks" + (Math.random()*0.01) , ""+(Math.random() * 0.001));
//				Toast.makeText(getApplicationContext(), "dodalem nieodebrane", 0).show();
			}
		});
		
//////////////////////////
		locale = java.util.Locale.getDefault().getLanguage();

		if (locale.equalsIgnoreCase("pl")) {

			addPreferencesFromResource(R.xml.main_pref_activity);
		} else {
			addPreferencesFromResource(R.xml.preferences_en);
		}

//		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		i = new Intent(this, NotificationsActivity.class);
//		pi = PendingIntent.getActivity(this, 0, i, 0);
//		ii = new Intent(this, NotificationsActivity.class);
//		pii = PendingIntent.getActivity(this, 0, ii, 0);

		Preference p = findPreference("editTextPref");
		p.setOnPreferenceClickListener(this);

		Preference pp = findPreference("call_sett");
		pp.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(getApplicationContext(),
						IndividualPreferenceCallActivity.class);
				startActivity(i);
				return false;
			}
		});

		Preference sp = findPreference("sms_sett");
		sp.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {

				Intent i = new Intent(getApplicationContext(),
						IndividualPreferenceActivity.class);
				startActivity(i);

				return false;
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean isMissiedCall = getPrefs.getBoolean("missCallCheck", true);
		boolean isMissedSMS = getPrefs.getBoolean("missSMSCheck", true);
		boolean isActive = getPrefs.getBoolean("autostart", true);
		if (isActive == true) {
			if (isMissiedCall == true) {
				startService(new Intent(this, ServiceCall.class));
			} else {
				stopService(new Intent(this, ServiceCall.class));
			}
			if (isMissedSMS == true) {
				startService(new Intent(this, ServiceSMS.class));
			} else {
				stopService(new Intent(this, ServiceSMS.class));
			}
		} else {
			stopService(new Intent(this, ServiceCall.class));
			stopService(new Intent(this, ServiceSMS.class));
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
//		am.cancel(pi);
//		am.cancel(pii);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean isMissiedCall = getPrefs.getBoolean("missCallCheck", true);
		boolean isMissedSMS = getPrefs.getBoolean("missSMSCheck", true);
		boolean isActive = getPrefs.getBoolean("autostart", true);
		if (isActive == true) {
			if (isMissiedCall == true) {
				startService(new Intent(this, ServiceCall.class));
			} else {
				stopService(new Intent(this, ServiceCall.class));
			}
			if (isMissedSMS == true) {
				startService(new Intent(this, ServiceSMS.class));
			} else {
				stopService(new Intent(this, ServiceSMS.class));
			}
		} else {
			stopService(new Intent(this, ServiceCall.class));
			stopService(new Intent(this, ServiceSMS.class));
		}
	}

	public boolean onPreferenceClick(Preference preference) {
		Intent i = new Intent(this, FilteredNumber.class);
		startActivity(i);
		return false;
	}
}
