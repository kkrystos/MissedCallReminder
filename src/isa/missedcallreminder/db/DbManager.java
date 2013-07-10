package isa.missedcallreminder.db;

import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.PHOTO;
import isa.missedcallreminder.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
			String nr) {
		ContentValues wartosci = new ContentValues();
		wartosci.put(PHOTO, photo);
		wartosci.put(NAZWA, nazwa);
		wartosci.put(NUMER, nr);
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
		Cursor kursor = myDb.query(tabela, null, null, null, null, null,
				"nazwa");
		activity.startManagingCursor(kursor);

		MyCursorAdapter myCursorAdapter = new MyCursorAdapter(context,
				R.layout.notification_layout, kursor, FROM, DO);
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

				holder.text = (TextView) convertView.findViewById(R.id.notification_main_txt);
				holder.text2 = (TextView) convertView.findViewById(R.id.notification_main2_txt);
				holder.buttonCall = (Button) convertView.findViewById(R.id.noti_callBtn);
				holder.buttonSms = (Button) convertView.findViewById(R.id.noti_smsBtn);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(cursor.getString(cursor.getColumnIndex("nazwa")));
			holder.text2.setText(cursor.getString(cursor.getColumnIndex("numer")));
			holder.buttonCall.setId(position);
			holder.buttonCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Cursor cursor = (Cursor) getItem(v.getId());
					Toast.makeText(context, cursor.getString(cursor.getColumnIndex("nazwa")), 0).show();
				}
			});
			holder.buttonSms.setId(position);
			holder.buttonSms.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Cursor cursor = (Cursor) getItem(v.getId());
					Toast.makeText(context, cursor.getString(cursor.getColumnIndex("numer")), 0).show();
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		TextView text;
		TextView text2;
		TextView text3;
		ImageView image;
		Button buttonCall;
		Button buttonSms;
	}
}
