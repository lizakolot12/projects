package proj.kolot.uzsearch.settings


import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.main.SearchRepeater
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import proj.kolot.uzsearch.main.TrainsRouteSearcher
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {


    companion object {
        val ID_ERROR_STATION_FROM: Integer = Integer(0)
        val ID_ERROR_STATION_TO: Integer = Integer(1)
        val ID_ERROR_DATE: Integer = Integer(2)
        val TAG_STATION_FROM: String = "from"
        val TAG_STATION_TO: String = "to"
        var idFiltersBlock = 1
    }

    @Inject
    lateinit var settingsStorage: SettingsStorage
    @Inject
    lateinit var routeSearcher: TrainsRouteSearcher


    var unsavedSettings: SettingsStorage.Settings? = null

    init {
        MainApplication.graph.inject(this)

    }


    fun onInputDate(date: LocalDateTime) {
        unsavedSettings?.dateRoute = date
        viewState.setInitialDate()
    }

    fun onInputStation(tagStation: String, station: Station) {
        when (tagStation) {
            TAG_STATION_FROM -> {
                viewState.setInitialStationFrom(station)
                unsavedSettings?.stationFrom = station
                Log.e("my test", " init id " + station.id)
            }
            TAG_STATION_TO -> {
                viewState.setInitialStationTo(station)
                unsavedSettings?.stationTo = station
            }
        }
    }

    fun onInputFilterSeats(filter: Int?) {
        // unsavedSettings?.filterNumSeat = filter
    }

    fun onChangeCheckPeridicaly(check: Boolean) {
        unsavedSettings?.needPeriodicCheck = check
        viewState.setInitialPeriod()

    }

    fun onChangePeriod(period: Long?) {
        unsavedSettings?.period = period
    }

    override fun attachView(view: SettingsView?) {
        super.attachView(view)
        Log.e("my test", " on attach view ")
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.e("my test", "restore state = " + isInRestoreState(viewState))
        initView()

        Log.e("my test", " on first view attach")
    }

    private fun initView() {
        unsavedSettings = settingsStorage.loadSettings()
        viewState.setInitialSettings(unsavedSettings as SettingsStorage.Settings)
        viewState.setInitialStationFrom(unsavedSettings?.stationFrom ?: Station("", ""))
        viewState.setInitialStationTo(unsavedSettings?.stationTo ?: Station("", ""))

        var initialDate: LocalDateTime = LocalDateTime.now()
        if (unsavedSettings?.dateRoute != null) {
            initialDate = unsavedSettings?.dateRoute as LocalDateTime
            unsavedSettings?.dateRoute = initialDate
        }
        viewState.setInitialDate()
        viewState.setInitialPeriodicCheck()
        viewState.setInitialPeriod()
        setInitialFilterNumSeat()

    }

    private fun setInitialFilterNumSeat() {
        var list: List<SeatFilter> = unsavedSettings?.seatFilters ?: emptyList()

        if (list.size > 0) {
            list.forEach {

                viewState.addLineFilterSeat(it.id ?: 0)
            }
        }

    }

    fun addFilterLine() {
        var id = idFiltersBlock++
        viewState.addLineFilterSeat(id)
        unsavedSettings?.seatFilters?.add(SeatFilter(id, null, 0))
    }

    fun changeAmountFilter(id: Int, amount: Int?) {
        unsavedSettings?.seatFilters?.filter { it.id == id }?.map { it.amount = amount }
    }

    fun removeFilterLine(id: Int) {
        viewState.removeLineFilterSeat(id)
        var element: SeatFilter? = unsavedSettings?.seatFilters?.find { it.id == id }
        unsavedSettings?.seatFilters?.remove(element)

    }

    fun findStations(nameStation: String): List<Station> {
        return routeSearcher.findStations(nameStation)
    }

    fun handleInputData(input: SettingsStorage.Settings) {
        var errors: ArrayList<Integer> = java.util.ArrayList()
        if (input.stationFrom?.name.isNullOrBlank() || input.stationFrom?.id.isNullOrBlank()) errors.add(ID_ERROR_STATION_FROM)
        if (input.stationTo?.name.isNullOrBlank() || input.stationTo?.id.isNullOrBlank()) errors.add(ID_ERROR_STATION_TO)
        if (input.dateRoute == null) errors.add(ID_ERROR_DATE)
        if (errors.size > 0) {
            viewState.showErrorInputData(errors)
            return
        }
        settingsStorage.saveSettings(input)
        viewState.showResult()

        runService()
    }

    private fun runService() {
        var repeater: SearchRepeater = SearchRepeaterImpl()
        repeater.runRepeatingTask(settingsStorage.needPeriodically(), settingsStorage.getPeriod())

    }

    fun afterTextChanged(tag: String, newText: String) {
        when (tag) {
            SettingsPresenter.TAG_STATION_FROM -> {
                unsavedSettings?.stationFrom?.name = newText
                unsavedSettings?.stationFrom?.id = ""
                Log.e("my test", " init id balnk")
            }
            SettingsPresenter.TAG_STATION_TO -> {
                unsavedSettings?.stationTo?.name = newText
                unsavedSettings?.stationTo?.id = ""
            }

        }
        viewState.hideErrorInputData(tag)
    }
}