package proj.kolot.uzsearch.data

import org.joda.time.LocalDateTime
import org.joda.time.Period
import java.net.URL


data class TransportRoute(var id: String? = null,
                          var name: String? = null,
                          var from: Station? = null,
                          var till: Station? = null,
                          var departureDate: LocalDateTime? = null,
                          var arrivalDate: LocalDateTime? = null,
                          var travelTime: Period? = null,
                          var freeSeatsCountByType: Map<SeatType, Int>? = null,
                          var url: URL? = null)

