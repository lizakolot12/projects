package proj.kolot.uzsearch.task.list;

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import proj.kolot.uzsearch.R.id
import proj.kolot.uzsearch.R.layout
import proj.kolot.uzsearch.task.edit.EditTaskActivity


class TaskListActivity : AppCompatActivity() {
    private var fragment: Fragment? = null

    companion object {
        private val NAME_FRAGMENT_TSK_LIST = "list_task"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, TaskListActivity::class.java)

            return intent
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        changeData()
    }

    private fun changeData(){
        if (fragment is TaskListFragment) {

            Log.e("my test", " on new intent in task list activity ")
            (fragment as TaskListFragment).changeData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_task_list);
        val toolbar: Toolbar = findViewById(id.toolbar) as Toolbar
        setSupportActionBar(toolbar);

        val fab: FloatingActionButton = findViewById(id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            startActivity(EditTaskActivity.newIntent(baseContext, -1))
        }

        fragment = fragmentManager.findFragmentByTag(NAME_FRAGMENT_TSK_LIST)
        if (fragment == null) {
            val ft = fragmentManager.beginTransaction();
            fragment = TaskListFragment()
            ft.replace(id.fragment, fragment, NAME_FRAGMENT_TSK_LIST);
            ft.commit()
        }
    }

}
