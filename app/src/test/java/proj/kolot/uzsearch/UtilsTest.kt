package proj.kolot.uzsearch


import org.joda.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import proj.kolot.uzsearch.data.SeatType
import proj.kolot.uzsearch.data.Station
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.utils.filterRoutes
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Kolot Liza on 3/15/18.
 */
class UtilsTest {
    var date: LocalDateTime = LocalDateTime.now()

    @Test
    fun testFilterRoute() {
        getResult().forEach { t, u ->
            var filtersRoutes = filterRoutes(getSourceList(), t)
            assertEquals(u, filtersRoutes)
        }
    }

    fun getResult(): Map<Map<String, Int>, List<TransportRoute>> {
        var result: MutableMap<Map<String, Int>, List<TransportRoute>> = HashMap<Map<String, Int>, List<TransportRoute>>()
        val sourceBase: List<TransportRoute> = getSourceList()

        val source1: MutableList<TransportRoute> = ArrayList<TransportRoute>()
        var mapFilters1: MutableMap<String, Int> = mutableMapOf("П" to 22, "К" to 2, "Л" to 3)
        source1.add(sourceBase.get(0))
        source1.add(sourceBase.get(1))
        source1.add(sourceBase.get(2))
        source1.add(sourceBase.get(3))
        source1.add(sourceBase.get(4))
        result.put(mapFilters1, source1)


        var mapFilters2: MutableMap<String, Int> = mutableMapOf()
        result.put(mapFilters2, sourceBase)

        val source3: MutableList<TransportRoute> = ArrayList<TransportRoute>()
        var mapFilters3: MutableMap<String, Int> = mutableMapOf("П" to 4)
        source3.add(sourceBase.get(0))
        source3.add(sourceBase.get(1))
        source3.add(sourceBase.get(3))
        result.put(mapFilters3, source3)

        val source4: MutableList<TransportRoute> = ArrayList<TransportRoute>()
        var mapFilters4: MutableMap<String, Int> = mutableMapOf("П" to 45, "К" to 45, "Л" to 94)
        result.put(mapFilters4, source4)


        return result
    }

    private fun getSourceList(): List<TransportRoute> {
        val source: ArrayList<TransportRoute> = ArrayList<TransportRoute>()

        var seats1 = HashMap<SeatType, Int>()
        seats1.put(SeatType("П", "П"), 23)
        seats1.put(SeatType("Л", "Л"), 43)
        seats1.put(SeatType("К", "К"), 34)
        source.add(TransportRoute("1",
                "name1",
                Station("1", "1"),
                Station("11", "11"),
                date,
                date,
                null, seats1))


        var seats2 = HashMap<SeatType, Int>()
        seats2.put(SeatType("П", "П"), 23)
        seats2.put(SeatType("К", "К"), 7)
        source.add(TransportRoute("2",
                "name2",
                Station("2", "2"),
                Station("22", "22"),
                date,
                date,
                null, seats2))

        var seats3 = HashMap<SeatType, Int>()
        seats3.put(SeatType("П", "П"), 1)
        seats3.put(SeatType("Л", "Л"), 93)
        source.add(TransportRoute("3",
                "name3",
                Station("3", "3"),
                Station("33", "33"),
                date,
                date,
                null, seats3))

        var seats4 = HashMap<SeatType, Int>()
        seats4.put(SeatType("П", "П"), 23)
        source.add(TransportRoute("4",
                "name4",
                Station("4", "4"),
                Station("44", "44"),
                date,
                date,
                null, seats4))

        var seats5 = HashMap<SeatType, Int>()
        seats5.put(SeatType("П", "П"), 2)
        seats5.put(SeatType("Л", "Л"), 2)
        seats5.put(SeatType("К", "К"), 2)
        source.add(TransportRoute("5",
                "name5",
                Station("5", "5"),
                Station("55", "55"),
                date,
                date,
                null, seats5))


        var seats6 = HashMap<SeatType, Int>()
        source.add(TransportRoute("6",
                "name6",
                Station("6", "6"),
                Station("66", "66"),
                date,
                date,
                null, seats6))
        return source

    }
}