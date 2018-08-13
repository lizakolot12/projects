package proj.kolot.uzsearch.route


import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import proj.kolot.uzsearch.MainActivity
import proj.kolot.uzsearch.R


class RouteActivity : MainActivity() {

    private var fragment: Fragment? = null

    companion object {
        private const val NAME_FRAGMENT_CONTENT = " content_fr"
        private const val ARGUMENT_ID = "argument_id"

        fun newIntent(context: Context, id: Int): Intent {
            val intent = Intent(context, RouteActivity::class.java)
            intent.putExtra(ARGUMENT_ID, id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        changeFragment()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        changeData(intent?.getIntExtra(ARGUMENT_ID, -1))

    }

    private fun changeData(id: Int?) {
        if (fragment is RouteFragment) {
            (fragment as RouteFragment).changeData(id ?: -1)
        }
    }

    fun changeFragment() {
        val ft = fragmentManager.beginTransaction()
        fragment = RouteFragment.newIntent(intent.getIntExtra(ARGUMENT_ID, -1))
        /*    ft.setCustomAnimations(
                    R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit);*/
        ft.replace(R.id.fragment, fragment, NAME_FRAGMENT_CONTENT);
        ft.commit()
    }


}

