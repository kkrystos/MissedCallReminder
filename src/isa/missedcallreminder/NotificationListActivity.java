package isa.missedcallreminder;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import isa.missedcallreminder.db.DataEvent;
import isa.missedcallreminder.db.DbManager;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public final class NotificationListActivity extends ListActivity implements OnItemClickListener {
	
	ListView lv;
	private static String[] FROM = { NAZWA, NUMER };
	private static int[] DO = { R.id.notification_main_txt, R.id.notification_main2_txt };
	DataEvent dataEvent;
	DbManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
//		dataEvent = new DataEvent(getApplicationContext());
		dbManager = new DbManager(getApplicationContext(), this);
		lv = getListView();

		dbManager.pobierzNieodebrane(NAZWA_TABELI_2, FROM, DO, lv);

	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        Toast.makeText(getApplicationContext(), "klik", 0).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		 Toast.makeText(getApplicationContext(), "klik", 0).show();
		
	}
}
