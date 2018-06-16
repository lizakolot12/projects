package proj.kolot.uzsearch.list

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.settings.RequestStorage
import proj.kolot.uzsearch.settings.SettingsStorage
import javax.inject.Inject

/**
 * Created by Kolot Liza on 6/6/18.
 */
@InjectViewState
class TasksPresenter : MvpPresenter<TasksView>() {

    @Inject
    lateinit var requestStorage: RequestStorage

    init {
        MainApplication.graph.inject(this)

    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.e("my test", " on first view attach trains presenter " + viewState)
        loadTasks()

    }

    private fun loadTasks() {

        // singleton move to Dagger
        viewState.showTasks(requestStorage.getAllRequest())
    }

    fun  clickItem(settings: SettingsStorage.Settings) {
        viewState.openDetail(settings.id?:-1)
    }


}