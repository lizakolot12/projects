package proj.kolot.uzsearch.service

import android.app.IntentService
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.R
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.SeatType
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.route.RouteActivity
import proj.kolot.uzsearch.storage.Storage
import java.util.Collections.emptyMap
import javax.inject.Inject




class SearchService : IntentService("SearchService") {

    //private val LOG = LoggerFactory.getLogger("SearchService")

    @Inject
    lateinit var trainsProvider: TrainsProvider
    @Inject
    lateinit var requestStorage: Storage

    private var id: Int = -1
    private var task: Task? = null

    init {
        MainApplication.graph.inject(this)
    }

    companion object {
        private val ARGUMENT_ID = "argument_id_route_for_service"

        fun newIntent(context:Context, id: Int): Intent {
            var intent: Intent = Intent(context, SearchService::class.java)
            intent.putExtra(ARGUMENT_ID, id)
            return intent
        }
    }

    override fun onHandleIntent(intent: Intent) {
        Log.e("my test", " on handle intent seaarch service " + intent.getIntExtra(ARGUMENT_ID, -1))
        if (!isNetworkAvailableAndConnected()) {
            return
        }
        searchTrains(intent)


    }

    private fun searchTrains(intent: Intent) {
        id = intent.getIntExtra(ARGUMENT_ID, -1)
        task = requestStorage.getRequestById(id)
        var result: Response = trainsProvider.getTrains(task as Task)
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
        var intent: Intent = RouteActivity.newIntent(baseContext,id )
        intent.data = Uri.parse(id.toString())
        //var pendingIntent: PendingIntent = PendingIntent.getActivity(baseContext, id, intent, FLAG_ACTIVITY_CLEAR_TOP)


        val stackBuilder = TaskStackBuilder.create(this)
// Adds the back stack
        stackBuilder.addParentStack(RouteActivity::class.java)
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent)
// Gets a PendingIntent containing the entire back stack
        val resultPendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

        val vibrate = longArrayOf(1000, 1000, 1000, 1000, 1000)
        val ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        var notif: NotificationCompat.Builder = NotificationCompat.Builder(baseContext)
        notif.setTicker(getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_train_white_18dp)
                .setContentTitle(getString(R.string.notification_title) + ":"+ task?.stationFrom?.name + " -> " + task?.stationTo?.name)
                .setContentText(getContentText(trains))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND and Notification.DEFAULT_VIBRATE)
                .setVibrate(vibrate)
                .setSound(ringURI)
        var nm: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        nm.notify(id, notif.build())

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
