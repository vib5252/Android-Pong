package org.project.pong;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PongPreferences extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
