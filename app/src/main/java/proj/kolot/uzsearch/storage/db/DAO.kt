package proj.kolot.uzsearch.storage.db

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource

class RouteDAO(connectionSource: ConnectionSource, dataClass: Class<Route>) : BaseDaoImpl<Route, Int>(connectionSource, dataClass) {

    fun getAllRoutes(): List<Route> {
        return this.queryForAll()
    }

    fun getRouteById(id: Int): Route {
        return this.queryForId(id);
    }

    fun getRoutesByNeedPeriodic(needPeriodic: Boolean): List<Route> {
        return this.queryForEq("need_periodic_check", needPeriodic)
    }


}


class FilterDAO(connectionSource: ConnectionSource, dataClass: Class<ItemSeatFilter>) : BaseDaoImpl<ItemSeatFilter, Int>(connectionSource, dataClass) {

    fun getAllFilters(): List<ItemSeatFilter> {
        return this.queryForAll()
    }
}