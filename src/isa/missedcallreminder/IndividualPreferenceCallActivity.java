package isa.missedcallreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class IndividualPreferenceCallActivity extends PreferenceActivity implements OnPreferenceClickListener{
    
   private boolean bool = false;
   private String callNumber;
   private AlarmManager am;
   private Intent i;
   private PendingIntent pi;
   private String locale;
          @Override
          protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  
                  locale = java.util.Locale.getDefault().getLanguage();
                 
                 if(locale.equalsIgnoreCase("pl")){
                     
                     addPreferencesFromResource(R.xml.indiv_pref_call);
                 }
                 else{
                     addPreferencesFromResource(R.xml.indiv_pref_call_en);
                 }

          }
		public boolean onPreferenceClick(Preference preference) {
			// TODO Auto-generated method stub
			return false;
		}
}
