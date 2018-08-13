package proj.kolot.uzsearch.task.list;

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import proj.kolot.uzsearch.MainActivity
import proj.kolot.uzsearch.R.id
import proj.kolot.uzsearch.R.layout
import proj.kolot.uzsearch.R.string.title_activity_task_list
import proj.kolot.uzsearch.task.edit.EditTaskActivity


class TaskListActivity : MainActivity() {
    private var fragment: Fragment? = null

    companion object {
        private val NAME_FRAGMENT_TSK_LIST = "list_task"

        fun newIntent(context: Context): Intent {

            return Intent(context, TaskListActivity::class.java)
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        changeData()
    }

    private fun changeData(){
        if (fragment is TaskListFragment) {
            (fragment as TaskListFragment).changeData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_task_list);
        val toolbar: Toolbar = findViewById(id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        setTitle(title_activity_task_list)

        val fab: FloatingActionButton = findViewById(id.fab) as FloatingActionButton
        fab.setOnClickListener { _ ->
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
