package proj.kolot.uzsearch.list;

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_task_list.*
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.route.ContentActivity
import proj.kolot.uzsearch.settings.SettingsStorage

class TaskListFragment: MvpFragment(), TasksView{

    @InjectPresenter(type = PresenterType.LOCAL, tag = "TasksPresenter")
    lateinit var presenter: TasksPresenter

    private val taskListView: RecyclerView by lazy {
        tasks.setHasFixedSize(true) // <-- Lazy executed!
        tasks.layoutManager = LinearLayoutManager(activity)
        var adapter:TaskAdapter = TaskAdapter(emptyList())
        adapter.setOnItemTaskClickListener(object:TaskAdapter.OnItemTaskClickListener{
            override fun onClick(settings: SettingsStorage.Settings) {
                presenter.clickItem(settings)
            }

        })
        tasks.adapter = adapter

        tasks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View= inflater.inflate(R.layout.fragment_task_list, container, false)
        //taskListView.addOnItemTouchListener(OnIte) //remove listener
        return view
    }

    override fun showTasks(settings: List<SettingsStorage.Settings>) {

        var adapter: TaskAdapter = taskListView.adapter as TaskAdapter;
        adapter.setList(settings)
        Log.e("my test", " update view tasks total = " + settings.size)
        taskListView.adapter.notifyDataSetChanged()

    }

    override fun openDetail(id:Int) {
        val intent = ContentActivity.newIntent(activity, id)
        startActivity(intent)
    }



}

