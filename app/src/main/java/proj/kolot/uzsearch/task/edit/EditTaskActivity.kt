package proj.kolot.uzsearch.task.edit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import proj.kolot.uzsearch.R


class EditTaskActivity : AppCompatActivity() {


    companion object {

        // private val INTENT_USER_ID = "user_id"
        private val NAME_SETTINGS_FRAGMENT = "settings_fr"

        private val ARGUMENT_ID = "argument_id"

        fun newIntent(context: android.content.Context, id: Int): android.content.Intent {
            val intent = android.content.Intent(context, EditTaskActivity::class.java)
            intent.putExtra(EditTaskActivity.ARGUMENT_ID, id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("my test", " on create edittaskactivity")
        setContentView(R.layout.settings_activity)
        setTitle(proj.kolot.uzsearch.R.string.settings_data_title)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        changeFragment()
    }

    fun changeFragment() {
        val fm = fragmentManager
        var fragment = fm.findFragmentByTag(EditTaskActivity.NAME_SETTINGS_FRAGMENT)
        Log.e("my test", " fragment from fragment  manager = " + fragment)
        if (fragment == null) {
            val ft = fragmentManager.beginTransaction();
            fragment = EditTaskFragment.newIntent(intent.getIntExtra(ARGUMENT_ID, -1))
            ft.replace(R.id.fragment, fragment, EditTaskActivity.NAME_SETTINGS_FRAGMENT);
            ft.commit()
        }
    }

}