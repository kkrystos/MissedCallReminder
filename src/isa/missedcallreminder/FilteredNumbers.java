package isa.missedcallreminder;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FilteredNumbers extends ListActivity {

	private ListView lv = null;
	private Cursor cursor = null;
	private int idCol = -1;
	private int nameCol = -1;
	private int notesCol = -1;
	ArrayAdapter<String> adapter;
	ArrayList<String> arrayList;
	ArrayList<Contact> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtered_numbers);
		lv = getListView();

		Button readyBtn = (Button) findViewById(R.id.filtered_numbersBtn);
		Button allBtn = (Button) findViewById(R.id.filtered_numbers_allBtn);
		readyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = lv.getCount();
				SparseBooleanArray itemViews = lv.getCheckedItemPositions();
				for (int i = 0; i < count; i++) {
					if (itemViews.get(i)) {
						String name = contacts.get(i).getName();
						String number = contacts.get(i).getNumber();
						Toast.makeText(getApplicationContext(),name + "\n" + number + "\n" + i, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		allBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					for (int i = 0; i < lv.getCount(); i++) {
						lv.setItemChecked(i, true);
					}
			}
		});

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
		lv.setItemChecked(1, true);
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("checkbox_" + position, true);
		editor.commit();

		// SparseBooleanArray itemViews = lv.getCheckedItemPositions();
		// if(itemViews.get(position)){
		//
		//
		// Toast.makeText(getApplicationContext(), "true",
		// Toast.LENGTH_SHORT).show();
		// }
		// else{
		// Toast.makeText(getApplicationContext(), "false",
		// Toast.LENGTH_SHORT).show();
		// }
	}
}
