package proj.kolot.uzsearch.route

import android.os.AsyncTask
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.storage.Storage
import javax.inject.Inject


@InjectViewState
class RoutePresenter : MvpPresenter<RouteView>() {
    @Inject
    lateinit var trainsProvider: TrainsProvider

    @Inject
    lateinit var requestStorage: Storage

    private var task: Task? = null
    private var taskId:Int? = 0
    private var routes:List<TransportRoute> = mutableListOf()

    init {
        MainApplication.graph.inject(this)
    }



    fun  editRoute(id: Int) {
        viewState.showEditRoute(id)
    }


    private fun loadTrains() {
        var err: Error;
        task = requestStorage.getRequestById(taskId?:-1)
        viewState.showRouteName( "" + task?.stationFrom?.name + " - " + task?.stationTo?.name)
        var dateSearch: LocalDateTime? = task?.dateRoute
        when {
            (dateSearch == null) -> {
                err = Error.NOT_SAVED_DATA
                viewState.showErrorMessage(err)
            }
            (isExpiredDate(dateSearch)) -> {
                err = Error.DATE_EXPIRED
                viewState.showErrorMessage(err)
            }
            else -> AsyncTaskExample().execute();
        }
    }

    private fun isExpiredDate(date: LocalDateTime): Boolean {
        val limitDate: LocalDateTime = LocalDateTime.now().minusDays(1)
        return date < limitDate
    }


    inner class AsyncTaskExample : AsyncTask<Void, Void, Response>() {

        override fun onPreExecute() {
            super.onPreExecute()
            viewState.showProgress()
        }

        override fun doInBackground(vararg params: Void?): Response {
            return trainsProvider.getTrains(task as Task)
        }

        override fun onPostExecute(result: Response?) {
            viewState.hideProgress()

            if (result?.list == null || result.list?.isEmpty() ?: true) {
                if (result?.message == null || result.message?.isBlank() ?: false) {
                    viewState.showErrorMessage(Error.EMPTY_LIST)
                } else {
                    viewState.showErrorMessage(result.message ?: "")
                }
            } else {
                viewState.hideErrorMessage()
                routes = result.list as List<TransportRoute>
                viewState.onTrainsLoaded(routes)
            }
        }
    }

    fun showRoutes(id:Int) {
      if( taskId != id){
          taskId = id
          loadTrains()
      }else {
          viewState.onTrainsLoaded(routes)
      }
    }

    fun  changeData(id: Int) {
        taskId = id
        loadTrains()
    }


    fun onItemClick(transportRoute: TransportRoute) {
        viewState.showTrain(transportRoute)
    }


}
