package isa.missedcallreminder;

import isa.missedcallreminder.db.DbManager;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TestActivity extends Activity implements OnClickListener{
	EditText nameET;
	EditText nrET;
	DbManager dbManager;
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
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonCall:
			dbManager.dodajZdarzenie(NAZWA_TABELI_2, "photo", nameET.getText().toString(), nrET.getText().toString());
			break;
		case R.id.buttonSMS:
			dbManager.dodajZdarzenie(NAZWA_TABELI_2, "photo", nameET.getText().toString(), nrET.getText().toString());
			break;
		case R.id.buttonStartNotification:
			Intent	i = new Intent(getApplicationContext(), NotificationListActivity.class);
			startActivity(i);
			break;
		case R.id.buttonClearTable:
			dbManager.deleteTable(NAZWA_TABELI_2);
			break;
		
		}
	}
	

}
