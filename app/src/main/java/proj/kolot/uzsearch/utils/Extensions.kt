package proj.kolot.uzsearch.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import proj.kolot.uzsearch.data.TransportRoute

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun filterRoutes(source: List<TransportRoute>, filters: Map<String, Int>): List<TransportRoute> {
    val processedResult: List<TransportRoute>
    if (filters.isEmpty()) {
        return source
    }
    processedResult = source.filter { rout ->
        var result = false
        //System.out.println("Source list:")
        filters.forEach { needName, needAmount ->
            //System.out.println("Map filters:")
            rout.freeSeatsCountByType?.filter { seat ->
                //System.out.println(seat.key.id + "   "+ needName + "  " +(seat.key.id == needName) )
                seat.key.id == needName }
                    ?.forEach { seatType, amountPlace ->
                       // System.out.println(needName + "  " + amountPlace + "  >= " + needAmount + "   " + ((amountPlace >= needAmount)))
                        if (amountPlace >= needAmount) {
                            result = true
                        }
                    }
        }
        result
    }

    return processedResult

}
