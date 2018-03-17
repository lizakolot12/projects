package proj.kolot.uzsearch.list

import android.os.AsyncTask
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.main.TrainsRouteSearcher
import proj.kolot.uzsearch.settings.SeatFilter
import proj.kolot.uzsearch.settings.SettingsStorage
import proj.kolot.uzsearch.utils.filterRoutes
import javax.inject.Inject

//https://habrahabr.ru/post/275255/
@InjectViewState
class TrainListPresenter : MvpPresenter<ListTrainView>() {

    @Inject
    lateinit var trainsSearcher: TrainsRouteSearcher

    @Inject
    lateinit var settingsStorage: SettingsStorage

    init {
        MainApplication.graph.inject(this)
    }


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.e("my test", " on first view attach trains presenter " + viewState)
        loadTrains()

    }

    fun loadTrains() {
        Log.e("my test", " load trains presenter+ " + viewState)
        var err: Error;
        var dateSearch: LocalDateTime? = settingsStorage.loadSettings().dateRoute
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
        lateinit private var settings:SettingsStorage.Settings
        override fun onPreExecute() {
            super.onPreExecute()
            Log.e("my test", " on pre execute must begin to show progress")
            viewState.showProgress()
        }

        override fun doInBackground(vararg params: Void?): Response {
            Log.e("my test", " do in background load trains !")
            settings = settingsStorage.loadSettings()
            var result = trainsSearcher.getTrains(settings.stationFrom ?: Station("", ""),
                    settings.stationTo ?: Station("", ""), settings.dateRoute ?: LocalDateTime.now())
            return result
        }

        override fun onPostExecute(result: Response?) {
            viewState.hideProgress()
            viewState.showRouteName( "" + settings.stationFrom?.name + " - " + settings.stationTo?.name)
            if (result?.list == null || result.list?.isEmpty() ?: true) {
                viewState.showErrorMessage(result?.message ?: "")
            } else {
                viewState.hideErrorMessage()
                viewState.onTrainsLoaded(processingResult(result))
            }
        }
    }


    private fun processingResult(result: Response): List<TransportRoute> {
        val filters = settingsStorage.getFilters()
        val mapFilters: Map<String, Int> = convertListToMap(filters)
        val list:List<TransportRoute> ? = result.list
        Log.e("my test", " map filters size " + mapFilters.size)
        if (list== null) return emptyList() else return filterRoutes(list, mapFilters)


    }

    private fun convertListToMap(list: List<SeatFilter>): Map<String, Int> {
        var map: MutableMap<String, Int> = HashMap<String, Int>()
        list.forEach {
            if (it.code != null) {
                var currentAmount: Int? = map.get(it.code as String)
                var newAmount: Int = it.amount ?: 0
                if (currentAmount != null && currentAmount < newAmount) {
                    newAmount = currentAmount
                }
                map.put(it.code as String, newAmount)
            }
        }
        return map
    }

}
