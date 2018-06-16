package proj.kolot.uzsearch.settings

import android.util.Log
import com.j256.ormlite.dao.ForeignCollection
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.list.db.*

/**
 * Created by Kolot Liza on 6/1/18.
 */
class RequestStorage {
    var routeDAO: RouteDAO? = HelperFactory.helper?.getRouteDAO()
    var filterDAO: FilterDAO? = HelperFactory.helper?.getFilterDAO()

    fun createRequest(route: SettingsStorage.Settings): Int {
        var routeRequest = Route(route.stationFrom?.id,
                route.stationFrom?.name,
                route.stationTo?.id,
                route.stationTo?.name,
                route.numberTrain,
                route.dateRoute?.toDate(),
                route.needPeriodicCheck,
                route.period)
        var id1: Int? = routeDAO?.create(routeRequest)
        Log.e("my test", " id save " + id1 + "     " + routeRequest.id)
        route.seatFilters?.forEach {
            filterDAO?.create(ItemSeatFilter(it.code, it.amount, routeRequest))
        }
        return routeRequest.id
    }

    fun getRequestByNeedPeriodic(needPeriodic: Boolean): List<SettingsStorage.Settings> {
        Log.e("my test", " request data from data base by need periodic " + needPeriodic)
        var result: MutableList<SettingsStorage.Settings> = mutableListOf()

        routeDAO?.getRoutesByNeedPeriodic(needPeriodic)?.forEach {
            result.add(transformSettingFromRoute(it))
        }
        return result
    }

    fun getAllRequest(): List<SettingsStorage.Settings> {

        Log.e("my test", " request data from data base")
        var result: MutableList<SettingsStorage.Settings> = mutableListOf()

        routeDAO?.getAllRoutes()?.forEach {
            result.add(transformSettingFromRoute(it))
        }
        return result
    }

    private fun transformSettingFromRoute(it: Route?): SettingsStorage.Settings {
        fun getFilters(collection: ForeignCollection<ItemSeatFilter>?): MutableList<SeatFilter> {
            var seatFilters: MutableList<SeatFilter> = mutableListOf()
            collection?.forEach { seatFilters.add(SeatFilter(it.id, it.code, it.amount)) }
            return seatFilters
        }
        return SettingsStorage.Settings(it?.id, Station(it?.stationFromName, it?.stationFromId), Station(it?.stationToName, it?.stationToId), it?.numberTrain, LocalDateTime.fromDateFields(it?.dateRoute),
                it?.needPeriodicCheck ?: false, it?.period, getFilters(it?.seatFilters))
    }

    fun getRequestById(id: Int): SettingsStorage.Settings {
        val route = routeDAO?.getRouteById(Integer(id))
        return transformSettingFromRoute(route)
    }


}

