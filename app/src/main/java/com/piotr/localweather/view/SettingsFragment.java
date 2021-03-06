package com.piotr.localweather.view;

/**
 * @author piotr on 06.08.16.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.activeandroid.query.Select;
import com.piotr.localweather.R;
import com.piotr.localweather.model.City;

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";
    private static String pref_location_key;
    private static EditTextPreference cityEditTextPreference;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
        pref_location_key = getString(R.string.pref_location_key);
        cityEditTextPreference = (EditTextPreference) findPreference("settings_edit_city_name");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        updatePreference(pref_location_key);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        updatePreference(key);
    }

    private void updatePreference(String key) {
        if (key.equals(pref_location_key)) {
            Preference preference = findPreference(key);
            if (preference instanceof EditTextPreference) {
                if ((cityEditTextPreference.getText() != null) && (cityEditTextPreference.getText
                        ().trim().length() > 0)) {
                    cityEditTextPreference.setSummary(
                            getString(R.string.settings_location_city_summary,
                                    cityEditTextPreference.getText().toString()));
                } else {
                    try {
                        City city = new Select().from(City.class).executeSingle();
                        cityEditTextPreference.setSummary(getString(R.string
                                .settings_location_city_summary, city.getName()));
                    } catch (NullPointerException e) {
                        Log.e(TAG, "updatePreference: ", e.getCause());
                        cityEditTextPreference.setSummary(getString(R.string
                                .settings_location_city_summary, getString(R.string.pref_location_default)));
                    }
                }
            }
        }
    }
}