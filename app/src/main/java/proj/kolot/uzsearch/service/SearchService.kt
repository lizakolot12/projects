package proj.kolot.uzsearch.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v4.app.NotificationManagerCompat
import proj.kolot.uzsearch.MainApplication
import proj.kolot.uzsearch.data.Response
import proj.kolot.uzsearch.data.Task
import proj.kolot.uzsearch.data.TransportRoute
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.storage.Storage
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
        private const val ARGUMENT_ID = "argument_id_route_for_service"

        fun newIntent(context:Context, id: Int): Intent {
            val intent: Intent = Intent(context, SearchService::class.java)
            intent.putExtra(ARGUMENT_ID, id)
            return intent
        }
    }

    override fun onHandleIntent(intent: Intent) {
        if (!isNetworkAvailableAndConnected()) {
            return
        }
        searchTrains(intent)


    }

    private fun searchTrains(intent: Intent) {
        id = intent.getIntExtra(ARGUMENT_ID, -1)
        task = requestStorage.getRequestById(id)
        val result: Response = trainsProvider.getTrains(task as Task)
        if (needNotification(result)) {
            showFoundTrains(result.list as List<TransportRoute>)
        }

    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isNetworkAvailable: Boolean = manager.getActiveNetworkInfo() != null
        val isNetworkConnected: Boolean = isNetworkAvailable && manager.getActiveNetworkInfo().isConnected
        return isNetworkConnected
    }





    private fun showFoundTrains(trains: List<TransportRoute>) {
        val nm: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        nm.notify(id, NotificationFactory().createNotification(trains, task))

    }



    private fun needNotification(response: Response): Boolean {
        return response.list != null && response.list?.size ?: 0 > 0
    }

}
