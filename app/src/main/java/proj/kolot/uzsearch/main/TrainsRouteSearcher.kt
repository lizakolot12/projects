package proj.kolot.uzsearch.main

import org.joda.time.LocalDateTime
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.Station


interface TrainsRouteSearcher{

    fun findStations(name:String): List<proj.kolot.uzsearch.data.Station>
    fun getTrains(from: proj.kolot.uzsearch.data.Station, to: proj.kolot.uzsearch.data.Station, departureDate: org.joda.time.LocalDateTime): proj.kolot.uzsearch.data.Response
}