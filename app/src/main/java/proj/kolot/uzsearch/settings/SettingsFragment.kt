package proj.kolot.uzsearch.settings

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.item_filter_place.view.*
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.list.ContentActivity
import proj.kolot.uzsearch.utils.DelayAutoCompleteTextView


class SettingsFragment : MvpFragment(), SettingsView, DatePickerDialog.OnDateSetListener {

    private var unsavedSettings: SettingsStorage.Settings? = null //сделать чтоб это было неизменяемые даные а изменяться только через презентер
    private var filtersViews = ArrayList<ViewGroup>()

    @InjectPresenter (type = PresenterType.LOCAL, tag = "SettingsPresenter")
    lateinit var mPresenter: SettingsPresenter


    override fun showResult() {
        val intent = ContentActivity.newIntent(activity)
        startActivity(intent)
        //   https://habrahabr.ru/company/redmadrobot/blog/325816/
    }

    override fun showErrorInputData(list: List<Integer>) {
        var msg: String = ""
        list.forEach {
            when (it) {
                SettingsPresenter.ID_ERROR_STATION_FROM -> {
                    msg += getString(R.string.error_station_from) + "\n"
                    view.container_station_from.error = getString(R.string.error_station_from)
                }
                SettingsPresenter.ID_ERROR_STATION_TO -> {
                    msg += getString(R.string.error_station_to) + "\n"
                    view.container_station_to.error = getString(R.string.error_station_to)
                }
                SettingsPresenter.ID_ERROR_DATE -> {
                    msg += getString(R.string.error_date)
                }
            }
        }
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }


    override fun hideErrorInputData(tag: String) {
        when (tag) {
            SettingsPresenter.TAG_STATION_FROM -> {
                view.container_station_from.error = null
            }
            SettingsPresenter.TAG_STATION_TO -> {
                view.container_station_to.error = null
            }
        }
    }

    override fun setInitialDate() {
        // unsavedSettings?.dateRoute = inputDate
        var inputDate: LocalDateTime = unsavedSettings?.dateRoute ?: LocalDateTime.now()
        showDate.text = formatDataText(inputDate)
        showDate.setOnClickListener { showDataPicker(inputDate) }
    }


    override fun addLineFilterSeat(idFilter: Int) {
        Log.e("my test", " add line filterseat id = " + idFilter)
        var seatFilter: SeatFilter? = null
        unsavedSettings?.seatFilters?.forEach { if (it.id == idFilter) seatFilter = it }
        if (seatFilter == null) {
            seatFilter = SeatFilter(idFilter, null, 0)
        }
        val parent: ViewGroup = view.sets_of_filters
        val inflater = LayoutInflater.from(activity)
        val layout = inflater.inflate(R.layout.item_filter_place, null, false) as LinearLayout
        layout.id = idFilter
        var spinner: Spinner = layout.type_of_seat
        val adapter = ArrayAdapter.createFromResource(activity, R.array.type_seat, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        if ((seatFilter as SeatFilter).code != null) {
            val range = 0..adapter.count
            var setItem = 0
            for (i in range) {
                if (adapter.getItem(i).equals(seatFilter?.code)) {
                    setItem = i
                    break
                }
            }
            spinner.setSelection(setItem)
            layout.filter_number.setText(seatFilter?.amount.toString())
        }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                unsavedSettings?.seatFilters
                        ?.filter { it.id == idFilter }
                        ?.map {
                            it.code = parent.getItemAtPosition(position).toString()
                        }
            }// to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        /* spinner.setOnItemClickListener { parent, view, position, id -> unsavedSettings?.seatFilters
             ?.filter { it.id == idFilter }
             ?.map { it.code = spinner.selectedItem.toString() } }*/
        layout.btn_del_line.setOnClickListener {
            mPresenter.removeFilterLine(idFilter)

        }
        layout.filter_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                mPresenter.changeAmountFilter(idFilter, s.toString().toIntOrNull())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do something before Text Change
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do something while Text Change
            }
        })
        parent.addView(layout)
        filtersViews.add(layout)

    }

    override fun removeLineFilterSeat(id: Int) {
        val parent: ViewGroup = view.sets_of_filters
        var layout = filtersViews.find { it.id == id }
        parent.removeView(layout)
        filtersViews.remove(layout)
    }


    private fun getFilters(): MutableList<SeatFilter> {
        var result: MutableList<SeatFilter> = ArrayList()
        for (view in filtersViews) {
            var type = view.type_of_seat.adapter.getItem(view.type_of_seat.selectedItemPosition).toString()
            var amount = (view.filter_number.text.toString()).toIntOrNull()
            var id = view.id
            result.add(SeatFilter(id, type, amount))
        }
        return result
    }

    override fun setInitialSettings(settings: SettingsStorage.Settings) {
        unsavedSettings = settings
    }

    private fun showDataPicker(initialDate: LocalDateTime) {
        DatePickerDialog(activity, this, initialDate.year, initialDate.monthOfYear - 1, initialDate.dayOfMonth).show()
    }

    override fun setInitialStationFrom(station: Station) {
        // unsavedSettings?.stationFrom = station
        var container = view.container_station_from as TextInputLayout
        // container.set
        setAutoCompleteStation(view.station_from, view.progress_bar_from, station, SettingsPresenter.TAG_STATION_FROM)

    }

    override fun setInitialStationTo(station: Station) {
        // unsavedSettings?.stationTo = station
        setAutoCompleteStation(view.station_to, view.progress_bar_to, station, SettingsPresenter.TAG_STATION_TO)
    }

    private fun setAutoCompleteStation(textView: DelayAutoCompleteTextView, progressBar: ProgressBar, default: Station?, tag: String) {
        with(textView) {
            setText(default?.name)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    mPresenter.afterTextChanged(tag, s.toString())
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do something before Text Change
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // Do something while Text Change
                }
            })
            threshold = 2
            var initial: ArrayList<Station> = ArrayList()
            if (default != null) {
                initial.add(default)
            }
            var adapter = StationAutoCompleteAdapter(context, mPresenter, initial)
            setAdapter(adapter)
            setLoadingIndicator(progressBar)
            onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                val station = adapterView.getItemAtPosition(position) as Station
                mPresenter.onInputStation(tag, station)
            }
        }
    }

    private fun handlingInputStation(textView: DelayAutoCompleteTextView): Station {
        val adapter = textView?.adapter
        var station = if (adapter != null && adapter.count > 0) adapter.getItem(0) as Station else null
        if (textView.text.toString() != station?.name) {
            station?.name = textView.text.toString()
            station?.id = ""
        }
        return station ?: Station("", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  retainInstance = true


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        view.btn_add_line.setOnClickListener { mPresenter.addFilterLine() }
        view.search.setOnClickListener {
            val stationFrom = handlingInputStation(station_from)
            val stationTo = handlingInputStation(station_to)
            val periodicCheck: CheckBox = check_periodically
            var period = getPeriod(period.selectedItem as String)
            //var filter = (filter_number.text.toString()).toIntOrNull()
            var inputData: SettingsStorage.Settings = SettingsStorage.Settings(stationFrom, stationTo,
                    unsavedSettings?.dateRoute ?: LocalDateTime.now(), periodicCheck.isChecked, period, getFilters())
            mPresenter.handleInputData(inputData)

        }
        return view
    }

    override fun setInitialPeriodicCheck() {
        view.check_periodically.setOnCheckedChangeListener { _, isChecked ->
            mPresenter.onChangeCheckPeridicaly(isChecked)
        }
        view.check_periodically.isChecked = unsavedSettings?.needPeriodicCheck ?: false

    }

    override fun setInitialPeriod() {
        var needShowPeriod: Boolean = unsavedSettings?.needPeriodicCheck ?: false
        var visibility: Int = if (needShowPeriod) View.VISIBLE else View.INVISIBLE
        setSpinnerPeriod(view.period, unsavedSettings?.period ?: 0)
        view.period.visibility = visibility
    }


    private fun getPeriod(s: String): Long {
        var milliseconds: Long
        val hour: Float = when (s) {
            getString(R.string.period_half_hour) -> 0.5f
            getString(R.string.period_one_hour) -> 1f
            getString(R.string.period_two_hour) -> 2f
            getString(R.string.period_three_hour) -> 3f
            getString(R.string.period_one_day) -> 24f
            getString(R.string.period_one_week) -> 24 * 7f
            else -> 24f
        }
        milliseconds = (hour * 60 * 60 * 1000).toLong()
        return milliseconds
    }

    private fun getStringPeriod(period: Long): String {
        return when (period / (60 * 60 * 1000f)) {
            0.5f -> getString(R.string.period_half_hour)
            1f -> getString(R.string.period_one_hour)
            2f -> getString(R.string.period_two_hour)
            3f -> getString(R.string.period_three_hour)
            24f -> getString(R.string.period_one_day)
            24 * 7f -> getString(R.string.period_one_week)
            else -> getString(R.string.period_half_hour)
        }
    }

    private fun setSpinnerPeriod(spinner: Spinner, period: Long) {
        val periodName = getStringPeriod(period)
        val periods = arrayOf(getString(R.string.period_half_hour), getString(R.string.period_one_hour),
                getString(R.string.period_two_hour), getString(R.string.period_three_hour),
                getString(R.string.period_one_week))
        val index: Int = periods.indexOf(periodName)
        val adapter = ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, periods)
        spinner.adapter = adapter
        spinner.setSelection(index)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                mPresenter.onChangePeriod(getPeriod(spinner.selectedItem as String))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    fun formatNumber(number: Int): String {
        return if (number < 10) "0 $number" else number.toString()
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = LocalDateTime(year, month + 1, dayOfMonth, 0, 0, 0)
        mPresenter.onInputDate(date)
    }

    private fun formatDataText(initialDate: LocalDateTime): String {
        return "${formatNumber(initialDate.dayOfMonth)} / ${formatNumber(initialDate.monthOfYear)} / ${initialDate.year}"
    }
}

