package proj.kolot.uzsearch.list


import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.settings.SettingsActivity

class ContentActivity : AppCompatActivity() {

    private var fragment: Fragment? = null

    companion object {
        private val NAME_FRAGMENT_CONTENT = " content_fr"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, ContentActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            startActivity(SettingsActivity.newIntent(baseContext))
        }
        changeFragment()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (fragment is ListTrainFragment) {
            (fragment as ListTrainFragment).changeData()
        }
    }

    fun changeFragment() {
       fragment = fragmentManager.findFragmentByTag(NAME_FRAGMENT_CONTENT)
        if (fragment == null) {
            val ft = fragmentManager.beginTransaction();
            fragment = ListTrainFragment()
            /*    ft.setCustomAnimations(
                        R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit);*/
            ft.replace(R.id.fragment, fragment, NAME_FRAGMENT_CONTENT);
            ft.commit()
        }

    }

}
