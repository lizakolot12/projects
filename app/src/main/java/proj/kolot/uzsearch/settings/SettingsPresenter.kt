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
    }

    @Inject
    lateinit var settingsStorage: SettingsStorage
    @Inject
    lateinit var routeSearcher: TrainsRouteSearcher

    private var idSeatFilter: Int = 0

    private var unsavedSettings: SettingsStorage.Settings? = null

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
                unsavedSettings?.stationFrom = station
                viewState.setInitialStationFrom()
                Log.e("my test", " init id " + station.id)
            }
            TAG_STATION_TO -> {
                unsavedSettings?.stationTo = station
                viewState.setInitialStationTo()
            }
        }
    }

    fun onChangeCheckPeridicaly(check: Boolean) {
        unsavedSettings?.needPeriodicCheck = check
        if (!check) {
            unsavedSettings?.period = 0
        }
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
        unsavedSettings = settingsStorage.loadSettings()
        initView()
        Log.e("my test", " on first view attach" + "restore state = " + isInRestoreState(viewState))
    }
//    private fun getStationFrom():Station{
//        return unsavedSettings?.stationFrom ?: Station("", "")
//    }
//    private fun getStationTo():Station{return unsavedSettings?.stationTo ?: Station("", "")}
//
//    private fun getInitialDate():LocalDateTime {return  unsavedSettings?.dateRoute ?: LocalDateTime.now()}
//
//    private fun getInitialPeriodicCheck():Boolean{return  unsavedSettings?.needPeriodicCheck ?: false}
//
//    private fun getPeriod():Long{return unsavedSettings?.period ?: 0}

    private fun getInitialFilterSeats(): MutableList<SeatFilter>? {
        var list: MutableList<SeatFilter>? = unsavedSettings?.seatFilters
        list?.forEach {
            it.id = getIdSeatFilter()
        }
        return list
    }

    private fun initView() {
        viewState.setInitialSettings(unsavedSettings)
        viewState.setInitialStationFrom()
        viewState.setInitialStationTo()
        viewState.setInitialDate()
        viewState.setInitialPeriodicCheck()
        viewState.setInitialPeriod()
        setInitialFilterNumSeat()

    }


    private fun setInitialFilterNumSeat() {
        var list: MutableList<SeatFilter>? = getInitialFilterSeats()
        list?.forEach {
            viewState.addLineFilterSeat(it.id?:getIdSeatFilter(), it)
        }
    }

    fun addFilterLine() {
        var newSeatFilter = SeatFilter(getIdSeatFilter(), "", 0)
        unsavedSettings?.seatFilters?.add(newSeatFilter)
        viewState.addLineFilterSeat(newSeatFilter.id?:getIdSeatFilter(), newSeatFilter)
    }

    private fun getIdSeatFilter(): Int {
        return idSeatFilter++;
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

    fun handleInputData() {
        var errors: ArrayList<Integer> = java.util.ArrayList()
        if (unsavedSettings?.stationFrom?.name.isNullOrBlank() || unsavedSettings?.stationFrom?.id.isNullOrBlank()) errors.add(ID_ERROR_STATION_FROM)
        if (unsavedSettings?.stationTo?.name.isNullOrBlank() || unsavedSettings?.stationTo?.id.isNullOrBlank()) errors.add(ID_ERROR_STATION_TO)
        if (unsavedSettings?.dateRoute == null) errors.add(ID_ERROR_DATE)
        if (errors.size > 0) {
            viewState.showErrorInputData(errors)
            return
        }
        if (unsavedSettings != null) {
            settingsStorage.saveSettings(unsavedSettings as SettingsStorage.Settings)
        }
        viewState.showResult()

        runService()
    }

    private fun runService() {
        var repeater: SearchRepeater = SearchRepeaterImpl()
        repeater.runRepeatingTask(settingsStorage.needPeriodically(), settingsStorage.getPeriod())

    }

    fun afterTextChanged(tag: String, textField: String) {
        when (tag) {
            SettingsPresenter.TAG_STATION_FROM -> {
                if (!textField.equals(unsavedSettings?.stationFrom?.name)) {
                    unsavedSettings?.stationFrom?.name = textField
                    unsavedSettings?.stationFrom?.id = ""
                }
                Log.e("my test", " init id balnk")
            }
            SettingsPresenter.TAG_STATION_TO -> {
                if (!textField.equals(unsavedSettings?.stationTo?.name)) {
                    unsavedSettings?.stationTo?.name = textField
                    unsavedSettings?.stationTo?.id = ""
                }
            }}
        viewState.hideErrorInputData(tag)
    }

    fun changeFilterCode(id: Int, code: String) {
        unsavedSettings?.seatFilters?.filter { it.id == id }?.map { it.code = code }
    }


}