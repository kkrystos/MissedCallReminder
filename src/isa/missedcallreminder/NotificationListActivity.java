package isa.missedcallreminder;

import static isa.missedcallreminder.db.Const.ICON;
import static isa.missedcallreminder.db.Const.BODY;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.PHOTO;
import static isa.missedcallreminder.db.Const.TIME;
import isa.missedcallreminder.db.DataEvent;
import isa.missedcallreminder.db.DbManager;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public final class NotificationListActivity extends ListActivity implements OnClickListener {
	
	ListView lv;
	private static String[] FROM = { NAZWA, NUMER, ICON, TIME, PHOTO, BODY };
	private static int[] DO = { R.id.notification_main_txt, R.id.notification_main2_txt, R.id.notification_layout_iconImg, R.id.notification_main_timeTv, R.id.notification_contactImg };
	DataEvent dataEvent;
	DbManager dbManager;
	
	static Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		activity = this;
		dbManager = new DbManager(getApplicationContext(), this);
		lv = getListView();
		dbManager.pobierzNieodebrane(NAZWA_TABELI_2, FROM, DO, lv);
		Button clearAllBtn = (Button) findViewById(R.id.notificationList_clearAllBtn);
		clearAllBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notificationList_clearAllBtn:
			dbManager.deleteTable(NAZWA_TABELI_2);
			Intent i = new Intent(this, HideNotification.class);
			startActivity(i);
			finish();
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK)
	    {
			dbManager.deleteTable(NAZWA_TABELI_2);
			Intent i = new Intent(this, HideNotification.class);
			startActivity(i);
			finish();
	    	
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
