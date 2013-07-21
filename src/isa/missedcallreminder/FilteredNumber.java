package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.PHOTO;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import isa.missedcallreminder.db.DataEvent;
import isa.missedcallreminder.db.DbManager;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FilteredNumber extends PreferenceActivity implements
		OnClickListener, OnItemLongClickListener, OnPreferenceClickListener {

	private static String phoneNrPick = "";
	private static String contactName = "";
	private static Uri photoUri;
	private static String[] FROM = { NAZWA, NUMER, PHOTO, _ID, };
	private static String ORDER_BY = NAZWA;
	private static int[] DO = { R.id.nazwa, R.id.numer, R.id.img_contact };
	private String locale;
	private boolean hasCheckPref = true;
	private CheckBox checkBoxAll;
	private SharedPreferences myCheckPref;
	private ListView listView;
	private DbManager dbManager;
	private CheckBoxPreference checkboxPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		locale = java.util.Locale.getDefault().getLanguage();

		myCheckPref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		dbManager = new DbManager(getApplicationContext(), this);
		super.onCreate(savedInstanceState);
		if (locale.equalsIgnoreCase("pl")) {
			setContentView(R.layout.filtered_number);
			addPreferencesFromResource(R.xml.filtered_pref);
		} else {
			setContentView(R.layout.filtered_number);
		}
		Button dodajNr = (Button) findViewById(R.id.filtered_numberAddBtn);
		dodajNr.setOnClickListener(this);
		Button readyBtn = (Button) findViewById(R.id.filtered_numberBtn);
		readyBtn.setOnClickListener(this);
		checkboxPreference = (CheckBoxPreference) findPreference("filtered_checkbox");
		checkboxPreference.setOnPreferenceClickListener(this);
		listView = (ListView) findViewById(R.id.list2);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		hasCheckPref = myCheckPref.getBoolean("filtered_checkbox", true);
		loadContactListFromDB(listView);

		if (listView.getCount() == 0) {
			checkboxPreference.setChecked(true);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position,
			long id) {
		Cursor mycursor = (Cursor) listView.getItemAtPosition(position);
		final String myIdd = mycursor.getString(3);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		if (locale.equalsIgnoreCase("pl")) {

			builder.setTitle("Usun kontakt")
					.setMessage("Jestes pewien?")
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									delete(myIdd);
									finish();
									Intent i = new Intent(
											getApplicationContext(),
											FilteredNumber.class);
									startActivity(i);
								}
							})
					.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();
		} else {
			builder.setTitle("Delete contact")
					.setMessage("Are You sure?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									delete(myIdd);
									finish();
									Intent i = new Intent(
											getApplicationContext(),
											FilteredNumber.class);
									startActivity(i);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();
		}
		return false;
	}

	public void delete(String myId) {
		DataEvent de = new DataEvent(this);
		SQLiteDatabase db = de.getWritableDatabase();
		db.delete(NAZWA_TABELI, "_id=?", new String[] { myId });
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.filtered_numberAddBtn:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, 1);
			break;
		case R.id.filtered_numberBtn:
			finish();
			break;
		}
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		try {
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor cur = managedQuery(contactData, null, null, null, null);
				ContentResolver contect_resolver = getContentResolver();

				if (cur.moveToFirst()) {
					String id = cur
							.getString(cur
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
					long idd = cur
							.getLong(cur
									.getColumnIndexOrThrow(ContactsContract.Contacts.Photo._ID));
					Log.i("test", "contact id: " + id);
					Cursor phoneCur = contect_resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					if (phoneCur.moveToFirst()) {
						contactName = phoneCur
								.getString(phoneCur
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						phoneNrPick = phoneCur
								.getString(phoneCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						String photoId = phoneCur
								.getString(phoneCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
						if (photoId != null) {
							photoUri = ContentUris.withAppendedId(
									ContactsContract.Data.CONTENT_URI,
									Long.parseLong(photoId));
						} else {
							photoUri = null;
						}
					}
					phoneCur.close();
					String[] temp;
					String delimiter = "-";
					temp = phoneNrPick.replaceAll("\\s", "").split(delimiter);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < temp.length; i++) {
						sb.append(temp[i]);
					}
					if (sb.length() >= 9) {
						// if (photoUri != null) {
						// Log.i("test", "photoUri: "+photoUri);
						// // dodajZdarzenie(sb.substring(sb.length() -
						// 9),contactName, photoUri.toString());
						dodajZdarzenie(sb.substring(sb.length() - 9),
								contactName, id);
						// }else {
						// Log.i("test", "photoUri: "+"brakFoci");
						// dodajZdarzenie(sb.substring(sb.length() -
						// 9),contactName, ""+R.drawable.ic_contact_img);
						// }

						Intent i = new Intent(getApplicationContext(),
								FilteredNumber.class);
						startActivity(i);
						checkboxPreference.setChecked(false);
					}
				}
				contect_resolver = null;
				cur.close();
				finish();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.e("IllegalArgumentException :: ", e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error :: ", e.toString());
		}
	}

	public void dodajZdarzenie(String nr, String nazwa, String photo) {
		ContentValues wartosci = new ContentValues();
		wartosci.put(NUMER, nr);
		wartosci.put(NAZWA, nazwa);
		wartosci.put(PHOTO, photo);
		getContentResolver().insert(TRESC_URI, wartosci);
		finish();
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		boolean hasCheckkPref = myCheckPref.getBoolean("filtered_checkbox",
				true);

		if (hasCheckkPref) {
			checkboxPreference.setChecked(false);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Kasuj stworzon¹ listê")
					.setMessage("Kasowaæ wybrane numery i filtrowaæ wszystko?")
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dbManager.deleteTable(NAZWA_TABELI);
									finish();
									checkboxPreference.setChecked(true);
								}
							})
					.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							}).show();

		} else {
			if (listView.getCount() == 0) {
				checkboxPreference.setChecked(true);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Najpierw dodaj kontakt")
						.setMessage("Dodaæ kontakt do listy?")
						.setPositiveButton("Tak",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent intent = new Intent(
												Intent.ACTION_PICK,
												ContactsContract.Contacts.CONTENT_URI);
										startActivityForResult(intent, 1);
									}
								})
						.setNegativeButton("Nie",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								}).show();
			}
		}
		return false;
	}

	public void loadContactList(ListView listView) {
		ArrayList<String> arrayList = new ArrayList<String>();
		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

		String[] columns = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };
		ListAdapter mAdapter = new SimpleCursorAdapter(getApplicationContext(),
				R.layout.contact_row, cursor, columns, DO);
		listView.setAdapter(mAdapter);
	}

	// public void loadContactListFromDB(ListView listView) {
	// Cursor kursor = managedQuery(TRESC_URI, FROM, null, null, ORDER_BY);
	// startManagingCursor(kursor);
	// ListAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
	// R.layout.contact_row, kursor, FROM, DO);
	// listView.setAdapter(adapter);
	// }
	public void loadContactListFromDB(ListView listView) {
		Cursor kursor = managedQuery(TRESC_URI, FROM, null, null, ORDER_BY);
		startManagingCursor(kursor);
		MyCursorAdapter myCursorAdapter = new MyCursorAdapter(
				getApplicationContext(), R.layout.contact_row, kursor, FROM, DO);
		listView.setAdapter(myCursorAdapter);
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
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				convertView = inflater.inflate(R.layout.contact_row, parent,
						false);
				holder = new ViewHolder();
				holder.nazwaTv = (TextView) convertView
						.findViewById(R.id.nazwa);
				holder.numerTv = (TextView) convertView
						.findViewById(R.id.numer);
				holder.imageContact = (ImageView) convertView
						.findViewById(R.id.img_contact);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.nazwaTv.setText(cursor.getString(cursor
					.getColumnIndex(NAZWA)));
			holder.numerTv.setText(cursor.getString(cursor
					.getColumnIndex(NUMER)));
			if (!cursor.getString(cursor.getColumnIndex(PHOTO))
					.equalsIgnoreCase("")) {
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
				} else {
					holder.imageContact
							.setImageResource(R.drawable.ic_contact_img);
				}
			} else {
				holder.imageContact.setImageResource(R.drawable.ic_contact_img);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		TextView nazwaTv;
		TextView numerTv;
		ImageView imageContact;
	}
}
