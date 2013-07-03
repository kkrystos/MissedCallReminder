package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI;
import static isa.missedcallreminder.db.Const.NUMER;
import isa.missedcallreminder.db.DbManager;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class FilteredNumbers extends ListActivity implements OnClickListener {

	private ListView lv = null;
	private Cursor cursor = null;
	ArrayAdapter<String> adapter;
	ArrayList<String> arrayList;
	ArrayList<Contact> contacts;
	ArrayList<String> idArray;
	DbManager dbManager;
	String id;
	String nazwa;
	String numer;
	boolean check = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtered_numbers);
		lv = getListView();

		dbManager = new DbManager(getApplicationContext());

		pokazZdarzenia();

		Button readyBtn = (Button) findViewById(R.id.filtered_numbersBtn);
		Button allBtn = (Button) findViewById(R.id.filtered_numbers_allBtn);
		readyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Watek();
				finish();
			}
		});
		allBtn.setOnClickListener(this);

		cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		arrayList = new ArrayList<String>();
		contacts = new ArrayList<Contact>();
		while (cursor.moveToNext()) {
			String contactName = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String contactNumber = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			if (!arrayList.contains(contactName)) {
				arrayList.add(contactName);
				contacts.add(new Contact(contactName, contactNumber));
			}
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				android.R.id.text1, arrayList);

		this.setListAdapter(adapter);

		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		for (int i = 0; i < idArray.size(); i++) {
			lv.setItemChecked(Integer.parseInt(idArray.get(i)), true);
		}
	}

	private static String[] FROM = { _ID, ID, NAZWA, NUMER };
	private static String ORDER_BY = _ID + " DESC LIMIT 1";

	private void pokazZdarzenia() {
		Cursor kursor = managedQuery(
				Uri.parse("content://isa.missedcallreminder.db/zdarzenia"),
				FROM, null, null, ID);
		idArray = new ArrayList<String>();
		while (kursor.moveToNext()) {
			long idd = kursor.getLong(0);
			idArray.add(kursor.getString(1));
			nazwa = kursor.getString(2);
			numer = kursor.getString(3);

		}
	}

	class Contact {

		private String name;
		private String number;

		public Contact(String name, String number) {
			this.name = name;
			this.number = number;
		}

		public String getName() {
			return this.name;
		}

		public String getNumber() {
			return this.number;
		}
	}

	class Watek implements Runnable {
		Thread t;

		public Watek() {

			t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			dbManager.deleteTable(NAZWA_TABELI);
			int count = lv.getCount();
			SparseBooleanArray itemViews = lv.getCheckedItemPositions();
			for (int i = 0; i < count; i++) {
				if (itemViews.get(i)) {
					String name = contacts.get(i).getName();
					String number = contacts.get(i).getNumber();

					String[] temp;
					String delimiter = "-";
					temp = number.replaceAll("\\s", "").split(delimiter);
					StringBuilder sb = new StringBuilder();
					for (int ii = 0; ii < temp.length; ii++) {
						sb.append(temp[ii]);
					}
					if (sb.length() >= 9) {
						dbManager.dodajZdarzenie("" + i, name,
								sb.substring(sb.length() - 9));
					}
				}
			}
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("checkbox_" + position, true);
		editor.commit();
	}

	@Override
	public void onClick(View arg0) {
		if (!check) {
			for (int i = 0; i < lv.getCount(); i++) {
				lv.setItemChecked(i, true);
			}
			check = true;
		} else {
			for (int i = 0; i < lv.getCount(); i++) {
				lv.setItemChecked(i, false);
			}
			check = false;
		}

	}
}
