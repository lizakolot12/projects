package proj.kolot.uzsearch.task.list;

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.fragment_task_list.*
import proj.kolot.uzsearch.R.layout.fragment_task_list
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.route.RouteActivity




class TaskListFragment: MvpFragment(), TasksView {

    @InjectPresenter(type = PresenterType.LOCAL, tag = "TasksPresenter")
    lateinit var presenter: TasksPresenter

    private val taskListView: RecyclerView by lazy {
        tasks.setHasFixedSize(true) // <-- Lazy executed!
        tasks.layoutManager = LinearLayoutManager(activity)
        var adapter: TaskAdapter = TaskAdapter(emptyList())
        adapter.setOnItemTaskClickListener(object: TaskAdapter.OnItemTaskClickListener {
            override fun onItemClick(task: Task) {
                presenter.clickItem(task)
            }

            override fun onItemLongClick(task: Task) {
                presenter.clickLong(task)

            }

        })
        adapter.setBtnRunClickListener(object :TaskAdapter.OnRunTaskClickListener{
            override fun onClick(task: Task) {
               presenter.clickRunItem(task)
            }
        })
        tasks.adapter = adapter

        tasks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(fragment_task_list, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()

    }
    override fun showTasks(tasks: List<Task>) {
        var adapter: TaskAdapter = taskListView.adapter as TaskAdapter;
        val taskDiffUtilCallback = TaskDiffUtilCallback(adapter.getList(), tasks)
        val taskDiffResult = DiffUtil.calculateDiff(taskDiffUtilCallback)

        adapter.setList(tasks)
        taskDiffResult.dispatchUpdatesTo(adapter)

    }

    override fun openDetail(id:Int) {
        val intent = RouteActivity.newIntent(activity, id)
        startActivity(intent)
    }

    fun changeData() {
       presenter.loadTasks()
    }


}

