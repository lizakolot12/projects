package proj.kolot.uzsearch.service

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.ConnectivityManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.SeatType
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.list.ContentActivity
import proj.kolot.uzsearch.main.TrainsProvider
import java.util.Collections.emptyMap
import javax.inject.Inject


class SearchService : IntentService("SearchService") {

    //private val LOG = LoggerFactory.getLogger("SearchService")

    @Inject
    lateinit var trainsProvider: TrainsProvider

    init {
        MainApplication.graph.inject(this)
    }


    override fun onHandleIntent(intent: Intent) {
        if (!isNetworkAvailableAndConnected()) {
            return
        }
        searchTrains()


    }

    private fun searchTrains() {
        var result: Response = trainsProvider.getTrains()
        if (needNotification(result)) {
            showFoundTrains(result.list as List<TransportRoute>)
        }

    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        var manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isNetworkAvailable: Boolean = manager.getActiveNetworkInfo() != null
        var isNetworkConnected: Boolean = isNetworkAvailable && manager.getActiveNetworkInfo().isConnected
        return isNetworkConnected
    }





    fun showFoundTrains(trains: List<TransportRoute>) {
        var intent: Intent = Intent(baseContext, ContentActivity::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getActivity(baseContext, 1, intent, FLAG_ACTIVITY_CLEAR_TOP)
        var notif: NotificationCompat.Builder = NotificationCompat.Builder(baseContext)
        notif.setTicker(getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_train_white_18dp)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getContentText(trains))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        var nm: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        nm.notify(0, notif.build())

    }

    private fun getContentText(trains: List<TransportRoute>): String {
        var content: String = ""
        var row: String
        for (route: TransportRoute in trains) {
            row = route.id + " " + route.name + " "
            for (entry: Map.Entry<SeatType, Int> in route.freeSeatsCountByType ?: emptyMap()) {
                row += entry.key.id + ":" + entry.value + ", "
            }
            row += "\n"
            content += row
        }
        return content
    }

    fun needNotification(response: Response): Boolean {
        return response.list != null && response.list?.size ?: 0 > 0
    }

}
