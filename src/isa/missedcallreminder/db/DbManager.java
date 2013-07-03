package isa.missedcallreminder.db;

import static android.provider.BaseColumns._ID;
import static isa.missedcallreminder.db.Const.ID;
import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.TRESC_URI;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DbManager {
	DataEvent dataEvent;
	private Context context;
	
	public DbManager(Context ctx) {
		this.context = ctx;
		this.dataEvent = new DataEvent(ctx);
		
	}
	
    public void dodajZdarzenie(String id, String nazwa, String nr) {
        ContentValues wartosci = new ContentValues();
        wartosci.put(ID, id);
        wartosci.put(NAZWA, nazwa);
        wartosci.put(NUMER, nr);
        context.getContentResolver().insert(TRESC_URI, wartosci);
    }
    
		public void deleteTable(String tableName)
		{
			SQLiteDatabase db = dataEvent.getWritableDatabase();
			
//			for(int i= startId ; i< (startId+ilosc_lista); i++){
//			
//			db.delete(NAZWA_TABELI, "_id=?", new String[]{ 
//					""+i
//			});
//			}
			db.execSQL("DELETE FROM "+ tableName);
			
			dataEvent.close();
		}

}
