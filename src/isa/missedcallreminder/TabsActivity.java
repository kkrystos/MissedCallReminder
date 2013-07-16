package isa.missedcallreminder;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);

		tabHost = getTabHost();
		TabHost.TabSpec spec1;
		TabHost.TabSpec spec2;
		TabHost.TabSpec spec3;
		Intent intentS1 = new Intent(this, MainPreferenceActivity.class);

		spec1 = tabHost.newTabSpec("Ogólne").setIndicator("Ogólne")
				.setContent(intentS1);
		tabHost.addTab(spec1);

		Intent intentS2 = new Intent(this, IndividualPreferenceCallActivity.class);

		spec2 = tabHost.newTabSpec("Po³¹czenia").setIndicator("Po³¹czenia")
				.setContent(intentS2);
		tabHost.addTab(spec2);

		Intent intentS3 = new Intent(this, IndividualPreferenceActivity.class);

		spec3 = tabHost.newTabSpec("Sms-y").setIndicator("Sms-y")
				.setContent(intentS3);
		tabHost.addTab(spec3);

	}

}
