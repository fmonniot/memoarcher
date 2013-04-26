package eu.monniot.memoArcher.ui;

import java.util.List;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.R;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}
	
	private static void unbindPreferenceSummaryToValue(Preference preference) {
		
		preference.setOnPreferenceChangeListener(null);
	}

	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			populateDefaultBowList();
		}
		
		@Override
		public void onResume() {
		    super.onResume();
			bindPreferenceSummaryToValue(findPreference("pref_default_mark_unit"));
			bindPreferenceSummaryToValue(findPreference("pref_default_distance_unit"));
			bindPreferenceSummaryToValue(findPreference("pref_default_bow"));
		}

		@Override
		public void onPause() {
		    super.onPause();
			unbindPreferenceSummaryToValue(findPreference("pref_default_mark_unit"));
			unbindPreferenceSummaryToValue(findPreference("pref_default_distance_unit"));
			unbindPreferenceSummaryToValue(findPreference("pref_default_bow"));
		}
		
		private void populateDefaultBowList() {
		    List<Bow> list = Bow.all();
		    
			ListPreference defaultBowList = (ListPreference) findPreference("pref_default_bow");
		    CharSequence[] lEntries = new CharSequence[list.size()];
		    CharSequence[] lEntryValues = new CharSequence[list.size()];
		    int i = 0;
			for(Bow bow : list) {
				lEntries[i] = bow.name;
				lEntryValues[i] = String.valueOf(bow.getId());
				i++;
			}
			defaultBowList.setEntries(lEntries);
			defaultBowList.setEntryValues(lEntryValues);
		}
	}
}
