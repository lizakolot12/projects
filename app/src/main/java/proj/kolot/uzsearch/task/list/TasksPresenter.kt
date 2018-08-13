package proj.kolot.uzsearch.task.list

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.main.SearchRepeater
import proj.kolot.uzsearch.storage.Storage
import java.util.*
import javax.inject.Inject

/**
 * Created by Kolot Liza on 6/6/18.
 */

@InjectViewState
class TasksPresenter : MvpPresenter<TasksView>() {

    @Inject
    lateinit var requestStorage: Storage
    @Inject
    lateinit var repeater: SearchRepeater
    private var list:MutableList<Task>? = null

    private var needUpdateData: Boolean = false
    private val observer: Observer = Observer { o, arg ->
        needUpdateData = true
    }

    init {
        MainApplication.graph.inject(this)

    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        requestStorage.addObserver( observer)
        loadTasks()

    }

    override fun onDestroy() {
        requestStorage.deleteObserver(observer)
        super.onDestroy()
    }
    fun clickRunItem(task:Task){
        task.needPeriodicCheck = !task.needPeriodicCheck
        if (task.period?:-1 < 0){
            task.period = SearchRepeater.DEFAULT_PERIOD_REPEATING
        }
        requestStorage.updateRequest(task)
        repeater.runRepeatingTask(task.id?:-1, task.needPeriodicCheck, task.period ?:-1)
    }

    fun loadTasks() {
        needUpdateData = false
        list = requestStorage.getAllRequest().toMutableList()
        viewState.showTasks(list?: emptyList())
    }

    fun  clickItem(task: Task) {
        viewState.openDetail(task.id?:-1)
    }

    fun  clickLong(task: Task) {
        requestStorage.delete(task)
        list?.remove(task)
        repeater.runRepeatingTask(task.id?:-1, false, 0)

    }

    private fun needUpdate(): Boolean {
        var result: Boolean = needUpdateData
        return needUpdateData
    }

    fun resume() {
        if (needUpdate()) {
            loadTasks()
        }
    }


}