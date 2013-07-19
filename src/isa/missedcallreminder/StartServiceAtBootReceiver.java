package isa.missedcallreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StartServiceAtBootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean isMissiedCall = getPrefs.getBoolean("missCallCheck", true);
		boolean isMissedSMS = getPrefs.getBoolean("missSMSCheck", true);
		boolean isActive = getPrefs.getBoolean("autostart", true);
		if (isActive == true) {
			if (isMissiedCall == true) {
				context.startService(new Intent(context, ServiceCall.class));
			} 
			if (isMissedSMS == true) {
				context.startService(new Intent(context, ServiceSMS.class));
			} 
		}
	}
}
