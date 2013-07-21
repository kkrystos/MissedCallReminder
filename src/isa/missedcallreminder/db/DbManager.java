package isa.missedcallreminder.db;

import static isa.missedcallreminder.db.Const.BODY;
import static isa.missedcallreminder.db.Const.ICON;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.PHOTO;
import static isa.missedcallreminder.db.Const.TIME;

import java.io.BufferedInputStream;
import java.io.InputStream;

import isa.missedcallreminder.NotificationSmsActivity;
import isa.missedcallreminder.NotificationsActivity;
import isa.missedcallreminder.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
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
		Cursor kursor = myDb.query(tabela, null, null, null, null, null, "_id DESC");
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
				Uri my_contact_Uri = Uri.withAppendedPath(
						ContactsContract.Contacts.CONTENT_URI,
						cursor.getString(cursor.getColumnIndex(PHOTO)));
				InputStream photo_stream = ContactsContract.Contacts
						.openContactPhotoInputStream(
								context.getContentResolver(), my_contact_Uri);
				BufferedInputStream buf = new BufferedInputStream(photo_stream);
				Log.i("test", "BufferedInputStream: " + buf);
				Bitmap my_btmp = BitmapFactory.decodeStream(buf);
				Log.i("test", "Bitmap: " + my_btmp);
				if (my_btmp != null) {
					holder.imageContact.setImageBitmap(my_btmp);
				}else {
					holder.imageContact.setImageResource(R.drawable.ic_contact_img);
				}
			} else {
				holder.imageContact.setImageResource(R.drawable.ic_contact_img);
			}
			return convertView;
		}
	}
	
	public String[] getContactPhotoUri(String phoneNumber){
		 Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
			Cursor cur = context.getContentResolver().query(uri, null, null, null, null);
			ContentResolver contect_resolver = context.getContentResolver();
			
			String photoUri = "";
			String contactName = "";
			String phoneNrPick = "";
			String cintactId = "";
		
			if (cur.moveToFirst()) {
				cintactId = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
				long idd = cur.getLong(cur.getColumnIndexOrThrow(ContactsContract.Contacts.Photo._ID));

				Cursor phoneCur = contect_resolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ?", new String[] { cintactId }, null);
				if (phoneCur.moveToFirst()) {
					contactName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					phoneNrPick = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String photoId = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
					if (photoId != null) {
						photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photoId) ).toString();
//						Log.i("test", "photoUriF: "+photoUri);
					}		
				}
			}
			Log.i("test", "cintactId: "+cintactId);
		return new String[]{photoUri,contactName, cintactId};
		}
	
//	public String[] getContactInfo(String phoneNumber){
//		
//		 Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
//			    Cursor cursor = context.getContentResolver().query(uri, new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID }, null, null, null);
//
//			    String contactId = "";
//			    String contactName = "";
//			    String phoneNrPick = "";
//			    String photoUri = "";
//
//			    if (cursor.moveToFirst()) {
//			        do {
//			        contactId = cursor.getString(cursor.getColumnIndex(PhoneLookup._ID));
//			        } while (cursor.moveToNext());
//			    }
//			    
//				Cursor phoneCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//								+ " = ?", new String[] { contactId }, null);
//			    
//				if (phoneCur.moveToFirst()) {
//					contactName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//					phoneNrPick = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//					String photoId = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
////					Log.i("test", "photoId: "+ photoId);
//					if (photoId != null) {
//						photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photoId)).toString();
//						return new String[]{contactName, photoUri };
//					}
//				}
//				return new String[]{contactName, photoUri };
//	}
	
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
