package proj.kolot.uzsearch.main

import android.util.Log
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.settings.SeatFilter
import proj.kolot.uzsearch.settings.SettingsStorage
import proj.kolot.uzsearch.utils.filterRoutes
import javax.inject.Inject

/**
 * Created by Kolot Liza on 3/22/18.
 */
class TrainsProvider {


    @Inject
    lateinit var settingsStorage: SettingsStorage
    @Inject
    lateinit var trainsSearcher: TrainsRouteSearcher

    private var settings: SettingsStorage.Settings? = null


    init {
        MainApplication.graph.inject(this)

    }

    fun getTrains():Response {
        settings = settingsStorage.loadSettings()
        var result:Response = trainsSearcher.getTrains(settings?.stationFrom ?: Station("", ""),
                settings?.stationTo ?: Station("", ""), settings?.dateRoute ?: LocalDateTime.now())
        var list:List<TransportRoute> = processingResult(result)
        return Response(result.message, list)
    }

    private fun processingResult(result: Response): List<TransportRoute> {
        val filters = settings?.seatFilters
        val mapFilters: Map<String, Int> = convertListToMap(filters)
        val list: List<TransportRoute>? = result.list
        Log.e("my test", " map filters size " + mapFilters.size)
        if (list == null) return emptyList() else return filterRoutes(list, mapFilters)
    }

    private fun convertListToMap(list: MutableList<SeatFilter>?): Map<String, Int> {
        var map: MutableMap<String, Int> = HashMap<String, Int>()
        list?.forEach {
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