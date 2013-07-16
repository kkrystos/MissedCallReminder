package isa.missedcallreminder;

import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import isa.missedcallreminder.db.DbManager;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestActivity extends Activity implements OnClickListener{
	EditText nameET;
	EditText nrET;
	DbManager dbManager;
	AlarmManager am;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		dbManager = new DbManager(getApplicationContext());
		nameET = (EditText) findViewById(R.id.editTextName);
		nameET.setText("Nazwa");
		nrET = (EditText) findViewById(R.id.editTextNumber);
		nrET.setText("517515654");
		
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		Button callBtn = (Button) findViewById(R.id.buttonCall);
		callBtn.setOnClickListener(this);
		Button smsBtn = (Button) findViewById(R.id.buttonSMS);
		smsBtn.setOnClickListener(this);
		Button notiBtn = (Button) findViewById(R.id.buttonStartNotification);
		notiBtn.setOnClickListener(this);
		Button clearBtn = (Button) findViewById(R.id.buttonClearTable);
		clearBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Calendar now = Calendar.getInstance();
		Intent i = new Intent(getApplicationContext(),
				NotificationListActivity.class);
		switch (v.getId()) {
		case R.id.buttonCall:
			dbManager.dodajZdarzenie(
					NAZWA_TABELI_2,
					"content://com.android.contacts/display_photo/1",
					nameET.getText().toString(),
					nrET.getText().toString(),
					android.R.drawable.sym_call_missed,
					"" + now.get(Calendar.HOUR_OF_DAY) + ":"
							+ now.get(Calendar.MINUTE), "");
			Intent ibuttonCall = new Intent(getApplicationContext(),
					NotificationsActivity.class);
			ibuttonCall.putExtra("lastCallnumber", nameET.getText().toString());
			ibuttonCall.putExtra("lastName", nrET.getText().toString());
			PendingIntent pi = PendingIntent.getActivity(
					getApplicationContext(), 0, ibuttonCall,
					PendingIntent.FLAG_CANCEL_CURRENT);
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis() + 1000, 10000, pi);
			startActivity(i);
			break;
		case R.id.buttonSMS:
			dbManager.dodajZdarzenie(
					NAZWA_TABELI_2,
					"",
					nameET.getText().toString(),
					nrET.getText().toString(),
					android.R.drawable.sym_action_email,
					"" + now.get(Calendar.HOUR_OF_DAY) + ":"
							+ now.get(Calendar.MINUTE), "Jak siê masz? kochanie jak siê masz? Jak siê masz? kochanie jak siê masz?");
			Intent ibuttonSms = new Intent(getApplicationContext(),
					NotificationSmsActivity.class);
			ibuttonSms.putExtra("lastCallnumber", nameET.getText().toString());
			ibuttonSms.putExtra("lastName", nrET.getText().toString());
			PendingIntent piSms = PendingIntent.getActivity(
					getApplicationContext(), 0, ibuttonSms,
					PendingIntent.FLAG_CANCEL_CURRENT);
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis() + 1000, 10000, piSms);
			startActivity(i);
			break;
		case R.id.buttonStartNotification:
			// Intent i = new Intent(getApplicationContext(),
			// NotificationListActivity.class);
			// startActivity(i);
			startActivity(i);
			break;
		case R.id.buttonClearTable:
			dbManager.deleteTable(NAZWA_TABELI_2);
			break;

		}
	}
	

}
