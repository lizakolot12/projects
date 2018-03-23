package proj.kolot.uzsearch.settings

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Station


class SettingsStorage {
    val PREFS_FILENAME = "proj.kolot.uzsearch.prefs"
    val PREF_STATION_FROM_NAME = "stationFrom_name"
    val PREF_STATION_FROM_ID = "stationFrom_id"
    val PREF_STATION_TO_NAME = "stationTo_name"
    val PREF_STATION_TO_ID = "stationTo_id"
    val PREF_DATE = "date"
    val PREF_NEED_PERIODICALLY = "need_periodically"
    val PREF_TIME_PERIODICALLY = "time_periodically"
    val PREF_FILTER_NUMBER_OF_SEAT = "filter_number_of_seat"
    val dividerItem: String = "//"
    val dividerWords: String = ":"

    companion object {
        val LOCALE_NEUTRAL_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")
        var count:Int = 0
    }

    private var context: Context = MainApplication.instance.baseContext
    private var prefs: SharedPreferences

    constructor() {
        prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
        Log.e("my test", " create settings storage " + count++)
    }


    fun getPeriod(): Long {
        return prefs.getLong(PREF_TIME_PERIODICALLY, 0)
    }

    fun saveSettings(settings: Settings) {


        val editor = prefs.edit()
        editor.putString(PREF_STATION_FROM_ID, settings.stationFrom?.id)
        editor.putString(PREF_STATION_FROM_NAME, settings.stationFrom?.name)

        editor.putString(PREF_STATION_TO_ID, settings.stationTo?.id)
        editor.putString(PREF_STATION_TO_NAME, settings.stationTo?.name)

        editor.putString(PREF_DATE, LOCALE_NEUTRAL_DATE_TIME_FORMATTER.print(settings.dateRoute))

        editor.putBoolean(PREF_NEED_PERIODICALLY, settings.needPeriodicCheck)
        editor.putLong(PREF_TIME_PERIODICALLY, settings.period ?: 0)
        editor.putString(PREF_FILTER_NUMBER_OF_SEAT, getStringFiltersForSave(settings.seatFilters))
        editor.apply()
    }

    private fun getStringFiltersForSave(list: List<SeatFilter>?): String {

        var result: String = ""
        list?.forEach {
            result += dividerItem +it.code + dividerWords + it.amount
            Log.e("my test", result)
        }
        return result
    }

    private fun getFiltersFromSavedString(str: String): MutableList<SeatFilter> {
        var result: MutableList<SeatFilter> = ArrayList<SeatFilter>()
        val list = str.split(dividerItem)
        for (str in list) {
            var listItem = str.split(dividerWords)
            if (listItem.size < 2) continue
            var seatType = SeatFilter(0, listItem.get(0), listItem.get(1).toIntOrNull())
            result.add(seatType)
        }
        return result
    }


    fun needPeriodically(): Boolean {
        return prefs.getBoolean(PREF_NEED_PERIODICALLY, false)
    }


    fun loadSettings(): Settings {
        val nameFrom = prefs.getString(PREF_STATION_FROM_NAME, null)
        val idFrom = prefs.getString(PREF_STATION_FROM_ID, null)
        val nameTo = prefs.getString(PREF_STATION_TO_NAME, null)
        val idTo = prefs.getString(PREF_STATION_TO_ID, null)

        val stationFrom = Station(nameFrom, idFrom)
        val stationTo = Station(nameTo, idTo)
        val date = prefs.getString(PREF_DATE, null)

        val dateNew: LocalDateTime? = if (date != null)
            LOCALE_NEUTRAL_DATE_TIME_FORMATTER.parseLocalDateTime(date)
        else null


        val needPeriodicCheck = prefs.getBoolean(PREF_NEED_PERIODICALLY, false)
        val period = prefs.getLong(PREF_TIME_PERIODICALLY, 0)
        val filter = getFiltersFromSavedString(prefs.getString(PREF_FILTER_NUMBER_OF_SEAT, ""))

        return Settings(stationFrom, stationTo, dateNew, needPeriodicCheck, period, filter
        )
    }


    data class Settings(
            var stationFrom: Station?,
            var stationTo: Station?,
            var dateRoute: LocalDateTime?,
            var needPeriodicCheck: Boolean,
            var period: Long?,
            var seatFilters: MutableList<SeatFilter>?

    )


}

