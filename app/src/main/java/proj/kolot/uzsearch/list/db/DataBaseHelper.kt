package proj.kolot.uzsearch.list.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils


class DatabaseHelper constructor(context: Context): OrmLiteSqliteOpenHelper(context, "routes.db", null, 1) {

    private var routeDAO: RouteDAO? = null
    private var filterDAO: FilterDAO? = null



    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTable(connectionSource, Route::class.java)
        TableUtils.createTable(connectionSource, ItemSeatFilter::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        //TableUtils.dropTable(connectionSource, Route::class.java, true)
        // TableUtils.dropTable(connectionSource, ItemSeatFilter::class.java, true)
        TableUtils.dropTable(routeDAO, true)
        TableUtils.dropTable(filterDAO, true)
        onCreate(database, connectionSource)
    }

    fun getRouteDAO(): RouteDAO {
        if (routeDAO == null) {
            routeDAO = RouteDAO(getConnectionSource(), Route::class.java)
        }
        return routeDAO as RouteDAO;
    }

    fun getFilterDAO(): FilterDAO {
        if (filterDAO == null) {
            filterDAO = FilterDAO(getConnectionSource(), ItemSeatFilter::class.java)
        }
        return filterDAO as FilterDAO;
    }


    override fun close() {
        super.close()
        routeDAO = null;
        filterDAO = null;
    }

}
