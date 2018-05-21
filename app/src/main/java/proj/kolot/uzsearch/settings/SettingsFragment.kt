package proj.kolot.uzsearch.settings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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
import java.util.*


class SettingsFragment : MvpFragment(), SettingsView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var filtersViews = ArrayList<ViewGroup>()
    private var unsavedSettings: SettingsStorage.Settings? = null

    @InjectPresenter(type = PresenterType.LOCAL, tag = "SettingsPresenter")
    lateinit var presenter: SettingsPresenter


    override fun showResult() {
        val intent = ContentActivity.newIntent(activity)
        startActivity(intent)
        //   https://habrahabr.ru/company/redmadrobot/blog/325816/
    }

    override fun setInitialSettings(settings: SettingsStorage.Settings?) {
        this.unsavedSettings = settings
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
    override fun setInitialStationFrom() {
        var station: Station = unsavedSettings?.stationFrom ?: Station("", "")
        setAutoCompleteStation(view.station_from, view.progress_bar_from, station, SettingsPresenter.TAG_STATION_FROM)

    }
    override fun setInitialStationTo() {
        var station: Station = unsavedSettings?.stationTo ?: Station("", "")
        setAutoCompleteStation(view.station_to, view.progress_bar_to, station, SettingsPresenter.TAG_STATION_TO)
    }
    /*addTextChangedListener(object : TextWatcher {
                 override fun afterTextChanged(s: Editable) {
                     presenter.afterTextChanged(tag, s.toString(), getTag().toString())
                 }

                 override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                 }

                 override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                 }
             })*/
    private fun setAutoCompleteStation(textView: DelayAutoCompleteTextView, progressBar: ProgressBar, station: Station?, tag: String) {
        with(textView) {
            setText(station?.name)
            threshold = 2
            var initial: ArrayList<Station> = ArrayList()
            if (station != null) {
                initial.add(station)
            }
            var adapter = StationAutoCompleteAdapter(context, presenter, initial)
            setAdapter(adapter)
            setLoadingIndicator(progressBar)
            onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                val station = adapterView.getItemAtPosition(position) as Station
                presenter.onInputStation(tag, station)
            }
        }
    }

    override fun setInitialDate() {
        var inputDate: LocalDateTime = unsavedSettings?.dateRoute ?: LocalDateTime.now()
        showDate.text = formatDataText(inputDate)
        showDate.setOnClickListener { showDataPicker(inputDate) }
    }

    override fun setInitialTime() {
        var inputDate: LocalDateTime = unsavedSettings?.dateRoute ?: LocalDateTime.now()
        showTimeDialog.text = formatTimeText(inputDate)
        showTimeDialog.setOnClickListener { showTimePicker() }
    }

    private fun formatTimeText(initialDate: LocalDateTime): String {
        return "${formatNumber(initialDate.hourOfDay)} : ${formatNumber(initialDate.minuteOfHour)}"

    }

    private fun showTimePicker() {
        val dateAndTime = Calendar.getInstance()
        TimePickerDialog(activity, this,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        presenter.onInputTime(hourOfDay, minute)
    }
    private fun formatDataText(initialDate: LocalDateTime): String {
        return "${formatNumber(initialDate.dayOfMonth)} / ${formatNumber(initialDate.monthOfYear)} / ${initialDate.year}"
    }
    private fun showDataPicker(initialDate: LocalDateTime) {
        DatePickerDialog(activity, this, initialDate.year, initialDate.monthOfYear - 1, initialDate.dayOfMonth).show()
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        presenter.onInputDate(year, month + 1, dayOfMonth)
    }

    override fun addLineFilterSeat(id: Int, seatFilter: SeatFilter) {
        val parent: ViewGroup = view.sets_of_filters
        val inflater = LayoutInflater.from(activity)
        val layout = inflater.inflate(R.layout.item_filter_place, null, false) as RelativeLayout

        var idSeatFilter =  id
        layout.id = idSeatFilter
        var spinner: Spinner = layout.type_of_seat
        val adapter = ArrayAdapter.createFromResource(activity, R.array.type_seat, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        if (seatFilter.code != null) {
            val range = 0..adapter.count - 1
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
                presenter.changeFilterCode(idSeatFilter, parent.getItemAtPosition(position).toString())

            }// to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        /* spinner.setOnItemClickListener { parent, view, position, id -> unsavedSettings?.seatFilters
             ?.filter { it.id == idFilter }
             ?.map { it.code = spinner.selectedItem.toString() } }*/
        layout.btn_del_line.setOnClickListener {
            presenter.removeFilterLine(idSeatFilter)

        }
        layout.filter_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                presenter.changeAmountFilter(layout.id, s.toString().toIntOrNull())
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


    private fun handlingInputStation(textView: DelayAutoCompleteTextView, tag: String) {
        var textField = textView.text.toString()
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
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  retainInstance = true


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        view.btn_add_line.setOnClickListener { presenter.addFilterLine() }
        view.search.setOnClickListener {
            with (presenter){
                afterTextChanged(SettingsPresenter.TAG_STATION_FROM, station_from.text.toString())
                afterTextChanged(SettingsPresenter.TAG_STATION_TO, station_to.text.toString())
                handleInputData()
            }


        }

        view.train_number_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                presenter.changeTrainNumberValue(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do something before Text Change
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do something while Text Change
            }
        })
        return view
    }

    override fun setInitialPeriodicCheck() {
        var initialCheck:Boolean = unsavedSettings?.needPeriodicCheck?:false
        view.check_periodically.setOnCheckedChangeListener { _, isChecked ->
            presenter.onChangeCheckPeridicaly(isChecked)
        }
        view.check_periodically.isChecked = initialCheck

    }

    override fun setInitialPeriod() {
        var needShowPeriod:Boolean = unsavedSettings?.needPeriodicCheck?:false
        var currentPeriod:Long = unsavedSettings?.period?:0
        var visibility: Int = if (needShowPeriod) View.VISIBLE else View.INVISIBLE
        setSpinnerPeriod(view.period, currentPeriod)
        view.period.visibility = visibility
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
                presenter.onChangePeriod(getPeriod(spinner.selectedItem as String))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
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



    fun formatNumber(number: Int): String {
        return if (number < 10) "0 $number" else number.toString()
    }






}

