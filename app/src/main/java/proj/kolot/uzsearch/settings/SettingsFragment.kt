package proj.kolot.uzsearch.settings

import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import proj.kolot.uzsearch.R


class SettingsFragment : PreferenceFragment() {
    private var ringtonePreference: RingtonePreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        ringtonePreference = findPreference(getString(R.string.pref_key_uri_ringtone)) as RingtonePreference


        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        val typeSound = prefs.getString(getString(R.string.pref_key_type_ringtone), "")
        setRingtoneEnabled(typeSound)
        val sound = prefs.getString(getString(R.string.pref_key_uri_ringtone), getString(R.string.pref_type_sound_melody))
        setRingtoneSummary(sound)
        val listPreference = findPreference(getString(R.string.pref_key_type_ringtone)) as ListPreference

        listPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->

            setRingtoneEnabled(newValue.toString())
            true
        }


        ringtonePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            setRingtoneSummary(newValue = newValue.toString())
            true
        }
    }

    private fun setRingtoneEnabled(value: String) {
        ringtonePreference?.isEnabled = value.equals(getString(R.string.pref_type_sound_melody))
    }

    private fun setRingtoneSummary(newValue: String) {
        val ringtone = RingtoneManager.getRingtone(
                activity, Uri.parse(newValue))
        ringtonePreference?.summary = ringtone.getTitle(activity)
    }
}
