package proj.kolot.uzsearch.main

import io.github.sunlaud.findticket.client.uz.UzTrainRouteSearchService
import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.Station


class TrainsRouteSearcherImpl : TrainsRouteSearcher {
    val routeSearchService: UzTrainRouteSearchService = UzTrainRouteSearchService()

    override fun findStations(name: String): List<Station> {
        var stations: List<io.github.sunlaud.findticket.model.Station> = routeSearchService.findStations(name)
        var result: ArrayList<Station> = ArrayList<Station>(stations.size)
        stations.forEach { result.add(transformStation(it)) }

        return result
    }


    override fun getTrains(from: Station, to: Station, departureDate: LocalDateTime): Response {
        var stationFrom = io.github.sunlaud.findticket.model.Station(from.name?:"", from.id?:"")
        var stationTo = io.github.sunlaud.findticket.model.Station(to.name?:"", to.id?:"")
        var response: Response = Response()
        try {
            var list: ArrayList<proj.kolot.uzsearch.data.TransportRoute> = ArrayList()

            var result = routeSearchService.findRoutes(stationFrom, stationTo, departureDate)
            result.forEach({
                list.add(proj.kolot.uzsearch.data.TransportRoute(it.id, it.name, transformStation(it.from),
                        transformStation(it.till), it.departureDate, it.arrivalDate, it.travelTime, transformSeatType(it.freeSeatsCountByType)))
            })
            response.list = list
        } catch(ex: Exception) {
            response.message = ex.message
        }
        return response
    }


    private fun transformStation(srStation: io.github.sunlaud.findticket.model.Station): Station {
        return Station(srStation.name, srStation.id)
    }

    private fun transformSeatType(source: Map<io.github.sunlaud.findticket.model.SeatType, Int>): Map<proj.kolot.uzsearch.data.SeatType, Int> {
        var result: HashMap<proj.kolot.uzsearch.data.SeatType, Int> = HashMap()
        for (entry  in source){
            result.put(proj.kolot.uzsearch.data.SeatType(entry.key.id, entry.key.name), entry.value)
        }
     //   source.forEach { t, u -> result.put(proj.kolot.uzsearch.data.SeatType(t.id, t.name), u) }
        return result
    }
}