package isa.missedcallreminder.db;

import android.provider.BaseColumns;

import android.net.Uri;

public interface Const extends BaseColumns {
    public static final String NAZWA_TABELI = "zdarzenia" ;
    public static final String NAZWA_TABELI_2 = "nieodebrane" ;
    
    public static final String URZAD = "isa.missedcallreminder.db" ;
    public static final Uri TRESC_URI = Uri.parse("content://"
        + URZAD + "/" + NAZWA_TABELI);
    public static final Uri TRESC_URI_2 = Uri.parse("content://"
    		+ URZAD + "/" + NAZWA_TABELI_2);


    // Kolumny w bazie danych Zdarzenia
    public static final String ID = "id" ;
    public static final String PHOTO = "photo" ;
    public static final String NUMER = "numer" ;
    public static final String NAZWA = "nazwa" ;
    public static final String INITIALIZED_CHECKBOX = "initialized_checkbox" ;
}