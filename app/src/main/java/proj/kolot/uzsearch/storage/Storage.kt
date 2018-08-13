package proj.kolot.uzsearch.storage

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.ForeignCollection
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.data.SeatFilter
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.storage.db.*
import java.util.*

/**
 * Created by Kolot Liza on 6/1/18.
 */

class Storage : Observable() {
    private var routeDAO: RouteDAO? = HelperFactory.helper?.getRouteDAO()
    private var filterDAO: FilterDAO? = HelperFactory.helper?.getFilterDAO()

    private var routeDaoObserver: Dao.DaoObserver = Dao.DaoObserver {
        setChanged()
        notifyObservers(true)
    }


    override fun addObserver(o: Observer?) {
        if (countObservers() == 0) {
            routeDAO?.registerObserver(routeDaoObserver)
        }
        super.addObserver(o)

    }

    override fun deleteObserver(o: Observer?) {
        super.deleteObserver(o)
        if (countObservers() == 0) {
            routeDAO?.unregisterObserver(routeDaoObserver)
        }
    }

    fun updateRequest(route: Task): Int? {
        val routeRequest = convert(route)
        routeRequest.id = route.id ?: -1
        var item: ItemSeatFilter
        route.seatFilters?.forEach {
            item = convert(it, routeRequest)
            if (it.id == -1) {
                filterDAO?.create(item)
            } else {
                item.id = it.id ?: -1
                filterDAO?.update(item)
            }
        }

        var resUpt = routeDAO?.update(routeRequest)
        return route.id
    }

    fun createRequest(route: Task): Int {
        val routeRequest = convert(route)
        var id1: Int? = routeDAO?.create(routeRequest)
        route.seatFilters?.forEach {
            filterDAO?.create(convert(it, routeRequest))
        }
        return routeRequest.id
    }

    private fun convert(filter: SeatFilter, route: Route): ItemSeatFilter = ItemSeatFilter(filter.code, filter.amount, route)

    private fun convert(route: Task): Route = Route(route.stationFrom?.id,
            route.stationFrom?.name,
            route.stationTo?.id,
            route.stationTo?.name,
            route.numberTrain,
            route.dateRoute?.toDate(),
            route.needPeriodicCheck,
            route.period)

    fun getRequestByNeedPeriodic(needPeriodic: Boolean): List<Task> {
        val result: MutableList<Task> = mutableListOf()

        routeDAO?.getRoutesByNeedPeriodic(needPeriodic)?.forEach {
            result.add(transformSettingFromRoute(it))
        }
        return result
    }

    fun getAllRequest(): List<Task> {
        val result: MutableList<Task> = mutableListOf()
        routeDAO?.getAllRoutes()?.forEach {
            result.add(transformSettingFromRoute(it))
        }
        return result
    }

    private fun transformSettingFromRoute(it: Route?): Task {
        fun getFilters(collection: ForeignCollection<ItemSeatFilter>?): MutableList<SeatFilter> {
            val seatFilters: MutableList<SeatFilter> = mutableListOf()
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

