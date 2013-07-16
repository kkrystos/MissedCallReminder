package isa.missedcallreminder.db;

import static isa.missedcallreminder.db.Const.ICON;
import static isa.missedcallreminder.db.Const.BODY;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.PHOTO;
import static isa.missedcallreminder.db.Const.TIME;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import isa.missedcallreminder.NotificationSmsActivity;
import isa.missedcallreminder.NotificationsActivity;
import isa.missedcallreminder.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DbManager {
	DataEvent dataEvent;
	private Context context;
	SQLiteDatabase myDb;
	Activity activity;

	public DbManager(Context ctx, Activity activity) {
		this.context = ctx;
		this.activity = activity;
		this.dataEvent = new DataEvent(ctx);
		myDb = dataEvent.getReadableDatabase();
	}
	
	public DbManager(Context ctx) {
		this.context = ctx;
		this.dataEvent = new DataEvent(ctx);
		myDb = dataEvent.getReadableDatabase();
	}

	public void dodajZdarzenie(String nazwa_tabeli, String photo, String nazwa,
			String nr, int icon) {
		ContentValues wartosci = new ContentValues();
		wartosci.put(PHOTO, photo);
		wartosci.put(NAZWA, nazwa);
		wartosci.put(NUMER, nr);
		wartosci.put(ICON, icon);
		dataEvent.getWritableDatabase().insert(nazwa_tabeli, null, wartosci);
		dataEvent.close();
	}
	
	public void dodajZdarzenie(String nazwa_tabeli, String photo, String nazwa,
			String nr, int icon, String czas, String body) {
		ContentValues wartosci = new ContentValues();
		wartosci.put(PHOTO, photo);
		wartosci.put(NAZWA, nazwa);
		wartosci.put(NUMER, nr);
		wartosci.put(ICON, icon);
		wartosci.put(TIME, czas);
		wartosci.put(BODY, body);
		dataEvent.getWritableDatabase().insert(nazwa_tabeli, null, wartosci);
		dataEvent.close();
	}

	public void deleteTable(String tableName) {
		SQLiteDatabase db = dataEvent.getWritableDatabase();
		db.execSQL("DELETE FROM " + tableName);
		dataEvent.close();
	}

	public void pobierzNieodebrane(String tabela, String[] FROM, int[] DO,
			ListView lv) {
		Cursor kursor = myDb.query(tabela, null, null, null, null, null, "nazwa");
		activity.startManagingCursor(kursor);
		MyCursorAdapter myCursorAdapter = new MyCursorAdapter(context,R.layout.notification_layout, kursor, FROM, DO);
		lv.setAdapter(myCursorAdapter);
	}

	class MyCursorAdapter extends SimpleCursorAdapter {
		Cursor c;
		Context context;
		Cursor cursor;

		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			this.c = c;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			cursor = (Cursor) getItem(position);
			
			if (convertView == null) {	
				LayoutInflater inflater = LayoutInflater.from(activity);
				convertView = inflater.inflate(R.layout.notification_layout,parent, false);
				holder = new ViewHolder();
				holder.nazwaTv = (TextView) convertView.findViewById(R.id.notification_main_txt);
				holder.numerTv = (TextView) convertView.findViewById(R.id.notification_main2_txt);
				holder.czasTv = (TextView) convertView.findViewById(R.id.notification_main_timeTv);
				holder.buttonCall = (Button) convertView.findViewById(R.id.noti_callBtn);
				holder.buttonSms = (Button) convertView.findViewById(R.id.noti_smsBtn);
				holder.imageIcon = (ImageView) convertView.findViewById(R.id.notification_layout_iconImg);
				holder.imageContact = (ImageView) convertView.findViewById(R.id.notification_contactImg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.nazwaTv.setText(cursor.getString(cursor.getColumnIndex(NAZWA)));
			holder.numerTv.setText(cursor.getString(cursor.getColumnIndex(NUMER))+" "+ cursor.getString(cursor.getColumnIndex(BODY)));
			holder.czasTv.setText(cursor.getString(cursor.getColumnIndex(TIME)));
			holder.buttonCall.setId(position);
			holder.buttonCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					hideAll(context);
					activity.finish();
					Cursor cursor = (Cursor) getItem(v.getId());
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					callIntent.setData(Uri.parse("tel:" + cursor.getString(cursor.getColumnIndex(NUMER))));
					context.startActivity(callIntent);
				}
			});
			holder.buttonSms.setId(position);
			holder.buttonSms.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					hideAll(context);
					activity.finish();
					Cursor cursor = (Cursor) getItem(v.getId());
					Intent smsIntent = new Intent(Intent.ACTION_VIEW);
					smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					smsIntent.setData(Uri.parse("sms:" + cursor.getString(cursor.getColumnIndex(NUMER))));
					context.startActivity(smsIntent);
				}
			});
			holder.imageIcon.setImageResource(cursor.getInt(cursor.getColumnIndex(ICON)));
			if (!cursor.getString(cursor.getColumnIndex(PHOTO)).equalsIgnoreCase("")) {
				holder.imageContact.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(PHOTO))));
			} else {
				holder.imageContact.setImageResource(R.drawable.ic_contact_img);
			}
			return convertView;
		}
	}
	
	public void hideAll(Context context){
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, NotificationsActivity.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
		Intent ii = new Intent(context, NotificationSmsActivity.class);
		PendingIntent pii = PendingIntent.getActivity(context, 0, ii, 0);
		am.cancel(pi);
		am.cancel(pii);
		
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(1);
		nm.cancel(2);
		
		deleteTable(NAZWA_TABELI_2);
	}

	static class ViewHolder {
		TextView nazwaTv;
		TextView numerTv;
		TextView text3;
		TextView czasTv;
		ImageView imageContact;
		ImageView imageIcon;
		Button buttonCall;
		Button buttonSms;
	}
}
