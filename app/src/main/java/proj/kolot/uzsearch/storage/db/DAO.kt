package proj.kolot.uzsearch.storage.db

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource

class RouteDAO : BaseDaoImpl<Route, Integer> {

    constructor(connectionSource: ConnectionSource,
                dataClass: Class<Route>) : super(connectionSource, dataClass) {

    }


    /*

       fun createRoute(route: Route): Int{
           return this.create(route)
       }*/

    fun getAllRoutes(): List<Route> {
        return this.queryForAll()
    }

    fun getRouteById(id: Integer): Route {
        return this.queryForId(id);
    }

    fun getRoutesByNeedPeriodic(needPeriodic: Boolean): List<Route> {
        return this.queryForEq("need_periodic_check", needPeriodic)
    }


}


class FilterDAO : BaseDaoImpl<ItemSeatFilter, Integer> {

    constructor(connectionSource: ConnectionSource,
                dataClass: Class<ItemSeatFilter>) : super(connectionSource, dataClass) {

    }

    fun getAllFilters(): List<ItemSeatFilter> {
        return this.queryForAll();
    }
}