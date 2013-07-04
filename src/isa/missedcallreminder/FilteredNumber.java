package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.INITIALIZED_CHECKBOX;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import isa.missedcallreminder.db.DataEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class FilteredNumber extends PreferenceActivity implements OnClickListener, OnItemLongClickListener, OnPreferenceClickListener {
	
    private static String phoneNrPick = "";
    private static String contactName = "";
    private static String[] FROM = {  NAZWA , NUMER, ID, _ID, };
    private static String ORDER_BY = NAZWA;
    private static int[] DO = { R.id.nazwa, R.id.numer};
    private String locale;
    boolean hasCheckPref = true;
    CheckBox checkBoxAll;
    SharedPreferences myCheckPref;
    ListView listView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		locale = java.util.Locale.getDefault().getLanguage();
		
		myCheckPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		hasCheckPref = myCheckPref.getBoolean("filtered_checkbox", true);
		if (hasCheckPref) {
			Toast.makeText(getApplicationContext(), "CHECKED", 0).show();
		}else {
			Toast.makeText(getApplicationContext(), "UNCHECKED", 0).show();
		}
		

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
		CheckBoxPreference checkboxPreference = (CheckBoxPreference) findPreference("filtered_checkbox");
		checkboxPreference.setOnPreferenceClickListener(this);
		listView = (ListView) findViewById(R.id.list2);
		listView.setOnItemLongClickListener(this);
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        Cursor kursor = managedQuery(TRESC_URI, FROM, null, null, ORDER_BY);
        startManagingCursor(kursor);
        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_row, kursor, FROM, DO);
        listView.setAdapter(adapter);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v,
			int position, long id) {
		Cursor mycursor = (Cursor) listView.getItemAtPosition(position); 
		 final String myIdd = mycursor.getString(3);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
	       if(locale.equalsIgnoreCase("pl")){
	           
	    	   builder.setTitle("Usun kontakt").setMessage("Jestes pewien?").setPositiveButton("Tak", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	        	   delete(myIdd);
	 	        	   finish();
	 	        	   Intent i = new Intent(getApplicationContext(), FilteredNumber.class);
	 	        	   startActivity(i);
	 	           }
	 	       }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	                dialog.cancel();
	 	           }
	 	       }).show();
	       }
	       else{
	    	   builder.setTitle("Delete contact").setMessage("Are You sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	        	   delete(myIdd);
	 	        	   finish();
	 	        	   Intent i = new Intent(getApplicationContext(), FilteredNumber.class);
	 	        	   startActivity(i);
	 	           }
	 	       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	                dialog.cancel();
	 	           }
	 	       }).show();
	       }
		return false;
	}
	public void delete(String myId)
	{
		DataEvent de = new DataEvent(this);
		SQLiteDatabase db = de.getWritableDatabase();
		db.delete(NAZWA_TABELI, "_id=?", new String[]{ 
				myId
		});
	}

	public void onClick(View v) {

		switch(v.getId()){
    	case R.id.filtered_numberAddBtn:
 	       Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
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
	                    String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
	                    long idd = cur.getLong(cur.getColumnIndexOrThrow(ContactsContract.Contacts.Photo._ID));

	                    Cursor phoneCur = contect_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
	                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
	                    if (phoneCur.moveToFirst()) {
	                    	contactName = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	                        phoneNrPick = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                    }
	                    
	          		  String[] temp;
	          		  String delimiter = "-";
	          		  temp = phoneNrPick.split(delimiter);
	          		  StringBuilder sb = new StringBuilder();
	          		  for(int i =0; i < temp.length ; i++){
	          			  sb.append(temp[i]);
	          			  }
	                   dodajZdarzenie(sb.substring(sb.length()-9), contactName, "");
	  	        	   Intent i = new Intent(getApplicationContext(), FilteredNumber.class);
	 	        	   startActivity(i);
	                }
	                contect_resolver = null;
	                cur = null;
	                finish();
	            }
	            
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	            Log.e("IllegalArgumentException :: ", e.toString());
	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.e("Error :: ", e.toString());
	        }}
	 
	    public void dodajZdarzenie(String nr, String nazwa, String photo) {
	        ContentValues wartosci = new ContentValues();
	        wartosci.put(NUMER, nr);
	        wartosci.put(NAZWA, nazwa);
//	        wartosci.put(PHOTO, photo);
	        getContentResolver().insert(TRESC_URI, wartosci);
	        finish();
	    }

		@Override
		public boolean onPreferenceClick(Preference arg0) {
			// TODO Auto-generated method stub
			boolean hasCheckkPref = myCheckPref.getBoolean("filtered_checkbox", true);
			
			if (hasCheckkPref) {
				Toast.makeText(getApplicationContext(), "CHECKED", 0).show();
			}else {
				Toast.makeText(getApplicationContext(), "UNCHECKED", 0).show();
			}
			return false;
		}
}

