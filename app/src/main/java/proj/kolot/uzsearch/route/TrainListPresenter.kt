package proj.kolot.uzsearch.route

import android.os.AsyncTask
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.settings.RequestStorage
import proj.kolot.uzsearch.settings.SettingsStorage
import javax.inject.Inject

//https://habrahabr.ru/post/275255/
@InjectViewState
class TrainListPresenter : MvpPresenter<ListTrainView>() {
    @Inject
    lateinit var trainsProvider: TrainsProvider

    @Inject
    lateinit var requestStorage: RequestStorage

    private var settings: SettingsStorage.Settings? = null

    init {
        MainApplication.graph.inject(this)
    }


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.e("my test", " on first view attach trains presenter " + viewState)
      //  loadTrains()

    }

    fun loadTrains(id:Int) {
        Log.e("my test", " load trains presenter+ " + viewState)
        var err: Error;
        settings = requestStorage.getRequestById(id)
        var dateSearch: LocalDateTime? = settings?.dateRoute
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
            Log.e("my test", " on pre execute must begin to show progress")
            viewState.showProgress()
        }

        override fun doInBackground(vararg params: Void?): Response {
            Log.e("my test", " do in background load trains !")
            return trainsProvider.getTrains(settings as SettingsStorage.Settings)
        }

        override fun onPostExecute(result: Response) {
            viewState.hideProgress()
            viewState.showRouteName( "" + settings?.stationFrom?.name + " - " + settings?.stationTo?.name)
            if (result.list == null || result.list?.isEmpty() ?: true) {
                if (result?.message == null || result?.message?.isBlank() ?: false) {
                    viewState.showErrorMessage(Error.EMPTY_LIST)
                } else {
                    viewState.showErrorMessage(result?.message ?: "")
                }
            } else {
                viewState.hideErrorMessage()
                viewState.onTrainsLoaded(result.list as List<TransportRoute>)
            }
        }
    }





}
