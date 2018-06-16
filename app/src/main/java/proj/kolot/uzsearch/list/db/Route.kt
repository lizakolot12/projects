package proj.kolot.uzsearch.list.db

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

/**
 * Created by Kolot Liza on 5/22/18.
 */
@DatabaseTable(tableName = "route")
class Route  {
    constructor(stationFromId: String?,
                stationFromName: String?,
                stationToId: String?,
                stationToName: String?,
                numberTrain: String?,
                dateRoute: Date?,
                needPeriodicCheck: Boolean,
                period: Long?  ) : super() {
        this.stationFromId  = stationFromId
        this.stationFromName = stationFromName
        this.stationToId = stationToId
        this.stationToName = stationToName
        this.numberTrain = numberTrain
        this.dateRoute = dateRoute
        this.needPeriodicCheck = needPeriodicCheck
        this.period = period
    }

    constructor() : super()

    @DatabaseField(generatedId = true)
    var id:Int = 0

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "station_from_id")
    var stationFromId: String? = null

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "station_from_name")
    var stationFromName: String? = null

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "station_to_id")
    var stationToId: String? = null

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "station_to_name")
    var stationToName: String? = null

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "number_train")
    var numberTrain: String? = null

    @DatabaseField(columnName = "date_route", dataType = DataType.DATE)
    var dateRoute: Date? = null

    @DatabaseField(canBeNull = false, dataType = DataType.BOOLEAN, columnName = "need_periodic_check")
    var needPeriodicCheck: Boolean = false

    @DatabaseField(columnName = "period")
    var period: Long? = null

    @ForeignCollectionField(eager = true)
    var seatFilters: ForeignCollection<ItemSeatFilter>? = null



}



@DatabaseTable(tableName = "seat_filter", daoClass = FilterDAO::class)
class ItemSeatFilter{
    @DatabaseField(generatedId = true)
    var id:Int = 0

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "name_seat")
    var code:String? = null

    @DatabaseField(columnName = "amount")
    var amount:Int? = 0


    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    var route: Route? = null


    constructor() : super()
    constructor(code: String?, amount: Int?, route: Route) : super() {
        this.code = code
        this.amount = amount
        this.route = route
    }



}