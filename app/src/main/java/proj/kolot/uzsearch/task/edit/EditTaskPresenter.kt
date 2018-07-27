package proj.kolot.uzsearch.task.edit

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.SeatFilter
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.main.SearchRepeater
import proj.kolot.uzsearch.main.TrainsRouteSearcher
import proj.kolot.uzsearch.storage.Storage
import javax.inject.Inject


@InjectViewState
class EditTaskPresenter : MvpPresenter<EditTaskView>() {


    companion object {
        val ID_ERROR_STATION_FROM: Integer = Integer(0)
        val ID_ERROR_STATION_TO: Integer = Integer(1)
        val ID_ERROR_DATE: Integer = Integer(2)
        val TAG_STATION_FROM: String = "from"
        val TAG_STATION_TO: String = "to"
    }

    @Inject
    lateinit var storage: Storage
    @Inject
    lateinit var routeSearcher: TrainsRouteSearcher

    @Inject
    lateinit var requestStorage: Storage
    @Inject
    lateinit var repeater: SearchRepeater


    private var idSeatFilter: Int = 0

    private var unsavedTask: Task? = null
    private var taskId:Int? = 0


    init {
        MainApplication.graph.inject(this)

    }
    fun setTaskId(id:Int?){
        this.taskId = id
    }


    fun onInputDate( year: Int, month: Int, dayOfMonth: Int) {
        var curDateRoute: LocalDateTime = unsavedTask?.dateRoute ?: LocalDateTime()
        var newDateRoute: LocalDateTime = LocalDateTime(year, month, dayOfMonth, curDateRoute.hourOfDay, curDateRoute.minuteOfHour)
        unsavedTask?.dateRoute = newDateRoute
        viewState.setInitialDate()
    }
    fun onInputTime(  hourOfDay: Int, minute: Int) {
        var curDateRoute: LocalDateTime = unsavedTask?.dateRoute ?: LocalDateTime()
        var newDateRoute: LocalDateTime = LocalDateTime(curDateRoute.year, curDateRoute.monthOfYear, curDateRoute.dayOfMonth, hourOfDay, minute)
        unsavedTask?.dateRoute = newDateRoute
        viewState.setInitialTime()
    }

    fun onInputStation(tagStation: String, station: proj.kolot.uzsearch.data.Station) {
        when (tagStation) {
            EditTaskPresenter.TAG_STATION_FROM -> {
                unsavedTask?.stationFrom = station
                viewState.setInitialStationFrom()
                android.util.Log.e("my test", " init id " + station.id)
            }
            EditTaskPresenter.TAG_STATION_TO -> {
                unsavedTask?.stationTo = station
                viewState.setInitialStationTo()
            }
        }
    }

    fun onChangeCheckPeridicaly(check: Boolean) {
        unsavedTask?.needPeriodicCheck = check
        if (!check) {
            unsavedTask?.period = 0
        }
        viewState.setInitialPeriod()

    }

    fun onChangePeriod(period: Long?) {
        unsavedTask?.period = period
    }

    override fun attachView(view: EditTaskView?) {
        super.attachView(view)
        Log.e("my test", " on attach view ")
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.e("my test", " on first view attach" + "restore state = " + isInRestoreState(viewState))
        initView(taskId?:-1)
    }

    private fun initView(routeId: Int) {
        if (unsavedTask!=null){
            return
        }
        if (routeId > -1) {
            unsavedTask = requestStorage.getRequestById(routeId)
        } else {
            unsavedTask = getDefaultTask()
        }
        initView()
    }

    private fun getDefaultTask(): Task? {
        var date: LocalDateTime = LocalDateTime.now()

        return Task(-1, Station("", ""), Station("", ""), "", LocalDateTime(date.year, date.monthOfYear, date.dayOfMonth, 0, 0, 0), false, 0, null)
    }

    private fun getInitialFilterSeats(): MutableList<SeatFilter>? {
        var list: MutableList<SeatFilter>? = unsavedTask?.seatFilters

        return list
    }

    private fun initView() {
        viewState.setInitialSettings(unsavedTask)
        viewState.setInitialStationFrom()
        viewState.setInitialStationTo()
        viewState.setInitialDate()
        viewState.setInitialTime()
        viewState.setInitialPeriodicCheck()
        viewState.setInitialPeriod()
        viewState.setInitialFilterTrainsNumber()
        setInitialFilterNumSeat()

    }


    private fun setInitialFilterNumSeat() {
        var list: MutableList<SeatFilter>? = getInitialFilterSeats()
        list?.forEach {
            viewState.addLineFilterSeat(it.tempId, it)
        }
    }

    fun addFilterLine() {
        var tempId = getIdSeatFilter()
        var id = -1// for item not saved db
        var newSeatFilter = SeatFilter(id, tempId, "", 0)
        unsavedTask?.seatFilters?.add(newSeatFilter)
        viewState.addLineFilterSeat(newSeatFilter.tempId, newSeatFilter)
    }

    private fun getIdSeatFilter(): Int {
        var id = 0
        do {
            id = idSeatFilter++
        } while (unsavedTask?.seatFilters?.filter { it.id == id }?.size ?: 0 > 0)
        return id
    }

    fun changeAmountFilter(id: Int, amount: Int?) {
        unsavedTask?.seatFilters?.filter { it.tempId == id }?.map { it.amount = amount }
    }

    fun removeFilterLine(id: Int) {
        viewState.removeLineFilterSeat(id)
        var element: SeatFilter? = unsavedTask?.seatFilters?.find { it.id == id }
        unsavedTask?.seatFilters?.remove(element)

    }

    fun findStations(nameStation: String): List<proj.kolot.uzsearch.data.Station> {
        return routeSearcher.findStations(nameStation)
    }

    private fun handleInputData(): Int {
        var errors: ArrayList<Integer> = java.util.ArrayList()
        if (unsavedTask?.stationFrom?.name.isNullOrBlank() || unsavedTask?.stationFrom?.id.isNullOrBlank()) errors.add(EditTaskPresenter.ID_ERROR_STATION_FROM)
        if (unsavedTask?.stationTo?.name.isNullOrBlank() || unsavedTask?.stationTo?.id.isNullOrBlank()) errors.add(EditTaskPresenter.ID_ERROR_STATION_TO)
        if (unsavedTask?.dateRoute == null) errors.add(EditTaskPresenter.ID_ERROR_DATE)
        if (errors.size > 0) {
            viewState.showErrorInputData(errors)
            return -1
        }
        var result: Int = -1
        if (unsavedTask != null) {
            result = if (unsavedTask?.id ?: -1 > -1) {
                requestStorage.updateRequest(unsavedTask as Task) ?: unsavedTask?.id ?: 0
            } else {
                requestStorage.createRequest(unsavedTask as Task)
            }

        }
        return result


    }

    fun searchTrains() {
        val id = handleInputData()
        if (id > -1) {
            runService(id)
            viewState.showResult(id)
        }
    }

    fun saveTrainsInfo() {
        if (handleInputData() > 0) {
            viewState.showAllTask()
        }
    }

    private fun runService(id:Int) {

        repeater.runRepeatingTask(id, unsavedTask?.needPeriodicCheck ?: false, unsavedTask?.period ?: 0)

    }

    fun afterTextChanged(tag: String, textField: String) {
        when (tag) {
            EditTaskPresenter.TAG_STATION_FROM -> {
                if (!textField.equals(unsavedTask?.stationFrom?.name)) {
                    unsavedTask?.stationFrom?.name = textField
                    unsavedTask?.stationFrom?.id = ""
                }
            }
            EditTaskPresenter.TAG_STATION_TO -> {
                if (!textField.equals(unsavedTask?.stationTo?.name)) {
                    unsavedTask?.stationTo?.name = textField
                    unsavedTask?.stationTo?.id = ""
                }
            }}
        viewState.hideErrorInputData(tag)
    }

    fun changeFilterCode(id: Int, code: String) {
        unsavedTask?.seatFilters?.filter { it.tempId == id }?.map { it.code = code }
    }




    fun changeTrainNumberValue(toString: String) {
        unsavedTask?.numberTrain = toString
    }
}