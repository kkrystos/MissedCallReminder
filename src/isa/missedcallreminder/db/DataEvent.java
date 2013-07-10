package isa.missedcallreminder.db;



import static isa.missedcallreminder.db.Const.ID;

import static isa.missedcallreminder.db.Const.NAZWA;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI;
import static isa.missedcallreminder.db.Const.NAZWA_TABELI_2;
import static isa.missedcallreminder.db.Const.NUMER;
import static isa.missedcallreminder.db.Const.PHOTO;
import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataEvent extends SQLiteOpenHelper {
    private static final String NAZWA_BAZY_DANYCH = "contacts.db" ;
    private static final int WERSJA_BAZY_DANYCH = 2;

    /** Tworzy obiekt pomocniczy dla bazy Zdarzenia */
    public DataEvent(Context ktks) {
        super(ktks, NAZWA_BAZY_DANYCH, null, WERSJA_BAZY_DANYCH);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE " + NAZWA_TABELI + " (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ ID
            + " TEXT," + NUMER + " TEXT," + NAZWA + " TEXT );" );
        bd.execSQL("CREATE TABLE " + NAZWA_TABELI_2 + " (" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ PHOTO
                + " TEXT," + NUMER + " TEXT," + NAZWA + " TEXT );" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int staraWersja,
            int nowaWersja) {
        bd.execSQL("DROP TABLE IF EXISTS " + NAZWA_TABELI);
        bd.execSQL("DROP TABLE IF EXISTS " + NAZWA_TABELI_2);
        onCreate(bd);
    }
    
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
    	// TODO Auto-generated method stub
    	return super.getWritableDatabase();
    }
    
}
