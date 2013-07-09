package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class NotificationListActivity extends ListActivity{
	
	ListView lv;
	private static String[] FROM = { NAZWA, NUMER, ID, _ID, };
	private static int[] DO = { R.id.notification_main_txt, R.id.notification_main2_txt };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		
		lv = getListView();
		
		Cursor kursor = managedQuery(TRESC_URI, FROM, null, null, NAZWA);
		startManagingCursor(kursor);
		ListAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
				R.layout.notification_layout, kursor, FROM, DO);
		lv.setAdapter(adapter);
		
	}

}
