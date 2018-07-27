package proj.kolot.uzsearch.task.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter.createFromResource
import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import kotlinx.android.synthetic.main.item_filter_place.view.*
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.R.*
import proj.kolot.uzsearch.R.layout.item_filter_place
import proj.kolot.uzsearch.data.SeatFilter
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.route.RouteActivity
import proj.kolot.uzsearch.task.list.TaskListActivity
import proj.kolot.uzsearch.utils.DelayAutoCompleteTextView
import java.util.*


class EditTaskFragment : MvpFragment(), EditTaskView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var filtersViews = ArrayList<ViewGroup>()
    private var unsavedTask: Task? = null

    @InjectPresenter(type = PresenterType.LOCAL, tag = "EditTaskPresenter")
    lateinit var presenter: EditTaskPresenter

    companion object {
        private val ARGUMENT_ID = "argument_id_route"

        fun newIntent(id: Int): EditTaskFragment {
            val fragment: EditTaskFragment = EditTaskFragment()
            val arg: Bundle = Bundle()
            arg.putInt(EditTaskFragment.ARGUMENT_ID, id)
            fragment.arguments = arg
            return fragment
        }
    }


    override fun showResult(id: Int) {
        val intent = RouteActivity.newIntent(activity, id)
        startActivity(intent)
        activity.finish()
        Log.e("my test", " show result for button search")
    }

    override fun showAllTask() {
        activity.finish()
        val intent = TaskListActivity.newIntent(activity)
        startActivity(intent)

        Log.e("my test", " show all task in edit task fragment")
    }

    override fun setInitialSettings(task: Task?) {
        this.unsavedTask = task
    }

    override fun setInitialFilterTrainsNumber() {
        train_number_value.setText(unsavedTask?.numberTrain)
    }

    override fun showErrorInputData(list: List<Integer>) {
        var msg: String = ""
        list.forEach {
            when (it) {
                EditTaskPresenter.ID_ERROR_STATION_FROM -> {
                    msg += getString(string.error_station_from) + "\n"
                    container_station_from.error = getString(string.error_station_from)
                }
                EditTaskPresenter.ID_ERROR_STATION_TO -> {
                    msg += getString(string.error_station_to) + "\n"
                    container_station_to.error = getString(string.error_station_to)
                }
                EditTaskPresenter.ID_ERROR_DATE -> {
                    msg += getString(string.error_date)
                }
            }
        }
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }


    override fun hideErrorInputData(tag: String) {
        when (tag) {
            EditTaskPresenter.TAG_STATION_FROM -> {
                container_station_from.error = null
            }
            EditTaskPresenter.TAG_STATION_TO -> {
                container_station_to.error = null
            }
        }
    }

    override fun setInitialStationFrom() {
        var station: Station = unsavedTask?.stationFrom ?: Station("", "")
        setAutoCompleteStation(station_from, progress_bar_from, station, EditTaskPresenter.TAG_STATION_FROM)

    }

    override fun setInitialStationTo() {
        var station: Station = unsavedTask?.stationTo ?: Station("", "")
        setAutoCompleteStation(station_to, progress_bar_to, station, EditTaskPresenter.TAG_STATION_TO)
    }


    private fun setAutoCompleteStation(textView: DelayAutoCompleteTextView, progressBar: ProgressBar, station: Station?, tag: String) {
        with(textView) {
            setText(station?.name)
            threshold = 2
            var initial: java.util.ArrayList<Station> = ArrayList()
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
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    presenter.afterTextChanged(tag, s.toString())
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    override fun setInitialDate() {
        var inputDate: LocalDateTime = unsavedTask?.dateRoute ?: LocalDateTime.now()
        showDate.text = formatDataText(inputDate)
        showDate.setOnClickListener { showDataPicker(inputDate) }
    }

    override fun setInitialTime() {
        var inputDate: LocalDateTime = unsavedTask?.dateRoute ?: LocalDateTime.now()
        showTimeDialog.text = formatTimeText(inputDate)
        showTimeDialog.setOnClickListener { showTimePicker() }
    }

    private fun formatTimeText(initialDate: LocalDateTime): String {
        return "${formatNumber(initialDate.hourOfDay)} : ${formatNumber(initialDate.minuteOfHour)}"

    }

    private fun showTimePicker() {
        val dateAndTime = Calendar.getInstance()
        TimePickerDialog(activity, this,
                dateAndTime.get(java.util.Calendar.HOUR_OF_DAY),
                dateAndTime.get(java.util.Calendar.MINUTE), true)
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
        val parent: ViewGroup = sets_of_filters
        val inflater = from(activity)
        val layout = inflater.inflate(item_filter_place, null, false) as RelativeLayout

        var idSeatFilter = id
        layout.id = idSeatFilter
        var spinner: Spinner = layout.type_of_seat
        val adapter = createFromResource(activity, array.type_seat, android.R.layout.simple_spinner_item)
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
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                presenter.changeFilterCode(idSeatFilter, parent.getItemAtPosition(position).toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

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
        val parent: android.view.ViewGroup = sets_of_filters
        var layout = filtersViews.find { it.id == id }
        parent.removeView(layout)
        filtersViews.remove(layout)
    }


    private fun handlingInputStation(textView: DelayAutoCompleteTextView, tag: String) {
        var textField = textView.text.toString()
        when (tag) {
            EditTaskPresenter.TAG_STATION_FROM -> {
                if (!textField.equals(unsavedTask?.stationFrom?.name)) {
                    unsavedTask?.stationFrom?.name = textField
                    unsavedTask?.stationFrom?.id = ""
                }
                android.util.Log.e("my test", " init id balnk")
            }
            EditTaskPresenter.TAG_STATION_TO -> {
                if (!textField.equals(unsavedTask?.stationTo?.name)) {
                    unsavedTask?.stationTo?.name = textField
                    unsavedTask?.stationTo?.id = ""
                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        Log.e("my test", " on create edit task fragment")


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layout.settings_fragment, container, false)
        Log.e("my test", " on create view edit task fragment")
        presenter.setTaskId(arguments.getInt(EditTaskFragment.ARGUMENT_ID))
        view.btn_add_line.setOnClickListener { presenter.addFilterLine() }
        view.search.setOnClickListener {
            with(presenter) {
                afterTextChanged(EditTaskPresenter.TAG_STATION_FROM, station_from.text.toString())
                afterTextChanged(EditTaskPresenter.TAG_STATION_TO, station_to.text.toString())
                searchTrains()
            }
        }

        view.save.setOnClickListener {
            with(presenter) {
                afterTextChanged(EditTaskPresenter.TAG_STATION_FROM, station_from.text.toString())
                afterTextChanged(EditTaskPresenter.TAG_STATION_TO, station_to.text.toString())
                saveTrainsInfo()
            }
        }





        view.train_number_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: android.text.Editable) {
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
        var initialCheck: Boolean = unsavedTask?.needPeriodicCheck ?: false
        check_periodically.setOnCheckedChangeListener { _, isChecked ->
            presenter.onChangeCheckPeridicaly(isChecked)
        }
        check_periodically.isChecked = initialCheck

    }

    override fun setInitialPeriod() {
        var needShowPeriod: Boolean = unsavedTask?.needPeriodicCheck ?: false
        var currentPeriod: Long = unsavedTask?.period ?: 0
        var visibility: Int = if (needShowPeriod) android.view.View.VISIBLE else android.view.View.INVISIBLE
        setSpinnerPeriod(period, currentPeriod)
        period.visibility = visibility
    }

    private fun setSpinnerPeriod(spinner: android.widget.Spinner, period: Long) {
        val periodName = getStringPeriod(period)
        val periods = arrayOf(getString(string.period_half_hour), getString(string.period_one_hour),
                getString(string.period_two_hour), getString(string.period_three_hour),
                getString(string.period_one_week))
        val index: Int = periods.indexOf(periodName)
        val adapter = android.widget.ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, periods)
        spinner.adapter = adapter
        spinner.setSelection(index)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            getString(string.period_half_hour) -> 0.5f
            getString(string.period_one_hour) -> 1f
            getString(string.period_two_hour) -> 2f
            getString(string.period_three_hour) -> 3f
            getString(string.period_one_day) -> 24f
            getString(string.period_one_week) -> 24 * 7f
            else -> 24f
        }
        milliseconds = (hour * 60 * 60 * 1000).toLong()
        return milliseconds
    }

    private fun getStringPeriod(period: Long): String {
        return when (period / (60 * 60 * 1000f)) {
            0.5f -> getString(string.period_half_hour)
            1f -> getString(string.period_one_hour)
            2f -> getString(string.period_two_hour)
            3f -> getString(string.period_three_hour)
            24f -> getString(string.period_one_day)
            24 * 7f -> getString(string.period_one_week)
            else -> getString(string.period_half_hour)
        }
    }


    fun formatNumber(number: Int): String {
        return if (number < 10) "0 $number" else number.toString()
    }


}

