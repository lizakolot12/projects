package proj.kolot.uzsearch.settings


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import proj.kolot.uzsearch.R


class SettingsActivity : AppCompatActivity() {


    companion object {

        // private val INTENT_USER_ID = "user_id"
        private val NAME_SETTINGS_FRAGMENT = "settings_fr"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setTitle(R.string.settings_data_title)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        changeFragment()
    }

    fun changeFragment() {
        val fm = fragmentManager
        var fragment = fm.findFragmentByTag(NAME_SETTINGS_FRAGMENT)

        if (fragment == null) {
            val ft = fragmentManager.beginTransaction();
            fragment = SettingsFragment()
            ft.replace(R.id.fragment, fragment, NAME_SETTINGS_FRAGMENT);
            ft.commit()
        }
    }
}