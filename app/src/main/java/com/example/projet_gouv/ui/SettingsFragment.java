package com.example.projet_gouv.ui;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.projet_gouv.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
