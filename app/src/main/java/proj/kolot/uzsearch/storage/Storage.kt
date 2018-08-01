package proj.kolot.uzsearch.storage

import android.util.Log
import com.j256.ormlite.dao.ForeignCollection
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.data.SeatFilter
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.storage.db.*

/**
 * Created by Kolot Liza on 6/1/18.
 */
class Storage {
    var routeDAO: RouteDAO? = HelperFactory.helper?.getRouteDAO()
    var filterDAO: FilterDAO? = HelperFactory.helper?.getFilterDAO()

    fun updateRequest(route: Task): Int? {
        var routeRequest = Route(route.stationFrom?.id,
                route.stationFrom?.name,
                route.stationTo?.id,
                route.stationTo?.name,
                route.numberTrain,
                route.dateRoute?.toDate(),
                route.needPeriodicCheck,
                route.period)
        routeRequest.id = route.id ?: -1
        route.seatFilters?.forEach {
            var item = ItemSeatFilter(it.code, it.amount, routeRequest)

            if (it.id == -1) {
                filterDAO?.create(item)
            } else {
                item.id = it.id ?: -1
                filterDAO?.update(item)
            }
        }

        var resUpt = routeDAO?.update(routeRequest)
        Log.e("my test", " rez update = " + resUpt)
        return route.id
    }

    fun createRequest(route: Task): Int {
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

    fun getRequestByNeedPeriodic(needPeriodic: Boolean): List<Task> {
        Log.e("my test", " request data from data base by need periodic " + needPeriodic)
        var result: MutableList<Task> = mutableListOf()

        routeDAO?.getRoutesByNeedPeriodic(needPeriodic)?.forEach {
            result.add(transformSettingFromRoute(it))
        }
        return result
    }

    fun getAllRequest(): List<Task> {

        Log.e("my test", " request data from data base")
        var result: MutableList<Task> = mutableListOf()

        routeDAO?.getAllRoutes()?.forEach {
            result.add(transformSettingFromRoute(it))
        }
        return result
    }

    private fun transformSettingFromRoute(it: Route?): Task {
        fun getFilters(collection: ForeignCollection<ItemSeatFilter>?): MutableList<SeatFilter> {
            var seatFilters: MutableList<SeatFilter> = mutableListOf()
            collection?.forEach { seatFilters.add(SeatFilter(it.id, it.id, it.code, it.amount)) } //
            return seatFilters
        }
        return Task(it?.id, Station(it?.stationFromName, it?.stationFromId), Station(it?.stationToName, it?.stationToId), it?.numberTrain, LocalDateTime.fromDateFields(it?.dateRoute),
                it?.needPeriodicCheck ?: false, it?.period, getFilters(it?.seatFilters))
    }

    fun getRequestById(id: Int): Task {
        val route = routeDAO?.getRouteById(id)
        return transformSettingFromRoute(route)
    }

    fun delete(task: Task) {
        val id = task.id ?: -1
        routeDAO?.deleteById(id)
    }


}

