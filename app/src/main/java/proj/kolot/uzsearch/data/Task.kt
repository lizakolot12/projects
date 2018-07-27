package proj.kolot.uzsearch.data


    data class Task(
            var id:Int?,

            var stationFrom: proj.kolot.uzsearch.data.Station?,
            var stationTo: proj.kolot.uzsearch.data.Station?,
            var numberTrain: String? = null,
            var dateRoute: org.joda.time.LocalDateTime?,
            var needPeriodicCheck: Boolean,
            var period: Long?,
            var seatFilters: MutableList<SeatFilter>?

    )





